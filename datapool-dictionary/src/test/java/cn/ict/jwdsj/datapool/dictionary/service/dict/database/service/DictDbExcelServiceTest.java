package cn.ict.jwdsj.datapool.dictionary.service.dict.database.service;

import cn.ict.jwdsj.datapool.dictionary.database.service.DictDbExcelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DictDbExcelServiceTest {

    @Autowired
    private DictDbExcelService excelService;

    @Test
    public void saveByExcelTest() {
        String filePath = "C:\\Users\\wangjinhao\\Desktop\\data poo测试\\dict_database.xlsx";
        File file = new File(filePath);
        excelService.saveByExcel(file);
    }
}
