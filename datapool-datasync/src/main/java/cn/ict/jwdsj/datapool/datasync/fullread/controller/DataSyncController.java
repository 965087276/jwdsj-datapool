package cn.ict.jwdsj.datapool.datasync.fullread.controller;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.kafka.KafkaTableSyncTaskProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataSyncController {

    @Autowired
    private KafkaTableSyncTaskProducer tableSyncTaskProducer;

    @PostMapping("datasync/sync")
    void syncTable(@RequestBody TableSyncMsg msg) {
        tableSyncTaskProducer.sendSyncMsg(msg);
    }
}
