package cn.ict.jwdsj.datapool.dictionary.meta.controller;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetaColumnController {

    @Autowired
    private MetaColumnService metaColumnService;

    @ApiOperation(value = "字段信息管理页--手动添加字段--字段英文名下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "tableId", value = "数据表id", paramType = "query", required = true)
    })
    @GetMapping("meta/column_names")
    public ResponseEntity<List<MetaColumn>> listDatabasesNotAdd(
            @RequestParam(name = "databaseId", required = true) long databaseId,
            @RequestParam(name = "tableId", required = true) long tableId) {
        List<MetaColumn> columns = metaColumnService.listColumnsNotAdd(databaseId, tableId);
        return ResponseEntity.ok(columns);
    }
}
