package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MappingColumnServiceTest {

    @Autowired
    private MappingColumnService mappingColumnService;

    @Test
    public void getTableFullReadDTOByTableIdTest() {
        long tableId = 102L;
        TableFullReadDTO dto = mappingColumnService.getTableFullReadDTOByTableId(tableId);
        System.out.println(dto);
    }

}
