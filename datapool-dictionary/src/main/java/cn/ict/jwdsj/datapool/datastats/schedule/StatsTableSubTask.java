package cn.ict.jwdsj.datapool.datastats.schedule;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.datastats.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastats.service.StatsService;
import cn.ict.jwdsj.datapool.datastats.service.StatsTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class StatsTableSubTask {

    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsService statsService;
    @Autowired
    private StatsColumnService statsColumnService;

    public void updateStatTables() {

        List<StatsTable> tables = statsTableService.listAll();

        tables.parallelStream().forEach(statsTable -> {
            StatsTable newStatsTable = (StatsTable) statsTable.clone();
            newStatsTable.setTotalRecords( statsService.countTableRecords(statsTable.getTableId()) );
            newStatsTable.setTotalColumns( statsColumnService.countColumnsByTableId(statsTable.getTableId()) );
            newStatsTable.setDefectColumns( statsColumnService.countDefectedColumnsByTableId(statsTable.getTableId()) );
            newStatsTable.setUpdateDate( statsService.getTableUpdateTime(statsTable.getTableId()) );
            newStatsTable.setDefectRate();

            log.info("newStatsTable is {}", newStatsTable);

            if (!statsTable.equals(newStatsTable)) {
                statsTableService.save(newStatsTable);
            }
        });

    }
}
