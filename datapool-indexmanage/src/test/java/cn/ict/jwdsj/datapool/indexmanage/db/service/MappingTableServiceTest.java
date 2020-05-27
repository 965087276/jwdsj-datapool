package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MappingTableServiceTest {
    @Autowired
    private ApplicationContext publisher;

    @Test
    public void updateDateTest() throws InterruptedException {
        for (int i = 0; i < 2000; i++) {
            TableSyncMsg msg = new TableSyncMsg();
            msg.setDatabaseId(2);
            msg.setEnDatabase("information_schema");
            msg.setTableId(3);
            msg.setEnTable("COLUMNS");
            msg.setIndexId(1);
            msg.setIndexName("jwdsj-datapool-index-index1");
            publisher.publishEvent(msg);
        }
        Thread.sleep(1000 * 400);
    }
}
