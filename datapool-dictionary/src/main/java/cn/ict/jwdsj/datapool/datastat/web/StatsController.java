package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {
    @Autowired
    private StatsService statsService;

    @ApiOperation(value = "字段数据统计--查看某字段的20条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enDatabase", value = "库名", paramType = "query", required = true),
            @ApiImplicitParam(name = "enTable", value = "表名", paramType = "query", required = true),
            @ApiImplicitParam(name = "enColumn", value = "字段名", paramType = "query", required = true),
    })
    @GetMapping("stats/stats_columns/column_data")
    public ResponseEntity<List<String>> listColumnData(
            @RequestParam(value = "enDatabase", required = true) String enDatabase,
            @RequestParam(value = "enTable", required = true) String enTable,
            @RequestParam(value = "enColumn", required = true) String enColumn) {
        List<String> columnDatas = statsService.getColumnData(enDatabase, enTable, enColumn);
        return ResponseEntity.ok(columnDatas);
    }


}
