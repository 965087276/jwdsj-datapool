package cn.ict.jwdsj.datapool.indexmanage.db.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QMappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.QSeTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableUpdateDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingTableVO;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.alibaba.fastjson.JSON;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.Mapping;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MappingTableServiceImpl implements MappingTableService {
    @Autowired
    private MappingTableRepo mappingTableRepo;
    @Autowired
    private EsColumnService esColumnService;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private ElasticRestService elasticRestService;
    @Autowired
    private EsIndexRepo esIndexRepo;
    @Autowired
    private DictClient dictClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private Mapper mapper;
    @Value("${kafka.topic-name.table-sync-task}")
    private String syncTableTaskTopic;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MappingTableAddDTO mappingTableAddDTO) throws IOException {

        long tableId = mappingTableAddDTO.getTableId();
        long databaseId = mappingTableAddDTO.getDatabaseId();
        long indexId = mappingTableAddDTO.getIndexId();

        EsIndex esIndex = esIndexRepo.findById(indexId);

        // 若索引中存在该表的数据，则说明之前的delete by query还没有执行完毕，因此拒绝这次增加请求
        long totalDocs = elasticRestService.getRecordsByTableIdInIndex(esIndex.getIndexName(), tableId);
        if (totalDocs != 0) {
            elasticRestService.deleteDocsByTableId(esIndex.getIndexName(), tableId);
        }
        Assert.isTrue(totalDocs == 0L, "数据正在删除中，请稍后尝试加入");

        DictTable dictTable = dictClient.findDictTableById(tableId);
        DictDatabase dictDatabase = dictClient.findDictDatabaseById(databaseId);

        MappingTable mappingTable = new MappingTable();
        mappingTable.setUpdatePeriod(mappingTableAddDTO.getUpdatePeriod());
        mappingTable.setEsIndex(esIndex);
        mappingTable.setDictTable(dictTable);
        mappingTable.setDictDatabase(dictDatabase);


        mappingTableRepo.save(mappingTable);
        // 为该索引添加一个别名，别名为"{alias-prefix}-表id"，别名指向这个表（即filter出elastic_table_id为该表id的文档）
        elasticRestService.addAlias(esIndex.getIndexName(), tableId);
        // 因为这个表要加入到es中，所以要根据表字段的搜索、分词情况来给elasticsearch的索引添加字段
        esColumnService.add(mappingTableAddDTO);

        // 更新se_table表的is_sync字段为true
        QSeTable seTable = QSeTable.seTable;
        jpaQueryFactory.update(seTable).set(seTable.sync, true).where(seTable.tableId.eq(tableId)).execute();

    }

    @Override
    public List<MappingTable> listTableNeedToUpdate() {
        QMappingTable mappingTable = QMappingTable.mappingTable;
        BooleanBuilder builder = new BooleanBuilder();

        // current_date - update_date >= update_period
        builder.and(
                Expressions.dateOperation(
                        Integer.class,
                        Ops.SUB,
                        Expressions.currentDate(), mappingTable.updateDate).goe(mappingTable.updatePeriod)
        );
        return jpaQueryFactory.selectFrom(mappingTable).where(builder).fetch();
    }

    @Override
    public Page<MappingTableVO> listMappingTableVO(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        QMappingTable mappingTable = QMappingTable.mappingTable;
        Predicate predicate = mappingTable.databaseId.eq(databaseId);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, mappingTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, mappingTable.enTable.like('%' + nameLike + '%'));

        return mappingTableRepo.findAll(predicate, pageable).map(tb -> BeanUtil.toBean(tb, MappingTableVO.class));
    }

    /**
     * 定时任务
     * 更新表记录数和索引记录数
     * 并将需要更新数据的表发送给datasync模块
     */
    @Override
    @Scheduled(initialDelay = 10000, fixedRate = 86400000)
    public void updateEsData() {
        QMappingTable mappingTable = QMappingTable.mappingTable;
        QStatsTable statsTable = QStatsTable.statsTable;

        List<Long> tableIds = mappingTableRepo.listTableId();
        // 当前日期
        LocalDate currentDate = mappingTableRepo.getLocalDate();
        tableIds.stream().forEach(tableId -> {

            MappingTable mtb = mappingTableRepo.findByTableId(tableId);
            long oldTableRecords = mtb.getTableRecords();
            long newTableRecords = jpaQueryFactory.select(statsTable.totalRecords).from(statsTable).where(statsTable.tableId.eq(tableId)).fetchOne();
            long oldIndexRecords = mtb.getIndexRecords();
            long newIndexRecords = 0;

            // 该表的最后更新日期
            LocalDate updateDate = mtb.getUpdateDate();
            // 上面两个日期的日期差
            int daysDiff = (int) ChronoUnit.DAYS.between(updateDate, currentDate);
            try {
                newIndexRecords = elasticRestService.getRecordsByTableIdInAlias(tableId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean isSync = false;
            // 如果表的记录数发生了变化并且更新周期已经到了，则对该表进行数据全量更新
            if (oldTableRecords != newTableRecords && daysDiff >= mtb.getUpdatePeriod()) {
//                // 先删除，再增加
//                elasticRestService.deleteDocsByTableId(mtb.getIndexName(), tableId);
//                while (true) {
//                    try {
//                        if (elasticRestService.getRecordsByTableIdInIndex(mtb.getIndexName(), tableId) == 0L) break;
//                        Thread.sleep(20000);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                TableSyncMsg msg = mapper.map(mtb, TableSyncMsg.class);
                kafkaTemplate.send(syncTableTaskTopic, JSON.toJSONString(msg));
                log.info("the msg have sent to kafka, table is {}.{}", msg.getEnTable(), msg.getEnDatabase());
                mappingTableRepo.updateUpdateDate(tableId);
                isSync = true;
            }

            // 更新 索引记录数 表记录数
            if (newIndexRecords != oldIndexRecords || newTableRecords != oldTableRecords)
                mappingTableRepo.updateRecords(tableId, newIndexRecords, isSync ? newTableRecords : oldTableRecords);

        });
    }

    /**
     * 取消数据同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTableId(long tableId) throws IOException {
        String indexName = mappingTableRepo.findByTableId(tableId).getIndexName();

        mappingTableRepo.deleteByTableId(tableId);

        QSeTable seTable = QSeTable.seTable;
        // 更新seTable表的sync字段为false
        jpaQueryFactory.update(seTable).set(seTable.sync, false).where(seTable.tableId.eq(tableId)).execute();
        // 在elasticsearch删除该表的索引别名
        elasticRestService.deleteAliasByIndexNameAndTableId(indexName, tableId);
        // 在elasticsearch删除该表的数据
        elasticRestService.deleteDocsByTableId(indexName, tableId);

    }

    /**
     * 更新表（表的同步周期）
     *
     * @param mappingTableUpdateDTO
     */
    @Override
    public void update(MappingTableUpdateDTO mappingTableUpdateDTO) {
        MappingTable mappingTable = mappingTableRepo.findByTableId(mappingTableUpdateDTO.getTableId());
        // 发生变化时才去更新
        if (mappingTable.getUpdatePeriod() != mappingTableUpdateDTO.getUpdatePeriod()) {
            mappingTable.setUpdatePeriod(mappingTableUpdateDTO.getUpdatePeriod());
            mappingTableRepo.save(mappingTable);
        }
    }

    /**
     * 手动数据同步
     */
    @Override
    @Async
    public void syncData() {
        this.updateEsData();
    }
}
