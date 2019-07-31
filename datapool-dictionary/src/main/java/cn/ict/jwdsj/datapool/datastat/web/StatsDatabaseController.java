package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Slf4j
public class StatsDatabaseController {

    @Autowired
    private StatsDatabaseService statsDatabaseService;

    @ApiOperation(value = "库数据统计--库信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "库名搜索", paramType = "query", required = false)
    })
    @GetMapping("stats/stats_databases")
    public ResponseEntity<Page<StatsDatabase>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<StatsDatabase> statsDatabases = statsDatabaseService.listAll(curPage, pageSize, nameLike);
        return ResponseEntity.ok(statsDatabases);
    }

    @ApiIgnore
    @GetMapping("stats/stats_databases/ids/{ids}")
    List<StatsDatabase> listDatabasesByDatabaseIds(@PathVariable("ids") String ids) {

        return statsDatabaseService.listByDatabaseIds(ids);
    }
}
