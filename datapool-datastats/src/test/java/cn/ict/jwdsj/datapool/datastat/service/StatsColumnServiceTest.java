package cn.ict.jwdsj.datapool.datastat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatsColumnServiceTest {
    @Autowired
    private StatsColumnService statsColumnService;

    @Test
    public void initAndListDefectedColumnsTest() {
        long tableId = 19;
        statsColumnService.initAndListDefectedColumns(tableId);

        List<String> defects = statsColumnService.initAndListDefectedColumns(tableId);

        System.out.println(defects);

    }

}
