package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatTable;
import cn.ict.jwdsj.datapool.datastat.service.StatService;
import cn.ict.jwdsj.datapool.datastat.service.StatTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatTableSubTask {

    @Autowired
    private StatTableService statTableService;
    @Autowired
    private StatService statService;

    public void updateStatTables() {

        statTableService.deleteTablesNotExist();
        statTableService.saveTablesNotAdd();

        List<StatTable> tables = statTableService.listAll();

        tables.parallelStream().forEach(statTable -> {
            long newCount = statService.countTableRecords(statTable.getDictTable());
            long oldCount = statTable.getTotalRecords();

            if (oldCount != newCount) {
                statTable.setTotalRecords(newCount);
                // 防止第一次统计时程序把时间错该改为最新时间
                if (oldCount != 0) {
                    statTable.setUpdateDate(new Date());
                }

                statTableService.save(statTable);
            }
        });

    }
}
