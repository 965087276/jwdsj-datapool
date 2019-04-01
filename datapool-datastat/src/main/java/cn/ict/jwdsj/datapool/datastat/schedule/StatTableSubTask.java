package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.ict.jwdsj.datapool.datastat.entity.StatTable;
import cn.ict.jwdsj.datapool.datastat.service.StatService;
import cn.ict.jwdsj.datapool.datastat.service.StatTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
            long count = statService.countTableRecords(statTable.getDictTable());
            if (statTable.getTotalRecords() != count) {
                statTable.setTotalRecords(count);
                statTable.setUpdateDate(new Date());
                statTableService.save(statTable);
            }
        });

    }
}
