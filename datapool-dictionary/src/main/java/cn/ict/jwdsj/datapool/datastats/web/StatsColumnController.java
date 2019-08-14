package cn.ict.jwdsj.datapool.datastats.web;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.datastats.service.StatsColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
public class StatsColumnController {
    @Autowired
    private StatsColumnService statsColumnService;

    @ApiOperation(value = "字段数据统计--字段信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true)
    })
    @GetMapping("stats/stats_columns")
    public ResponseEntity<List<StatsColumn>> listAll(
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "tableId", required = true) long tableId) {
        List<StatsColumn> dictColumns = statsColumnService.listAll(databaseId, tableId);
        return ResponseEntity.ok(dictColumns);
    }

    @ApiIgnore
    @GetMapping("stats/stats_column/defect_column_names/tableId/{tableId}")
    public List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId) {
        return statsColumnService.initAndListDefectedColumns(tableId);
    }
}
