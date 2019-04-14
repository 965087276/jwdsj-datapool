package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(value = "字段信息管理")
@RestController
public class DictColumnController {
    @Autowired
    private DictColumnService dictColumnService;
    @Autowired
    private DictColExcelService dictColExcelService;

    @ApiOperation(value = "字段信息管理页--库下拉框")
    @GetMapping("dict/dict_column/database_drop_down_box")
    public ResponseEntity<List<DatabaseNameDTO>> getDatabaseDropDownBox() {
        List<DatabaseNameDTO> list = dictColumnService.listDatabaseDropDownBox();
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "字段信息管理页--表下拉框")
    @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    @GetMapping("dict/dict_column/table_drop_down_box")
    public ResponseEntity<List<TableNameDTO>> getDatabaseDropDownBox(
        @RequestParam(name = "databaseId", required = true) long databaseId) {

        List<TableNameDTO> list = dictColumnService.listTableDropDownBox(databaseId);
        return ResponseEntity.ok(list);
    }


    @ApiOperation(value = "字段信息管理页--字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true)
    })
    @GetMapping("dict/dict_columns")
    public ResponseEntity<List<DictColumnVO>> listAll(
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "tableId", required = true) long tableId) {
        List<DictColumnVO> dictColumns = dictColumnService.listDictColumnVOs(databaseId, tableId);
        return ResponseEntity.ok(dictColumns);
    }

    @ApiOperation(value = "字段信息管理页--添加字段--excel添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    })
    @PostMapping("dict/dict_columns_file")
    public ResponseEntity fileUpload(
            @RequestParam(name = "databaseId", required = true) long databaseId,
            @RequestParam("file") MultipartFile file) throws IOException {
        dictColExcelService.saveAll(databaseId, file);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "字段信息管理页--添加字段--手动添加")
    @ApiImplicitParam(name = "dictColumnMultiAddDTO", value = "字段信息", dataType = "DictColumnMultiAddDTO", required = true)
    @PostMapping("dict/dict_columns")
    public ResponseEntity addAll(@Valid @RequestBody DictColumnMultiAddDTO dictColumnMultiAddDTO) {
        dictColumnService.saveAll(dictColumnMultiAddDTO);
        return ResponseEntity.ok();
    }

    @ApiIgnore
    @GetMapping("dict/dict_columns/tableId/{tableId}")
    public List<DictColumn> listByTableId(@PathVariable("tableId") long tableId) {
        DictTable dictTable = DictTable.builtById(tableId);
        return dictColumnService.listByDictTable(dictTable);
    }
    @ApiIgnore
    @GetMapping("dict/column_name_dto/tableId/{tableId}")
    public List<ColumnNameDTO> listColumnNameDTOsByTableId(@PathVariable("tableId") long tableId) {
        return dictColumnService.listColumnNameDTOsByTableId(tableId);
    }
}
