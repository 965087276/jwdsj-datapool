package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsColumnController {
    @Autowired
    private StatsColumnService statsColumnService;

    @GetMapping("stats/stats_column/defect_column_names/tableId/{tableId}")
    public List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId) {
        return statsColumnService.initAndListDefectedColumns(tableId);
    }
}
