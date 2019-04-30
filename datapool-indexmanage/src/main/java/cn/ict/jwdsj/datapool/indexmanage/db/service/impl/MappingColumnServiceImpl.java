package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Pair;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.api.feign.StatsClient;
import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingColumnDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum.KEYWORD;
import static cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum.TEXT;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class MappingColumnServiceImpl implements MappingColumnService {
    @Autowired private MappingColumnRepo mappingColumnRepo;
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private DictClient dictClient;
    @Autowired private StatsClient statsClient;
    @Autowired private SeTableService seTableService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(SeTableAddDTO seTableAddDTO) {
        Map<String, List<MappingColumnDTO>> columnsGroupByType = seTableAddDTO.getColumns()
                .stream()
                .collect(groupingBy(this::getColumnType));

        // 若该操作为在已存在的表上新增字段，且该表已加入了数据同步，那么不能增加新字段
        Optional.ofNullable(seTableService.findByDictTableId(seTableAddDTO.getTableId()))
                .ifPresent(seTable -> {
                    Assert.isTrue(!seTable.isSync(), "该表在数据同步任务中，不能添加新字段");
                });

        // 字段的类型及其数量
        Map<String, Integer> typeAndCount = this.groupWithTypeAndCountByDictTableId(seTableAddDTO.getTableId());

        List<MappingColumn> mappingColumns = new ArrayList<>();

        columnsGroupByType.forEach((type, list) -> {
            // 数据库中已有的typeEnum字段类型的数量
            int typeCount = Optional.ofNullable(typeAndCount.get(type))
                    .orElse(new Integer(0))
                    .intValue();

            for (int i = 0; i < list.size(); ++i) {
                MappingColumnDTO mappingColumnDTO = list.get(i);
                MappingColumn mappingColumn = BeanUtil.toBean(mappingColumnDTO, MappingColumn.class);
                mappingColumn.setDictTableId(seTableAddDTO.getTableId());
                mappingColumn.setEsColumn(type + "-" + (++typeCount));
                mappingColumn.setType(type);

                mappingColumns.add(mappingColumn);
            }
        });

        mappingColumnRepo.saveAll(mappingColumns);

    }

    @Override
    public List<ColumnTypeDTO> listColumnTypeDTOByDictTableId(long dictTableId) {
        QMappingColumn mappingColumn = QMappingColumn.mappingColumn;
        return jpaQueryFactory
                .select(mappingColumn.dictColumnId, mappingColumn.esColumn, mappingColumn.type)
                .from(mappingColumn)
                .where(mappingColumn.dictTableId.eq(dictTableId))
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

    /**
     * 将某表的字段按照类型和数目进行group by
     *
     * @param dictTableId
     * @return
     */
    @Override
    public Map<String, Integer> groupWithTypeAndCountByDictTableId(long dictTableId) {
        QMappingColumn mappingColumn = QMappingColumn.mappingColumn;
        return jpaQueryFactory
                .select(mappingColumn.type, mappingColumn.count())
                .from(mappingColumn)
                .where(mappingColumn.dictTableId.eq(dictTableId))
                .groupBy(mappingColumn.type)
                .fetch()
                .stream()
                .collect(toMap(tuple -> tuple.get(mappingColumn.type), tuple -> tuple.get(mappingColumn.count().intValue())));
    }

    /**
     * 表搜索引擎管理--表信息增加--加载该表的字段
     * 需要剔除空字段
     * @param databaseId 库id
     * @param tableId 表id
     * @return
     */
    @Override
    public List<MappingColumnVO> getInitMappingColumns(long databaseId, long tableId) {

        List<DictColumn> dictColumns = dictClient.listDictColumnsByTableId(tableId);
        // 缺陷字段
        List<String> defectColumns = statsClient.getDefectColumnsByTable(tableId);

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
        List<ColumnTypeDTO> columnTypeDTOS = listColumnTypeDTOByDictTableId(tableId);

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

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     *
     * @param tableId 表id
     * @return
     */
    @Override
    public List<ColDisplayedDTO> listColDisplayedDTOByTableId(long tableId) {
        return mappingColumnRepo.findByDictTableIdAndDisplayed(tableId, true)
                .stream()
                .map(this::convertToColDisplayedDTO)
                .collect(Collectors.toList());
    }

    /**
     * 表信息管理--查看字段
     *
     * @param tableId
     * @return
     */
    @Override
    public List<MappingColumnVO> listMappingColumnVOs(long tableId) {
        return mappingColumnRepo.findByDictTableId(tableId)
                .stream()
                .map(this::convertToMappingColumnVO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDictTableId(long dictTableId) {
        mappingColumnRepo.deleteByDictTableId(dictTableId);
    }

    /**
     * 更新字段（未配置数据同步，可任意增删改字段）
     * 前台传输所有字段
     * @param seTableAddDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnsNotSync(SeTableAddDTO seTableAddDTO) {
        long dictTableId = seTableAddDTO.getTableId();
        mappingColumnRepo.deleteByDictTableId(dictTableId);
        this.saveAll(seTableAddDTO);
    }

    /**
     * 更新字段（已配置数据同步，可配置权重、非搜索字段的是否展示）
     * 前台只传输发生更新的字段
     * @param seTableAddDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnsHasSync(SeTableAddDTO seTableAddDTO) {
        long dictTableId = seTableAddDTO.getTableId();
        var columns = seTableAddDTO.getColumns();
        List<MappingColumn> mappingColumns = new ArrayList<>();
        for (var column : columns) {
            MappingColumn colUpd = mappingColumnRepo.findByDictColumnId(column.getDictColumnId());
            colUpd.setDisplayed(column.isDisplayed());
            colUpd.setBoost(column.getBoost());
            mappingColumns.add(colUpd);
        }
        mappingColumnRepo.saveAll(mappingColumns);
    }

    /**
     * 表信息管理--字段编辑--新增字段--返回未添加的字段列表
     *
     * @param databaseId
     * @param tableId
     * @return
     */
    @Override
    public List<MappingColumnVO> getMappingColumnsNotAdd(long databaseId, long tableId) {
        // 可以加入搜索引擎中的所有字段（包含已增加的和未增加的）
        var columnsAll = this.getInitMappingColumns(databaseId, tableId);
        Set<String> columnsAdd = mappingColumnRepo.findByDictTableId(tableId).stream().map(MappingColumn::getEnColumn).collect(Collectors.toSet());
        return columnsAll.stream().filter(col -> !columnsAdd.contains(col.getEnColumn())).collect(Collectors.toList());
    }


    private ColDisplayedDTO convertToColDisplayedDTO(MappingColumn mappingColumn) {
        ColDisplayedDTO result = new ColDisplayedDTO();
        result.setBoost(mappingColumn.getBoost());
        result.setDictColumnId(mappingColumn.getDictColumnId());
        result.setEsColumn(mappingColumn.getEsColumn());
        result.setSearched(mappingColumn.isSearched());
        return result;
    }

    private MappingColumnVO convertToMappingColumnVO(MappingColumn mappingColumn) {
        return BeanUtil.toBean(mappingColumn, MappingColumnVO.class);
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

    private String getColumnType(MappingColumnDTO column) {
        boolean searched = column.isSearched();
        boolean analyzed = column.isAnalyzed();
        boolean displayed = column.isDisplayed();

        if (!searched || !displayed) {
            return EsColumnTypeEnum.NOT_SEARCH.toString();
        } else if (!analyzed) {
            return KEYWORD.toString();
        } else {
            return TEXT.toString();
        }

    }

}
