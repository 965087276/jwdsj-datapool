package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class EsIndexServiceImpl implements EsIndexService {
    @Autowired
    private EsIndexRepo esIndexRepo;
    @Autowired
    private ElasticRestService elasticRestService;
    @Value("${elasticsearch.index-prefix}")
    private String indexPrefix;

    @Override
    @Transactional
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
}
