package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
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
    @Autowired private MappingColumnService mappingColumnService;
    @Autowired private EsIndexService esIndexService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MappingTableAddDTO mappingTableAddDTO) throws IOException {

        EsIndex esIndex = esIndexService.findById(mappingTableAddDTO.getIndexId());
        long dictTableId = mappingTableAddDTO.getTableId();

        Set<String> esColumnsAll = esColumnRepo.findByEsIndex(esIndex).stream().map(EsColumn::getName).collect(Collectors.toSet());

        // 筛选出该表在mapping_column中但不在es_column中的字段，这些字段需要加入elasticsearch与es_column中
        List<EsColumn> esColumns = mappingColumnService.listColumnTypeDTOByDictTableId(dictTableId)
                .stream()
                .filter(columnTypeDTO -> !(esColumnsAll.contains(columnTypeDTO.getName()))) // 这里效率低，需要优化
                .map(columnTypeDTO -> BeanUtil.toBean(columnTypeDTO, EsColumn.class))
                .collect(Collectors.toList());
        esColumns.forEach(esColumn -> esColumn.setEsIndex(esIndex));

        esColumnRepo.saveAll(esColumns);
        elasticRestService.addFields(esIndex, esColumns);

    }

}
