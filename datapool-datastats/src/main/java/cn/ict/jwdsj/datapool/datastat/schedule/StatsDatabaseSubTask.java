package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class StatsDatabaseSubTask {

    @Autowired
    private StatsDatabaseService statsDatabaseService;
    @Autowired
    private StatsService statsService;

    public void updateStatDatabases() {
        // 删去字典中不存在的库
        statsDatabaseService.deleteDatabasesNotExist();
        // 添加字典中存在但是统计模块不存在的库
        statsDatabaseService.saveDatabasesNotAdd();

        List<StatsDatabase> databases = statsDatabaseService.listAll();

        databases.parallelStream().forEach(statDatabase -> {
            long countRecords = statsService.countDatabaseRecords(statDatabase.getDictDatabaseId());
            int  countTables  = statsService.countTablesInDatabase(statDatabase.getDictDatabaseId());
            LocalDate updateDate = statsService.getDatabaseUpdateDate(statDatabase.getDictDatabaseId());
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
