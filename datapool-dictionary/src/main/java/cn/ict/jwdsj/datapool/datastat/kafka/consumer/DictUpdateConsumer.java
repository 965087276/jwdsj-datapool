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
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
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
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private DictColumnService dictColumnService;


    @KafkaListener(topics = "#{'${kafka.topic-name.dict-update}'}", groupId = "dict-update-datastats")
    public void update(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional.ofNullable(record.value()).ifPresent(v -> {
            DictUpdateMsg msg = JSONObject.parseObject(v, DictUpdateMsg.class);
            switch (msg.getType()) {
                case DATABASE:
                    DictDatabase dictDatabase = dictDatabaseService.findById(msg.getObjectId());
                    statsDatabaseService.updateDatabaseInfo(msg.getObjectId(), dictDatabase.getEnDatabase(), dictDatabase.getChDatabase());
                    break;

                case TABLE:
                    DictTable dictTable = dictTableService.findById(msg.getObjectId());
                    statsTableService.updateTableInfo(msg.getObjectId(), dictTable.getEnTable(), dictTable.getChTable());
                    break;

                case COLUMN:
                   DictColumn dictColumn = dictColumnService.findById(msg.getObjectId());
                   statsColumnService.updateColumnInfo(msg.getObjectId(), dictColumn.getEnColumn(), dictColumn.getChColumn());
                    break;
            }
            ack.acknowledge();
        });
    }
}
