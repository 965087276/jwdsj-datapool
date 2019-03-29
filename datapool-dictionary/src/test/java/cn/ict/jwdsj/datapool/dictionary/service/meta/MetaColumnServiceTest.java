package cn.ict.jwdsj.datapool.dictionary.service.meta;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaColumn;
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
