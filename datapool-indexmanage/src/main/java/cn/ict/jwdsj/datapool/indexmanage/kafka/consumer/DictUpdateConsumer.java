package cn.ict.jwdsj.datapool.indexmanage.kafka.consumer;

import cn.ict.jwdsj.datapool.api.feign.DictClient;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.SeTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg.DictUpdateType;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DictUpdateConsumer {

    @Autowired
    private SeTableService seTableService;
    @Autowired
    private MappingTableService mappingTableService;
    @Autowired
    private MappingColumnService mappingColumnService;
    @Autowired
    private DictClient dictClient;

    @KafkaListener(topics = "#{'${kafka.topic-name.dict-update}'}", groupId = "dict-update-indexmanage")
    public void update(ConsumerRecord<String, String> record) {
        Optional.ofNullable(record.value()).ifPresent(v -> {
            DictUpdateMsg msg = JSONObject.parseObject(v, DictUpdateMsg.class);
            switch (msg.getType()) {

                case TABLE:
                    DictTable dictTable = dictClient.findDictTableById(msg.getObjectId());
                    SeTable seTable = seTableService.findByDictTableId(msg.getObjectId());
                    MappingTable mappingTable = mappingTableService.findByDictTableId(msg.getObjectId());

                    seTable.setEnTable(dictTable.getEnTable());
                    seTable.setChTable(dictTable.getChTable());
                    seTableService.save(seTable);

                    mappingTable.setEnTable(dictTable.getEnTable());
                    mappingTable.setChTable(dictTable.getChTable());
                    mappingTableService.save(mappingTable);

                    break;

                case COLUMN:
                    DictColumn dictColumn = dictClient.findDictColumnById(msg.getObjectId());
                    MappingColumn mappingColumn = mappingColumnService.findByDictColumnId(msg.getObjectId());
                    mappingColumn.setEnColumn(dictColumn.getEnColumn());
                    mappingColumn.setChColumn(dictColumn.getChColumn());
                    mappingColumnService.save(mappingColumn);
                    break;

                case DATABASE:
                    break;
            }
        });

    }
}
