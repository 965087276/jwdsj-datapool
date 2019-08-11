package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StatsDatabaseSubTask {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsTableService statsTableService;

    public void updateStatDatabases() {

        List<StatsDatabase> databases = statsDatabaseService.listAll();

        databases.parallelStream().forEach(statDatabase -> {
            long countRecords = statsTableService.countDatabaseRecords(statDatabase.getDatabaseId());
            int  countTables  = statsTableService.countTablesByDatabaseId(statDatabase.getDatabaseId());
            LocalDate updateDate = statsTableService.getDatabaseUpdateDate(statDatabase.getDatabaseId());
            if (countRecords != statDatabase.getTotalRecords()
                    || countTables != statDatabase.getTotalTables()
                    || updateDate != statDatabase.getUpdateDate()) {
                statDatabase.setTotalRecords(countRecords);
                statDatabase.setTotalTables(countTables);
                statDatabase.setUpdateDate(updateDate);
                statsDatabaseService.save(statDatabase);
            }
        });
    }
}
