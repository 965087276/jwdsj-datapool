package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class StatsTableSubTask {

    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsService statsService;

    public void updateStatTables() {

        statsTableService.deleteTablesNotExist();
        statsTableService.saveTablesNotAdd();

        List<StatsTable> tables = statsTableService.listAll();

        tables.parallelStream().forEach(statTable -> {
            long newCount = statsService.countTableRecords(statTable.getDictTable());
            long oldCount = statTable.getTotalRecords();

            if (oldCount != newCount) {
                statTable.setTotalRecords(newCount);
                // 防止第一次统计时程序把时间错该改为最新时间
                if (oldCount != 0) {
                    statTable.setUpdateDate(LocalDate.now());
                }

                statsTableService.save(statTable);
            }
        });

    }
}
