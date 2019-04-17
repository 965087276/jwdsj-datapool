package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatsDatabaseSubTask {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsService statsService;

    public void updateStatDatabases() {
        statsDatabaseService.deleteDatabasesNotExist();
        statsDatabaseService.saveDatabasesNotAdd();

        List<StatsDatabase> databases = statsDatabaseService.listAll();

        databases.parallelStream().forEach(statDatabase -> {
            long countRecords = statsService.countDatabaseRecords(statDatabase.getDictDatabase());
            int  countTables  = statsService.countTablesInDatabase(statDatabase.getDictDatabase());
            Date updateDate = statsService.getDatabaseUpdateDate(statDatabase.getDictDatabase());
            if (countRecords != statDatabase.getTotalRecords()
                    || countTables != statDatabase.getTotalTables()
                    || updateDate != statDatabase.getUpdateDate()) {
                statDatabase.setTotalRecords(countRecords);
                statDatabase.setTotalTables(countTables);
                statDatabase.setUpdateDate(new Date());
                statsDatabaseService.save(statDatabase);
            }
        });
    }
}
