package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.datastat.service.StatsService;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@RestController
public class StatsTableController {

    @Autowired
    private StatsTableService statsTableService;
    @Autowired
    private StatsService statsService;

    @ApiOperation(value = "表数据统计--表信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "表名搜索", paramType = "query", required = false)
    })
    @GetMapping("stats/stats_tables")
    public ResponseEntity<Page<StatsTable>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<StatsTable> dictTableVOS = statsTableService.listAll(curPage, pageSize, databaseId, nameLike);
        return ResponseEntity.ok(dictTableVOS);
    }

    @ApiIgnore
    @GetMapping("stats/stats_table/records/tableId/{tableId}")
    long getTableRecords(@PathVariable("tableId") long tableId) {
        return Optional.ofNullable(statsTableService.findByDictTableId(tableId))
                .map(StatsTable::getTotalRecords)
                .orElse(statsService.countTableRecords(tableId));
    }
}
