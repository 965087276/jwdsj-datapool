package cn.ict.jwdsj.datapool.datasync.fullread.controller;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.listener.KafkaTableSyncTaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSyncController {

    @Autowired
    private KafkaTableSyncTaskListener tableSyncTaskProducer;

    @PostMapping("datasync/sync")
    void syncTable(@RequestBody TableSyncMsg msg) {
        tableSyncTaskProducer.sendSyncMsg(msg);
    }
}
