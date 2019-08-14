package cn.ict.jwdsj.datapool.dictionary.service.meta.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.service.meta.MetaColumnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetaColumnServiceTest {
    @Autowired
    private MetaColumnService metaColumnService;

    @Test
    public void listByDatabaseAndTableTest() {
        String database = "zkbh-test";
        String table = "dict_column";
        List<MetaColumn> metaColumns = metaColumnService.listByDatabaseAndTable(database, table);
        metaColumns.forEach(System.out::println);
    }
}
