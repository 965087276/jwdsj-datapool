package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.datastat.service.StatColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatColumnController {
    @Autowired
    private StatColumnService statColumnService;

    @GetMapping("stat/stat_column/defect_column_names/tableId/{tableId}")
    public List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId) {
        return statColumnService.initAndListDefectedColumns(tableId);
    }
}