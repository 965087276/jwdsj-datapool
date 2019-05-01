package cn.ict.jwdsj.datapool.datastat.kafka.consumer;

import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsColumn;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class DictUpdateConsumer {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsColumnService statsColumnService;
    @Autowired
    private DictClient dictClient;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @KafkaListener(topics = "#{'${kafka.topic-name.dict-update}'}", groupId = "dict-update-datastats")
    @Transactional
    public void update(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional.ofNullable(record.value()).ifPresent(v -> {
            DictUpdateMsg msg = JSONObject.parseObject(v, DictUpdateMsg.class);
            switch (msg.getType()) {
                case DATABASE:
                    DictDatabase dictDatabase = dictClient.findDictDatabaseById(msg.getObjectId());
                    QStatsDatabase statsDatabase = QStatsDatabase.statsDatabase;
                    jpaQueryFactory.update(statsDatabase)
                            .set(statsDatabase.enDatabase, dictDatabase.getEnDatabase())
                            .set(statsDatabase.chDatabase, dictDatabase.getChDatabase())
                            .where(statsDatabase.dictDatabaseId.eq(dictDatabase.getId()))
                            .execute();
                    break;

                case TABLE:
                    DictTable dictTable = dictClient.findDictTableById(msg.getObjectId());
                    QStatsTable statsTable = QStatsTable.statsTable;
                    jpaQueryFactory.update(statsTable)
                            .set(statsTable.enTable, dictTable.getEnTable())
                            .set(statsTable.chTable, dictTable.getChTable())
                            .where(statsTable.dictTableId.eq(dictTable.getId()))
                            .execute();
                    break;

                case COLUMN:
                    DictColumn dictColumn = dictClient.findDictColumnById(msg.getObjectId());
                    QStatsColumn statsColumn = QStatsColumn.statsColumn;
                    jpaQueryFactory.update(statsColumn)
                            .set(statsColumn.enColumn, dictColumn.getEnColumn())
                            .set(statsColumn.chColumn, dictColumn.getChColumn())
                            .where(statsColumn.dictColumnId.eq(dictColumn.getId()))
                            .execute();
                    break;
            }
            ack.acknowledge();
        });
    }
}
