package cn.ict.jwdsj.datapool.datastats.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatsScheduleTask {

    @Autowired
    private StatsDatabaseSubTask databaseSubTask;
    @Autowired
    private StatsTableSubTask tableSubTask;
    @Autowired
    private StatsColumnSubTask columnSubTask;

    @Scheduled(initialDelay = 1000, fixedRate = 43200000)
    public void updateTableAndDatabaseCounts() {
        columnSubTask.updateStatsColumns();
        tableSubTask.updateStatTables();
        databaseSubTask.updateStatDatabases();
    }
}
