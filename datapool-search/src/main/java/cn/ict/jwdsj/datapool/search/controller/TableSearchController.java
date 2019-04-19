package cn.ict.jwdsj.datapool.search.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.search.entity.vo.SearchTableVO;
import cn.ict.jwdsj.datapool.search.service.TableSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(description = "搜索引擎模块--表搜索结果展示页")
@RestController
public class TableSearchController {

    @Autowired
    private TableSearchService tableSearchService;

    @ApiOperation(value = "表下搜索（按照表id）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchWord", value = "搜索词", paramType = "query", required = true),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true)
    })
    @GetMapping("search_engine/search/tableId/{tableId}")
    public ResponseEntity<SearchTableVO> aggByDatabase(
            @RequestParam(value = "searchWord", required = true) String searchWord,
            @RequestParam(value = "tableId", required = true) long tableId,
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        SearchTableVO vo = tableSearchService.searchByTableId(tableId, searchWord, curPage, pageSize);
        return ResponseEntity.ok(vo);
    }

    @ApiIgnore
    @ApiOperation(value = "表下搜索（按照库名和表名）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchWord", value = "搜索词", paramType = "query", required = true),
            @ApiImplicitParam(name = "enDatabase", value = "库名", paramType = "path", required = true),
            @ApiImplicitParam(name = "enTable", value = "表名", paramType = "path", required = true),
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true)
    })
    @GetMapping("search_engine/search/enDatabases/{enDatabase}/enTables/{enTable}")
    public ResponseEntity<SearchTableVO> aggByDatabase(
            @PathVariable("enDatabase") String enDatabase,
            @PathVariable("enTable") String enTable,
            @RequestParam(value = "searchWord", required = true) String searchWord,
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize)
    {

        SearchTableVO vo = tableSearchService.searchByEnDatabaseAndEnTable(enDatabase, enTable, searchWord, curPage, pageSize);
        return ResponseEntity.ok(vo);
    }



}
