package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingColumn;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingColumnDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.feignclient.DictClient;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.feignclient.StatClient;
import cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class MappingColumnServiceImpl implements MappingColumnService {
    @Autowired private MappingColumnRepo mappingColumnRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private DictClient dictClient;
    @Autowired private StatClient statClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(SeTableAddDTO seTableAddDTO) {
        Map<EsColumnTypeEnum, List<MappingColumnDTO>> columnsGroupByType = seTableAddDTO.getColumns()
                .stream()
                .collect(groupingBy(this::getColumnType));
        List<MappingColumn> mappingColumns = new ArrayList<>();
        DictTable dictTable = DictTable.builtById(seTableAddDTO.getTableId());

        columnsGroupByType.forEach((typeEnum, list) -> {
            for (int i = 0; i < list.size(); ++i) {
                MappingColumnDTO mappingColumnDTO = list.get(i);

                MappingColumn mappingColumn = BeanUtil.toBean(mappingColumnDTO, MappingColumn.class);
                mappingColumn.setDictColumnId(mappingColumnDTO.getDictColumnId());
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
                .select(mappingColumn.dictColumnId, mappingColumn.esColumn, mappingColumn.type)
                .from(mappingColumn)
                .where(mappingColumn.dictTable.eq(dictTable))
                .fetch()
                .stream()
                .map(tuple -> ColumnTypeDTO.builder()
                        .dictColumnId(tuple.get(mappingColumn.dictColumnId))
                        .name(tuple.get(mappingColumn.esColumn))
                        .type(tuple.get(mappingColumn.type))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<MappingColumnVO> getInitMappingColumns(long databaseId, long tableId) {

        List<DictColumn> dictColumns = dictClient.listDictColumnsByTableId(tableId);
        // 缺陷字段
        List<String> defectColumns = statClient.getDefectColumnsByTable(tableId);

        return dictColumns
                .stream().filter(dictColumn -> !defectColumns.contains(dictColumn.getEnColumn())) // 过滤缺陷字段
                .map(this::convertToMappingColumnVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取全量读取数据时的字段信息
     *
     * @param tableId 表id
     * @return 字段信息。包括要加入到搜索引擎的字段列表，表字段到索引字段的映射。
     */
    @Override
    public TableFullReadDTO getTableFullReadDTOByTableId(long tableId) {
        List<DictColumn> dictColumns = dictClient.listDictColumnsByTableId(tableId);
        List<ColumnTypeDTO> columnTypeDTOS = listColumnTypeDTOByTable(DictTable.builtById(tableId));

        // 表字段id与表字段名的映射
        Map<Long, String> colIdAndColNameMap = dictColumns
                .stream().collect(toMap(DictColumn::getId, dictColumn -> dictColumn.getEnColumn()));
        // 表字段id与索引字段名的映射
        Map<Long, String> colIdAndEsColMap = columnTypeDTOS
                .stream().collect(toMap(ColumnTypeDTO::getDictColumnId, columnTypeDTO -> columnTypeDTO.getName()));

        TableFullReadDTO tableFullReadDTO = new TableFullReadDTO();

        // 表字段名与索引字段名的映射
        Map<String, String> colAndEsColMap = new HashMap<>();
        colIdAndEsColMap.forEach((colId, esColumn) -> {
            colAndEsColMap.put(colIdAndColNameMap.get(colId), esColumn);
        });

        // 需要加入到搜索引擎的字段列表
        List<String> columns = colAndEsColMap.keySet().stream().collect(Collectors.toList());

        tableFullReadDTO.setColumns(columns);
        tableFullReadDTO.setColAndEsColMap(colAndEsColMap);

        return tableFullReadDTO;

    }

    private MappingColumnVO convertToMappingColumnVO(DictColumn dictColumn) {
        MappingColumnVO columnVO = new MappingColumnVO();
        columnVO.setEnColumn(dictColumn.getEnColumn());
        columnVO.setChColumn(dictColumn.getChColumn());
        columnVO.setDictColumnId(dictColumn.getId());
        columnVO.setDisplayed(true);
        columnVO.setSearched(false);
        columnVO.setAnalyzed(false);
        columnVO.setBoost(1);
        return columnVO;
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
