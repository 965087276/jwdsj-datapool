package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetaTableController {
    @Autowired
    private MetaTableService metaTableService;

    @ApiOperation(value = "表信息管理页--手动添加表--表英文名下拉框")
    @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    @GetMapping("meta/table_names")
    public ResponseEntity<List<String>> listDatabasesNotAdd(
            @RequestParam(name = "databaseId", required = true) long databaseId) {
        List<String> databases = metaTableService.listTablesNotAdd(databaseId);
        return ResponseEntity.ok(databases);
    }
}
