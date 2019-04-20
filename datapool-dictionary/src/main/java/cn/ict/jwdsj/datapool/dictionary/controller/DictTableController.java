package cn.ict.jwdsj.datapool.dictionary.controller;

import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTableMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.UpdateTableDTO;
import cn.ict.jwdsj.datapool.dictionary.table.entity.vo.DictTableVO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTbExcelService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DictTableController {
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private DictTbExcelService dictTbExcelService;

//    @ApiOperation(value = "表信息管理页--库下拉框")
//    @GetMapping("dict/dict_table/database_drop_down_box")
//    public ResponseEntity<List<DatabaseNameDTO>> getDatabaseDropDownBox() {
//        List<DatabaseNameDTO> list = dictTableService.listDatabaseDropDownBox();
//        return ResponseEntity.ok(list);
//    }

    @ApiOperation(value = "表信息管理页--表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "表名搜索", paramType = "query", required = false)
    })
    @GetMapping("dict/dict_tables")
    public ResponseEntity<Page<DictTableVO>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<DictTableVO> dictTableVOS = dictTableService.listVO(curPage, pageSize, databaseId, nameLike);
        return ResponseEntity.ok(dictTableVOS);
    }

    @ApiOperation(value = "表中英下拉框")
    @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    @GetMapping("dict/dict_table/table_drop_down_box")
    public ResponseEntity<List<TableNameDTO>> getDatabaseDropDownBox(
        @RequestParam(name = "databaseId", required = true) long databaseId) {

        List<TableNameDTO> list = dictTableService.listTableDropDownBox(databaseId);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "表信息管理页--添加表--手动添加")
    @ApiImplicitParam(name = "dictTableMultiAddDTO", value = "表信息", dataType = "DictTableMultiAddDTO", required = true)
    @PostMapping("dict/dict_tables")
    public ResponseEntity addOne(@Valid @RequestBody DictTableMultiAddDTO dictTableMultiAddDTO) {
        dictTableService.saveAll(dictTableMultiAddDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理页--修改表信息")
    @ApiImplicitParam(name = "updateTableDTO", value = "表信息", dataType = "UpdateTableDTO", required = true)
    @PutMapping("dict/dict_tables")
    public ResponseEntity update(@Valid @RequestBody UpdateTableDTO updateTableDTO) {
        dictTableService.update(updateTableDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理页--添加表--excel添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true)
    })
    @PostMapping("dict/dict_tables_file")
    public ResponseEntity fileUpload(
            @RequestParam(name = "databaseId", required = true) long databaseId,
            @RequestParam("file") MultipartFile file) throws IOException {
        dictTbExcelService.saveAll(databaseId, file);
        return ResponseEntity.ok();
    }

    @ApiIgnore
    @GetMapping("dict/dict_tables/id/{id}")
    public DictTable findById(@PathVariable("id") long id) {
        return dictTableService.findById(id);
    }

    @ApiIgnore
    @GetMapping("dict/table_name_dtos/ids/{ids}")
    List<TableNameDTO> listTableNameDTOByIdIn(@PathVariable("ids") String idStr) {
        List<Long> ids = Arrays.stream(idStr.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return dictTableService.listTableNameDTOByIds(ids);
    }
}
