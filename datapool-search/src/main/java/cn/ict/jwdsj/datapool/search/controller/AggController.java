package cn.ict.jwdsj.datapool.search.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabaseVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggTableVO;
import cn.ict.jwdsj.datapool.search.service.AggService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "搜索引擎模块--搜索结果聚合页")
@RestController
public class AggController {

    @Autowired
    private AggService aggService;

    @ApiOperation(value = "库聚合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchWord", value = "搜索词", paramType = "query", required = true),
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true)
    })
    @GetMapping("search_engine/agg/database")
    public ResponseEntity<List<AggDatabaseVO>> aggByDatabase(
            @RequestParam(value = "searchWord", required = true) String searchWord,
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize)
    {

        List<AggDatabaseVO> list = aggService.aggByDatabase(searchWord, curPage, pageSize);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "表聚合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchWord", value = "搜索词", paramType = "query", required = true),
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    })
    @GetMapping("search_engine/agg/table")
    public ResponseEntity<List<AggTableVO>> aggByDatabase(
            @RequestParam(value = "searchWord", required = true) String searchWord,
            @RequestParam(value = "databaseId", required = true) long databaseId)
    {
        List<AggTableVO> list = aggService.aggByTable(databaseId, searchWord);
        return ResponseEntity.ok(list);
    }



}
