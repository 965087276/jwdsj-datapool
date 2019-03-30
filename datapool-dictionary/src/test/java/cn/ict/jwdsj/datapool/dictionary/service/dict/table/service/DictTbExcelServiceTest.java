package cn.ict.jwdsj.datapool.dictionary.service.dict.table.service;

import cn.ict.jwdsj.datapool.dictionary.table.service.DictTbExcelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictTbExcelServiceTest {
    @Autowired
    private DictTbExcelService dictTbExcelService;

    @Test
    public void saveByDatabaseAndExcelTest() {
        File file = new File("C:\\Users\\wangjinhao\\Desktop\\data poo测试\\dict_table.xlsx");
        String enDatabase = "zkbh-test";
        dictTbExcelService.saveAll(enDatabase, file);
    }

}
