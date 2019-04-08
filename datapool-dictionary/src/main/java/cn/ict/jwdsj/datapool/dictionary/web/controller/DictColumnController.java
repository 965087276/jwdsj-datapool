package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value = "表信息管理页--添加表--手动添加")
    @ApiImplicitParam(name = "dictColumnMultiAddDTO", value = "表信息", dataType = "DictColumnMultiAddDTO", required = true)
    @PostMapping("dict/dict_columns")
    public ResponseEntity addAll(@Valid @RequestBody DictColumnMultiAddDTO dictColumnMultiAddDTO) {
        dictColumnService.saveAll(dictColumnMultiAddDTO);
        return ResponseEntity.ok();
    }
}
