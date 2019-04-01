package cn.ict.jwdsj.datapool.dictionary.service.dict.database.service;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictDatabaseServiceTest {
    @Autowired
    private DictDatabaseService dictDatabaseService;

    @Test
    @Transactional
    public void saveTest() {
        DictDatabase dictDatabase = new DictDatabase();
        dictDatabase.setEnDatabase("db1");
        dictDatabase.setChDatabase("测试库1");
        dictDatabase.setDetail("这是测试库1");
        dictDatabaseService.save(dictDatabase);
    }

    @Test
    @Transactional
    public void saveAllTest() {
        List<DictDatabase> dictDatabases = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            DictDatabase dictDatabase = new DictDatabase();
            dictDatabase.setEnDatabase("db" + i);
            dictDatabase.setChDatabase("测试库" + i);
            dictDatabase.setDetail("这是测试库" + i);
            dictDatabases.add(dictDatabase);
        }
        dictDatabaseService.saveAll(dictDatabases);
    }


}
