package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StatsTableController {

    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsService statsService;

    @GetMapping("stats/stats_table/records/tableId/{tableId}")
    long getTableRecords(@PathVariable("tableId") long tableId) {
        return Optional.ofNullable(statsTableService.findByDictTableId(tableId))
                .map(StatsTable::getTotalRecords)
                .orElse(statsService.countTableRecords(tableId));
    }
}
