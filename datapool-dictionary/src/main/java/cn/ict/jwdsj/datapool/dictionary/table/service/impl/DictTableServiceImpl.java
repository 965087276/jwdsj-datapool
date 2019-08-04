package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.UpdateTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import cn.ict.jwdsj.datapool.dictionary.table.mapper.DictTableMapper;
import cn.ict.jwdsj.datapool.dictionary.table.repo.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.github.dozermapper.core.Mapper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.constant.DictType.TABLE;
import static cn.ict.jwdsj.datapool.common.constant.DictType.TABLES;


@Service
public class DictTableServiceImpl implements DictTableService {
    @Autowired
    private DictTableRepo dictTableRepo;
    @Autowired
    private DictTableMapper dictTableMapper;
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private DictColumnService dictColumnService;
    @Autowired
    private ApplicationContext publisher;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private Mapper mapper;
    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    @Override
    public void save(DictTable dictTable) {
        this.saveAllToDb(Arrays.asList(dictTable));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllToDb(List<DictTable> dictTables) {
        String currentTime = dictTableMapper.getCurrentTimeStamp();
        dictTableMapper.insertIgnore(dictTables);
        publisher.publishEvent(new DictAddEvent(currentTime, TABLES));
    }

    @Override
    public List<String> listEnTablesByDictDatabase(DictDatabase dictDatabase) {
        return dictTableMapper.listEnTableByDatabaseId(dictDatabase.getId());
    }

    /**
     * 所有表的id
     *
     * @return
     */
    @Override
    public List<Long> listTableId() {
        return dictTableMapper.listTableId();
    }

    @Override
    public DictTable findByEnDatabaseAndEnTable(String enDatabase, String enTable) {
        return dictTableRepo.findByEnDatabaseAndEnTable(enDatabase, enTable);
    }

//    @Override
//    public List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase) {
//        QDictTable dictTable = QDictTable.dictTable;
//        return jpaQueryFactory
//                .select(dictTable.id, dictTable.enTable)
//                .from(dictTable)
//                .where(dictTable.dictDatabase.eq(dictDatabase))
//                .fetch()
//                .stream()
//                .map(tuple -> TbIdNameDTO.builder()
//                        .id(tuple.get(dictTable.id))
//                        .enTable(tuple.get(dictTable.enTable))
//                        .build()
//                )
//                .collect(Collectors.toList());
//
//
//    }

    @Override
    public Page<DictTableVO> listVO(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
        QDictTable dictTable = QDictTable.dictTable;
        Predicate predicate = dictTable.dictDatabase.eq(dictDatabase);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, dictTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, dictTable.enTable.like('%' + nameLike + '%'));

        return dictTableRepo.findAll(predicate, pageable).map(dictTable1 -> this.convertToDictTableVO(dictTable1));

    }

    @Override
    public List<TableNameDTO> listTableNameDTOByIds(List<Long> ids) {
        return dictTableRepo.findByIdIn(ids)
                .stream()
                .map(this::convertToTableNameDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(DictTableMultiAddDTO dictTableMultiAddDTO) {
        DictDatabase dictDatabase = dictDatabaseService.findById(dictTableMultiAddDTO.getDatabaseId());
        List<DictTableDTO> dictTableDTOS = dictTableMultiAddDTO.getDictTables();
        // 不能有重复元素
        Assert.isTrue(dictTableDTOS.stream().distinct().count() == dictTableDTOS.size(), "有重复元素");

        List<DictTable> dictTables = dictTableDTOS
                .stream()
                .map(dictTableDTO -> this.convertToDictTable(dictTableDTO, dictDatabase))
                .collect(Collectors.toList());

        this.saveAllToDb(dictTables);
    }

    @Override
    public DictTable findById(long id) {
        return dictTableRepo.findById(id).get();
    }

    /**
     * 表下拉框
     *
     * @param databaseId
     * @return
     */
    @Override
    public List<TableNameDTO> listTableDropDownBox(long databaseId) {
        return dictTableRepo.findByDictDatabase(DictDatabase.buildById(databaseId))
                .stream()
                .map(this::convertToTableNameDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(UpdateTableDTO updateTableDTO) {
        DictTable dictTable = dictTableRepo.getOne(updateTableDTO.getTableId());
        dictTable.setChTable(updateTableDTO.getChTable());
        dictTableRepo.save(dictTable);
        kafkaSender.send(kafkaUpdateTopic, new DictUpdateMsg(TABLE, updateTableDTO.getTableId()));
    }

    /**
     * 通过id删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        DictTable dictTable = dictTableRepo.getOne(id);
        // 若该表已加入搜索引擎，则不能删除
        Assert.isTrue(!dictTable.isAddToSe(), "该表已加入搜索引擎表中，不能被删除");
        // 删除所有字段
        dictColumnService.deleteByTableId(id);
        // 删除该表
        dictTableRepo.deleteById(id);
        publisher.publishEvent(new DictDeleteEvent(id, TABLE));
    }

    /**
     * 判断某个库下是否有表
     *
     * @param dictDatabase
     * @return
     */
    @Override
    public boolean existsByDictDatabase(DictDatabase dictDatabase) {
        return dictTableRepo.existsByDictDatabase(dictDatabase);
    }


    private DictTableVO convertToDictTableVO(DictTable dictTable) {
        return mapper.map(dictTable, DictTableVO.class);
    }

    private TableNameDTO convertToTableNameDTO(DictTable dictTable) {
        return mapper.map(dictTable, TableNameDTO.class);
    }

    private DictTable convertToDictTable(DictTableDTO dictTableDTO, DictDatabase dictDatabase) {
        DictTable dictTable = mapper.map(dictTableDTO, DictTable.class);
        dictTable.setDictDatabase(dictDatabase);
        dictTable.setEnDatabase(dictDatabase.getEnDatabase());
        return dictTable;
    }
}
