package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsTableController {

    @Autowired
    private StatsTableService statsTableService;

    @GetMapping("stats/stats_table/records/tableId/{tableId}")
    long getTableRecords(@PathVariable("tableId") long tableId) {
        return statsTableService.findByDictTableId(tableId).getTotalRecords();
    }
}
