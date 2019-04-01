package cn.ict.jwdsj.datapool.datastat.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatScheduleTask {

    @Autowired
    private StatDatabaseSubTask databaseSubTask;
    @Autowired
    private StatTableSubTask tableSubTask;

    @Scheduled(initialDelay = 1000, fixedRate = 43200000)
    public void updateTableAndDatabaseCounts() {
        tableSubTask.updateStatTables();
        databaseSubTask.updateStatDatabases();
    }
}
