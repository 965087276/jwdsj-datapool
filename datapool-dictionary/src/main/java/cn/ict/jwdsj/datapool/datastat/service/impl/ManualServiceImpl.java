package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.ict.jwdsj.datapool.datastat.schedule.StatsColumnSubTask;
import cn.ict.jwdsj.datapool.datastat.schedule.StatsDatabaseSubTask;
import cn.ict.jwdsj.datapool.datastat.schedule.StatsTableSubTask;
import cn.ict.jwdsj.datapool.datastat.service.ManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ManualServiceImpl implements ManualService {
    @Autowired
    private StatsDatabaseSubTask databaseSubTask;
    @Autowired
    private StatsTableSubTask tableSubTask;
    @Autowired
    private StatsColumnSubTask columnSubTask;

    /**
     * 手动更新
     */
    @Override
    @Async
    public void manualSync() {
        columnSubTask.updateStatsColumns();
        tableSubTask.updateStatTables();
        databaseSubTask.updateStatDatabases();
    }

    /**
     * 手动库更新
     */
    @Override
    @Async
    public void manualSyncDatabase() {
        databaseSubTask.updateStatDatabases();
    }

    /**
     * 手动表更新
     */
    @Override
    @Async
    public void manualSyncTable() {
        tableSubTask.updateStatTables();

    }

    /**
     * 手动字段更新
     */
    @Override
    @Async
    public void manualSyncColumn() {
        columnSubTask.updateStatsColumns();
    }
}
