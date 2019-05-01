package cn.ict.jwdsj.datapool.indexmanage.kafka.consumer;

import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.*;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg.DictUpdateType;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
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
    private DictClient dictClient;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @KafkaListener(topics = "#{'${kafka.topic-name.dict-update}'}", groupId = "dict-update-indexmanage")
    @Transactional
    public void update(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional.ofNullable(record.value()).ifPresent(v -> {

            DictUpdateMsg msg = JSONObject.parseObject(v, DictUpdateMsg.class);

            switch (msg.getType()) {

                case TABLE:
                    Optional.ofNullable(dictClient.findDictTableById(msg.getObjectId())).ifPresent(dictTable -> {
                        QSeTable seTable = QSeTable.seTable;
                        QMappingTable mappingTable = QMappingTable.mappingTable;
                        jpaQueryFactory.update(seTable)
                                .set(seTable.enTable, dictTable.getEnTable())
                                .set(seTable.chTable, dictTable.getChTable())
                                .where(seTable.dictTableId.eq(dictTable.getId()))
                                .execute();
                        jpaQueryFactory.update(mappingTable)
                                .set(mappingTable.enTable, dictTable.getEnTable())
                                .set(mappingTable.chTable, dictTable.getChTable())
                                .where(mappingTable.dictTableId.eq(dictTable.getId()))
                                .execute();
                    });
                    break;

                case COLUMN:
                    Optional.ofNullable(dictClient.findDictColumnById(msg.getObjectId())).ifPresent(dictColumn -> {
                        QMappingColumn mappingColumn = QMappingColumn.mappingColumn;
                        jpaQueryFactory.update(mappingColumn)
                                .set(mappingColumn.enColumn, dictColumn.getEnColumn())
                                .set(mappingColumn.chColumn, dictColumn.getChColumn())
                                .execute();
                    });
                    break;

                case DATABASE:
                    break;
            }

            ack.acknowledge();
        });

    }
}
