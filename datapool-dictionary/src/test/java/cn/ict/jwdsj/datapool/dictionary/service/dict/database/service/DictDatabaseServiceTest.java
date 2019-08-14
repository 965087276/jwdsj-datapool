package cn.ict.jwdsj.datapool.dictionary.service.dict.database.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.service.database.DictDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictDatabaseServiceTest {
    @Autowired
    private DictDatabaseService dictDatabaseService;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void saveTest() {
        DictDatabase dictDatabase = new DictDatabase();
        dictDatabase.setEnDatabase("db1");
        dictDatabase.setChDatabase("测试库1");
        dictDatabase.setDetail("这是测试库1");
        dictDatabaseService.save(dictDatabase);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void saveAllTest() {
        List<DictDatabase> dictDatabases = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            DictDatabase dictDatabase = new DictDatabase();
            dictDatabase.setEnDatabase("db" + i);
            dictDatabase.setChDatabase("测试库" + i);
            dictDatabase.setDetail("这是测试库" + i);
            dictDatabases.add(dictDatabase);
        }
        dictDatabaseService.saveAllToDb(dictDatabases);
    }


}
