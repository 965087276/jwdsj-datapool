package cn.ict.jwdsj.datapool.datastat.schedule;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.clone.Cloneable;
import cn.hutool.core.util.ObjectUtil;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        // 删去字典中不存在的表
        statsTableService.deleteTablesNotExist();
        // 添加字典中存在但是统计模块不存在的表
        statsTableService.saveTablesNotAdd();

        List<StatsTable> tables = statsTableService.listAll();

        tables.parallelStream().forEach(statsTable -> {
            StatsTable newStatsTable = (StatsTable) statsTable.clone();

            newStatsTable.setTotalRecords( statsService.countTableRecords(statsTable.getDictTableId()) );
            newStatsTable.setTotalColumns( statsColumnService.countColumnsByTableId(statsTable.getDictTableId()) );
            newStatsTable.setDefectColumns( statsColumnService.countDefectedColumnsByTableId(statsTable.getDictTableId()) );
            newStatsTable.setDefectRate();
            log.info("newStatsTable is {}", newStatsTable);
            if (!statsTable.equals(newStatsTable)) {
                if (statsTable.getTotalRecords() != 0)
                    newStatsTable.setUpdateDate(LocalDate.now());
                statsTableService.save(newStatsTable);
            }

        });

    }
}
