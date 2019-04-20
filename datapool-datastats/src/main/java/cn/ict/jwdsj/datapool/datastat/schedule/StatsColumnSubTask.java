package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsColumnSubTask {

    @Autowired
    private StatsColumnService statsColumnService;
    @Autowired
    private StatsTableService statsTableService;

    public void updateStatsColumns() {

        // 删去字典中不存在的字段
        statsColumnService.deleteColumnsNotExist();

        // 添加字典中存在但是统计模块不存在的字段
        statsColumnService.saveColumnsNotAdd();

        // 更新缺陷字段
        statsColumnService.updateDefectedColumns();
    }
}
