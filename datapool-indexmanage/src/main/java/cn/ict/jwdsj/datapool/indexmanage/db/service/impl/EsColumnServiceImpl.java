package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class EsColumnServiceImpl implements EsColumnService {
    @Autowired private EsColumnRepo esColumnRepo;
    @Autowired private ElasticRestService elasticRestService;
    @Autowired private MappingColumnRepo mappingColumnRepo;
    @Autowired private EsIndexRepo esIndexRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MappingTableAddDTO mappingTableAddDTO) throws IOException {

        EsIndex esIndex = esIndexRepo.findById(mappingTableAddDTO.getIndexId());
        long tableId = mappingTableAddDTO.getTableId();

        Set<String> esColumnsAll = esColumnRepo.findByEsIndex(esIndex).stream().map(EsColumn::getName).collect(Collectors.toSet());

        // 筛选出该表在mapping_column中但不在es_column中的字段，这些字段需要加入elasticsearch与es_column中
        List<EsColumn> esColumns = mappingColumnRepo.listColumnTypeDTOByTableId(tableId)
                .stream()
                .filter(columnTypeDTO -> !(esColumnsAll.contains(columnTypeDTO.getName())))
                .map(columnTypeDTO -> BeanUtil.toBean(columnTypeDTO, EsColumn.class))
                .collect(Collectors.toList());
        esColumns.forEach(esColumn -> esColumn.setEsIndex(esIndex));

        esColumnRepo.saveAll(esColumns);
        elasticRestService.addFields(esIndex, esColumns);

    }

    /**
     * 删除某索引下的字段
     *
     * @param indexId 索引的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIndexId(long indexId) {
        EsIndex esIndex = esIndexRepo.findById(indexId);
        esColumnRepo.deleteByEsIndex(esIndex);
    }

}
