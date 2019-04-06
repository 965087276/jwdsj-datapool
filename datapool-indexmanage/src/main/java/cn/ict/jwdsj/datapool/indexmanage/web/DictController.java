package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.service.DictionaryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class DictController {
    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation(value = "表信息管理--表信息增加--获取库名")
    @GetMapping("dict/database_names")
    public ResponseEntity<List<DatabaseNameDTO>> getDatabaseNames() {
        List<DatabaseNameDTO> nameDTOS = dictionaryService.listDatabaseNames();
        return ResponseEntity.ok(nameDTOS);
    }

    @ApiOperation(value = "表信息管理--表信息增加--获取表名")
    @ApiImplicitParam(name = "databaseId", value = "库的id", paramType = "query", required = true)
    @GetMapping("dict/table_names")
    public ResponseEntity<List<TableNameDTO>> getTableNamesNotAdd(@RequestParam("databaseId") long databaseId) {
        List<TableNameDTO> nameDTOS = dictionaryService.listTableNamesNotAddByDatabaseId(databaseId);
        return ResponseEntity.ok(nameDTOS);
    }

    @ApiOperation(value = "表信息管理--表信息增加--获取所有字段名")
    @ApiImplicitParam(name = "tableId", value = "表的id", paramType = "query", required = true)
    @GetMapping("dict/column_names")
    public ResponseEntity<List<ColumnNameDTO>> getColumnNamesNotAdd(@RequestParam("tableId") long tableId) {
        List<ColumnNameDTO> nameDTOS = dictionaryService.listColumnNamesByTableId(tableId);
        return ResponseEntity.ok(nameDTOS);
    }
}
