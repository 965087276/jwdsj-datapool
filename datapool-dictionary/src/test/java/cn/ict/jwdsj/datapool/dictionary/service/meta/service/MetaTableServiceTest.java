package cn.ict.jwdsj.datapool.dictionary.service.meta.service;

import cn.ict.jwdsj.datapool.dictionary.meta.entity.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetaTableServiceTest {
    @Autowired
    private MetaTableService metaTableService;

    @Test
    public void listByDatabaseTest() {
        String database = "zkbh-test";
        List<MetaTable> metaTables = metaTableService.listByDatabase(database);
        metaTables.forEach(System.out::println);
    }
}
