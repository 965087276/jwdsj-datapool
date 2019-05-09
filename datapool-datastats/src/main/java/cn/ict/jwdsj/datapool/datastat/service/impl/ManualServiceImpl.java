package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.datastat.schedule.StatsScheduleTask;
import cn.ict.jwdsj.datapool.datastat.service.ManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ManualServiceImpl implements ManualService {
    @Autowired
    private StatsScheduleTask scheduleTask;

    /**
     * 手动更新
     */
    @Override
    @Async
    public void manualSync() {
        scheduleTask.updateTableAndDatabaseCounts();
    }
}
