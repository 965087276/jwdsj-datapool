package cn.ict.jwdsj.datapool.indexmanage.db.repo;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MappingColumnRepoTest {

    @Autowired
    private MappingColumnRepo mappingColumnRepo;

    @Test
    public void listByDictTableTest() {
        DictTable dictTable = DictTable.builtById(19L);
        List<MappingColumn> columns = mappingColumnRepo.findByDictTable(dictTable);
        columns.forEach(System.out::println);
    }

}
