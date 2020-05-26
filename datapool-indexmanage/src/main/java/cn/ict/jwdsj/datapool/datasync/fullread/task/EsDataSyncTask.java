package cn.ict.jwdsj.datapool.datasync.fullread.task;

import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.EsIndexRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsColumnService;
import cn.ict.jwdsj.datapool.indexmanage.elastic.service.ElasticRestService;
import com.alibaba.fastjson.JSON;
import com.github.dozermapper.core.Mapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author wangjinhao
 * @date 2020/5/25 21:32
 */
@Component
@Slf4j
public class EsDataSyncTask {

    @Autowired
    private MappingTableRepo mappingTableRepo;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private ElasticRestService elasticRestService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private ApplicationContext publisher;

    /**
     * 定时任务
     * 更新表记录数和索引记录数
     * 并将需要更新数据的表发送给datasync模块
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateEsData() {
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
            if (daysDiff >= mtb.getUpdatePeriod()) {
                // 先删除，再增加
                elasticRestService.deleteDocsByTableId(mtb.getIndexName(), tableId);
                while (true) {
                    try {
                        if (elasticRestService.getRecordsByTableIdInIndex(mtb.getIndexName(), tableId) == 0L) break;
                        Thread.sleep(20000);
                    } catch (Exception e) {
                        break;
                    }
                }
                TableSyncMsg msg = mapper.map(mtb, TableSyncMsg.class);
                publisher.publishEvent(msg);
                isSync = true;
            }
            // 更新 索引记录数 表记录数
            if (newIndexRecords != oldIndexRecords || newTableRecords != oldTableRecords)
                mappingTableRepo.updateRecords(tableId, newIndexRecords, isSync ? newTableRecords : oldTableRecords);

        });
    }

}
