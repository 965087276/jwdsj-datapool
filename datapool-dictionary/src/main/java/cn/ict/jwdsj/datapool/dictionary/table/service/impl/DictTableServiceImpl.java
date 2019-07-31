package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.QDictTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.UpdateTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import cn.ict.jwdsj.datapool.dictionary.table.mapper.DictTableMapper;
import cn.ict.jwdsj.datapool.dictionary.table.repo.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.constant.DictType.TABLE;


@Service
public class DictTableServiceImpl implements DictTableService {
    @Autowired
    private DictTableRepo dictTableRepo;
    @Autowired
    private DictTableMapper dictTableMapper;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private DictColumnService dictColumnService;
    @Autowired
    private KafkaSender kafkaSender;
    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    @Override
    public void save(DictTable dictTable) {
        dictTableRepo.save(dictTable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<DictTable> dictTables) {
        dictTableRepo.saveAll(dictTables);
    }

    @Override
    public List<String> listEnTablesByDictDatabase(DictDatabase dictDatabase) {
        QDictTable dictTable = QDictTable.dictTable;

        return jpaQueryFactory
                .select(dictTable.enTable)
                .from(dictTable)
                .where(dictTable.dictDatabase.eq(dictDatabase))
                .fetch();
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

    @Override
    public List<TbIdNameDTO> listTbIdNameDTOByDictDatabase(DictDatabase dictDatabase) {
        QDictTable dictTable = QDictTable.dictTable;
        return jpaQueryFactory
                .select(dictTable.id, dictTable.enTable)
                .from(dictTable)
                .where(dictTable.dictDatabase.eq(dictDatabase))
                .fetch()
                .stream()
                .map(tuple -> TbIdNameDTO.builder()
                        .id(tuple.get(dictTable.id))
                        .enTable(tuple.get(dictTable.enTable))
                        .build()
                )
                .collect(Collectors.toList());


    }

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

        return dictTableRepo.findAll(predicate, pageable).map(dictTable1 -> this.convertToDictTableVO(dictDatabase, dictTable1));

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

        dictTableRepo.saveAll(dictTables);
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
        dictColumnService.deleteByDictTableId(id);
        // 删除该表
        dictTableRepo.deleteById(id);
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

//    @Override
//    public List<DatabaseNameDTO> listDatabaseDropDownBox() {
//        QDictTable dictTable = QDictTable.dictTable;
//        List<Long> databaseIds = jpaQueryFactory
//                .select(dictTable.dictDatabase.id)
//                .from(dictTable)
//                .groupBy(dictTable.dictDatabase.id)
//                .fetch();
//        return dictDatabaseService.listDatabaseNameDTOByIds(databaseIds);
//    }

    private DictTableVO convertToDictTableVO(DictDatabase dictDatabase, DictTable dictTable) {
        DictTableVO dictTableVO = BeanUtil.toBean(dictTable, DictTableVO.class);
        dictTableVO.setTableId(dictTable.getId());
        dictTableVO.setChDatabase(dictDatabase.getChDatabase());
        dictTableVO.setEnDatabase(dictDatabase.getEnDatabase());
        return dictTableVO;
    }
    private TableNameDTO convertToTableNameDTO(DictTable dictTable) {
        TableNameDTO tableNameDTO = BeanUtil.toBean(dictTable, TableNameDTO.class);
        tableNameDTO.setTableId(dictTable.getId());
        return tableNameDTO;
    }
    private DictTable convertToDictTable(DictTableDTO dictTableDTO, DictDatabase dictDatabase) {
        DictTable dictTable = BeanUtil.toBean(dictTableDTO, DictTable.class);
        dictTable.setDictDatabase(dictDatabase);
        dictTable.setEnDatabase(dictDatabase.getEnDatabase());
        return dictTable;
    }
}
