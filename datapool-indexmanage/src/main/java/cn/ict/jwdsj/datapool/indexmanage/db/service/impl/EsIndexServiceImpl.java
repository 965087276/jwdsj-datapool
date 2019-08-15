package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@Service
public class EsIndexServiceImpl implements EsIndexService {
    @Autowired
    private EsIndexRepo esIndexRepo;
    @Autowired
    private ElasticRestService elasticRestService;
    @Autowired
    private MappingTableRepo mappingTableRepo;
    @Autowired
    private EsColumnRepo esColumnRepo;

    @Value("${elasticsearch.index-prefix}")
    private String indexPrefix;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createIndex(EsIndexDTO indexDTO) throws IOException {

        EsIndex esIndex = BeanUtil.toBean(indexDTO, EsIndex.class);
        esIndex.setIndexName(indexPrefix + esIndex.getIndexName());
        esIndexRepo.save(esIndex);
        elasticRestService.createIndex(indexDTO);
    }

    @Override
    public EsIndex findById(long indexId) {
        return esIndexRepo.findById(indexId);
    }

    @Override
    public List<EsIndex> listAll() {
        return esIndexRepo.findAll();
    }

    /**
     * 删除索引
     *
     * @param indexId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIndexById(long indexId) throws IOException {

        EsIndex esIndex = esIndexRepo.findById(indexId);
        String indexName = esIndex.getIndexName();

        // 不能有表存在于数据同步列表中
        Assert.isTrue(!mappingTableRepo.existsByIndexId(indexId), "该索引下还有表在同步，请先从数据同步列表中删除");

        // 删除该表的索引字段信息
        esColumnRepo.deleteByEsIndex(esIndex);

        // 删除EsIndex表的信息
        esIndexRepo.deleteById(indexId);

        // 删除elasticsearch中的索引
        elasticRestService.deleteIndex(indexName);
    }
}
