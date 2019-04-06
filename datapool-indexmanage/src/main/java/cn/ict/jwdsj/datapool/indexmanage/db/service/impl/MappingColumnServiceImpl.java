package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.MappingColumn;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.QMappingColumn;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.TableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingColumnDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MappingColumnServiceImpl implements MappingColumnService {
    @Autowired
    private MappingColumnRepo mappingColumnRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void save(TableAddDTO tableAddDTO) {
        Map<EsColumnTypeEnum, List<MappingColumnDTO>> columnsGroupByType = tableAddDTO.getColumns()
                .stream()
                .collect(Collectors.groupingBy(this::getColumnType));
        List<MappingColumn> mappingColumns = new ArrayList<>();
        DictTable dictTable = DictTable.builtById(tableAddDTO.getTableId());

        columnsGroupByType.forEach((typeEnum, list) -> {
            for (int i = 0; i < list.size(); ++i) {
                MappingColumnDTO mappingColumnDTO = list.get(i);

                MappingColumn mappingColumn = BeanUtil.toBean(mappingColumnDTO, MappingColumn.class);
                mappingColumn.setDictColumn(DictColumn.builtById(mappingColumnDTO.getDictColumnId()));
                mappingColumn.setDictTable(dictTable);
                mappingColumn.setEsColumn(typeEnum.name() + "-" + (i+1));
                mappingColumn.setType(typeEnum.name());

                mappingColumns.add(mappingColumn);
            }
        });

        mappingColumnRepo.saveAll(mappingColumns);

    }

    @Override
    public List<ColumnTypeDTO> listColumnTypeDTOByTable(DictTable dictTable) {
        QMappingColumn mappingColumn = QMappingColumn.mappingColumn;
        return jpaQueryFactory
                .select(mappingColumn.dictColumn.id, mappingColumn.esColumn, mappingColumn.type)
                .from(mappingColumn)
                .where(mappingColumn.dictTable.eq(dictTable))
                .fetch()
                .stream()
                .map(tuple -> ColumnTypeDTO.builder()
                        .dictColumnId(tuple.get(mappingColumn.dictColumn.id))
                        .name(tuple.get(mappingColumn.esColumn))
                        .type(tuple.get(mappingColumn.type))
                        .build()
                )
                .collect(Collectors.toList());
    }

    private EsColumnTypeEnum getColumnType(MappingColumnDTO column) {
        boolean searched = column.isSearched();
        boolean analyzed = column.isAnalyzed();
        boolean displayed = column.isDisplayed();

        if (!searched || !displayed) {
            return EsColumnTypeEnum.NOT_SEARCH;
        } else if (!analyzed) {
            return EsColumnTypeEnum.KEYWORD;
        } else {
            return EsColumnTypeEnum.TEXT;
        }

    }

}