package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.UpdateColumnDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg.DictUpdateType.COLUMN;
import static java.util.stream.Collectors.groupingBy;

@Service
public class DictColumnServiceImpl implements DictColumnService {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictColumnRepo dictColumnRepo;
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private KafkaSender kafkaSender;
    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    public List<String> getEnTableByDictDatabaseId(long dictDatabaseId) {
        return this.listTableNameDTOByDatabaseId(dictDatabaseId)
                .stream()
                .map(TableNameDTO::getEnTable)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<DictColumn> dictColumns) {
        dictColumnRepo.saveAll(dictColumns);
    }

    @Override
    public List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId) {
        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
        DictTable dictTable = dictTableService.findById(tableId);

        return dictColumnRepo.findByDictTableId(tableId)
                .stream()
                .map(dictColumn -> this.convertToDictColumnVO(dictDatabase, dictTable, dictColumn))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(DictColumnMultiAddDTO dictColumnMultiAddDTO) {
//        DictDatabase dictDatabase = dictDatabaseService.findById(dictColumnMultiAddDTO.getDatabaseId());
//        DictTable dictTable = dictTableService.findById(dictColumnMultiAddDTO.getTableId());
//        // 判断库id、表id的合法性
//        Assert.isTrue(!BeanUtil.isEmpty(dictDatabase), "数据库不存在");
//        Assert.isTrue(!BeanUtil.isEmpty(dictTable), "数据表不存在");
        long databaseId = dictColumnMultiAddDTO.getDatabaseId();
        long tableId = dictColumnMultiAddDTO.getTableId();

        // 判断是否有重复元素
        Assert.isTrue(dictColumnMultiAddDTO.getDictColumnAddDTOS().size() == dictColumnMultiAddDTO.getDictColumnAddDTOS().stream().distinct().count(), "有重复元素");
        List<DictColumn> dictColumns = dictColumnMultiAddDTO.getDictColumnAddDTOS()
                .stream()
                .map(dictColumnAddDTO -> this.convertToDictColumn(databaseId, tableId, dictColumnAddDTO))
                .collect(Collectors.toList());
        dictColumnRepo.saveAll(dictColumns);
    }

    @Override
    public List<TableNameDTO> listTableNameDTOByDatabaseId(long databaseId) {
        QDictColumn dictColumn = QDictColumn.dictColumn;
        List<Long> tableIds = jpaQueryFactory
                .select(dictColumn.dictTableId)
                .from(dictColumn)
                .where(dictColumn.dictDatabaseId.eq(databaseId))
                .groupBy(dictColumn.dictTableId)
                .fetch();
        return dictTableService.listTableNameDTOByIds(tableIds);
    }

//    @Override
//    public List<DatabaseNameDTO> listDatabaseDropDownBox() {
//        QDictColumn dictColumn = QDictColumn.dictColumn;
//        List<Long> databaseIds = jpaQueryFactory
//                .select(dictColumn.dictDatabase.id)
//                .from(dictColumn)
//                .groupBy(dictColumn.dictDatabase.id)
//                .fetch();
//        return dictDatabaseService.listDatabaseNameDTOByIds(databaseIds);
//    }

    @Override
    public List<DictColumn> listByDictTableId(long dictTableId) {
        return dictColumnRepo.findByDictTableId(dictTableId);
    }

    @Override
    public List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId) {
        QDictColumn dictColumn = QDictColumn.dictColumn;
        return jpaQueryFactory.select(dictColumn.id, dictColumn.enColumn, dictColumn.chColumn)
                .from(dictColumn)
                .where(dictColumn.dictTableId.eq(tableId))
                .fetch()
                .stream()
                .map(tuple -> ColumnNameDTO.builder()
                        .columnId(tuple.get(dictColumn.id))
                        .enColumn(tuple.get(dictColumn.enColumn))
                        .chColumn(tuple.get(dictColumn.chColumn))
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(UpdateColumnDTO updateColumnDTO) {
        DictColumn dictColumn = dictColumnRepo.getOne(updateColumnDTO.getColumnId());
        dictColumn.setChColumn(updateColumnDTO.getChColumn());
        dictColumnRepo.save(dictColumn);
        kafkaSender.send(kafkaUpdateTopic, new DictUpdateMsg(COLUMN, updateColumnDTO.getColumnId()));
    }

    @Override
    public DictColumn findById(long id) {
        return dictColumnRepo.getOne(id);
    }

    private DictColumnVO convertToDictColumnVO(DictDatabase dictDatabase, DictTable dictTable, DictColumn dictColumn) {
        DictColumnVO dictColumnVO = BeanUtil.toBean(dictColumn, DictColumnVO.class);
        dictColumnVO.setColumnId(dictColumn.getId());
        dictColumnVO.setEnDatabase(dictDatabase.getEnDatabase());
        dictColumnVO.setChDatabase(dictDatabase.getChDatabase());
        dictColumnVO.setEnTable(dictTable.getEnTable());
        dictColumnVO.setChTable(dictTable.getChTable());
        return dictColumnVO;
    }

    private DictColumn convertToDictColumn(long databaseId, long tableId, DictColumnAddDTO dictColumnAddDTO) {
        DictColumn dictColumn = BeanUtil.toBean(dictColumnAddDTO, DictColumn.class);
        dictColumn.setDictTableId(tableId);
        dictColumn.setDictDatabaseId(databaseId);
        return dictColumn;
    }
}
