package cn.ict.jwdsj.datapool.datastats.schedule;

import cn.ict.jwdsj.datapool.datastats.service.StatsColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsColumnSubTask {

    @Autowired
    private StatsColumnService statsColumnService;

    public void updateStatsColumns() {

        // 更新缺陷字段
        statsColumnService.updateDefectedColumns();
    }
}
