package cn.ict.jwdsj.datapool.overview.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.overview.service.DataOverviewService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DataOverviewController {

    @Autowired
    private DataOverviewService dataOverviewService;

    @ApiOperation(value = "数据查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "database", value = "库名", paramType = "query", required = true),
            @ApiImplicitParam(name = "table", value = "表名", paramType = "query", required = true),
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true)
    })
    @GetMapping("data_overview/records")
    public ResponseEntity<List<Map<String, Object>>> getAllRecords(
            @RequestParam("database") String database,
            @RequestParam("table") String table,
            @RequestParam("curPage") int curPage,
            @RequestParam("pageSize") int pageSize
    ) {
        List<Map<String, Object>> records = dataOverviewService.getAllRecords(database, table, curPage, pageSize);
        return ResponseEntity.ok(records);
    }
}
