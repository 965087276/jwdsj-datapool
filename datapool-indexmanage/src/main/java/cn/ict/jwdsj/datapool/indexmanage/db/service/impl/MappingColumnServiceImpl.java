package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.api.feign.StatsClient;
import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColumnTypeDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingColumnDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingColumnRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.SeTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.constant.EsColumnTypeEnum;
import com.github.dozermapper.core.Mapper;
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
    @Autowired private SeTableRepo seTableRepo;
    @Autowired private Mapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(SeTableAddDTO seTableAddDTO) {
        Map<String, List<MappingColumnDTO>> columnsGroupByType = seTableAddDTO.getColumns()
                .stream()
                .collect(groupingBy(this::getColumnType));

        // 若该表已加入了数据同步，那么不能增加新字段
        Optional.ofNullable(seTableRepo.findByTableId(seTableAddDTO.getTableId()))
                .ifPresent(seTable -> {
                    Assert.isTrue(!seTable.isSync(), "该表在数据同步任务中，不能添加新字段");
                });

        // 字段的类型及其数量
        Map<String, Integer> typeAndCount = this.groupWithTypeAndCountByTableId(seTableAddDTO.getTableId());

        List<MappingColumn> mappingColumns = new ArrayList<>();

        columnsGroupByType.forEach((type, list) -> {
            // 数据库中已有的typeEnum字段类型的数量
            int typeCount = Optional.ofNullable(typeAndCount.get(type))
                    .orElse(new Integer(0))
                    .intValue();

            for (int i = 0; i < list.size(); ++i) {
                MappingColumnDTO mappingColumnDTO = list.get(i);
                MappingColumn mappingColumn = BeanUtil.toBean(mappingColumnDTO, MappingColumn.class);
                mappingColumn.setTableId(seTableAddDTO.getTableId());
                mappingColumn.setEsColumn(type + "-" + (++typeCount));
                mappingColumn.setType(type);

                mappingColumns.add(mappingColumn);
            }
        });

        mappingColumnRepo.saveAll(mappingColumns);

    }

    @Override
    public MappingColumn findByColumnId(long columnId) {
        return mappingColumnRepo.findByColumnId(columnId);
    }

    @Override
    public List<ColumnTypeDTO> listColumnTypeDTOByTableId(long tableId) {
        return mappingColumnRepo.findByTableId(tableId)
                .stream()
                .map(obj -> mapper.map(obj, ColumnTypeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 将某表的字段按照类型和数目进行group by
     *
     * @param tableId
     * @return
     */
    private Map<String, Integer> groupWithTypeAndCountByTableId(long tableId) {
        QMappingColumn mappingColumn = QMappingColumn.mappingColumn;
        return jpaQueryFactory
                .select(mappingColumn.type, mappingColumn.count())
                .from(mappingColumn)
                .where(mappingColumn.tableId.eq(tableId))
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
        List<ColumnTypeDTO> columnTypeDTOS = listColumnTypeDTOByTableId(tableId);

        // 表字段id与表字段名的映射
        Map<Long, String> colIdAndColNameMap = dictColumns
                .stream().collect(toMap(DictColumn::getId, dictColumn -> dictColumn.getEnColumn()));
        // 表字段id与索引字段名的映射
        Map<Long, String> colIdAndEsColMap = columnTypeDTOS
                .stream().collect(toMap(ColumnTypeDTO::getColumnId, columnTypeDTO -> columnTypeDTO.getEsColumn()));

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
    public List<MappingColumn> listMappingColumnByTableId(long tableId) {
        return mappingColumnRepo.findByTableIdAndDisplayed(tableId, true);
    }

    /**
     * 表信息管理--查看字段
     *
     * @param tableId
     * @return
     */
    @Override
    public List<MappingColumnVO> listMappingColumnVOs(long tableId) {
        return mappingColumnRepo.findByTableId(tableId)
                .stream()
                .map(this::convertToMappingColumnVO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableId(long tableId) {
        mappingColumnRepo.deleteByTableId(tableId);
    }

    /**
     * 更新字段（未配置数据同步，可任意增删改字段）
     * 前台传输所有字段
     * @param seTableAddDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateColumnsNotSync(SeTableAddDTO seTableAddDTO) {
        long tableId = seTableAddDTO.getTableId();
        mappingColumnRepo.deleteByTableId(tableId);
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
        var columns = seTableAddDTO.getColumns();
        List<MappingColumn> mappingColumns = new ArrayList<>();
        for (var column : columns) {
            MappingColumn colUpd = mappingColumnRepo.findByColumnId(column.getColumnId());
            colUpd.setDisplayed(column.isDisplayed());
            colUpd.setBoost(column.getBoost());
            mappingColumns.add(colUpd);
        }
        mappingColumnRepo.saveAll(mappingColumns);
    }

    /**
     * 插入/更新单条数据
     *
     * @param mappingColumn
     */
    @Override
    public void save(MappingColumn mappingColumn) {
        mappingColumnRepo.save(mappingColumn);
    }

    private MappingColumnVO convertToMappingColumnVO(MappingColumn mappingColumn) {
        return mapper.map(mappingColumn, MappingColumnVO.class);
    }

    private MappingColumnVO convertToMappingColumnVO(DictColumn dictColumn) {
        return mapper.map(dictColumn, MappingColumnVO.class);
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
