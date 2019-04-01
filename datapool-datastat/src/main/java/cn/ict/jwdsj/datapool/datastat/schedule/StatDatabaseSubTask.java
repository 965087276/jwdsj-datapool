package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.datastat.entity.StatDatabase;
import cn.ict.jwdsj.datapool.datastat.service.StatDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatDatabaseSubTask {

    @Autowired
    private StatDatabaseService statDatabaseService;
    @Autowired
    private StatService statService;

    public void updateStatDatabases() {
        statDatabaseService.deleteDatabasesNotExist();
        statDatabaseService.saveDatabasesNotAdd();

        List<StatDatabase> databases = statDatabaseService.listAll();

        databases.parallelStream().forEach(statDatabase -> {
            long countRecords = statService.countDatabaseRecords(statDatabase.getDictDatabase());
            int  countTables  = statService.countTablesInDatabase(statDatabase.getDictDatabase());
            if (countRecords != statDatabase.getTotalRecords() || countTables != statDatabase.getTotalTables()) {
                statDatabase.setTotalRecords(countRecords);
                statDatabase.setTotalTables(countTables);
                statDatabase.setUpdateDate(new Date());
                statDatabaseService.save(statDatabase);
            }
        });
    }
}
