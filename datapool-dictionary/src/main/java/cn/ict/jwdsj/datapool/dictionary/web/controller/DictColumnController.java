package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "字段信息管理")
@RestController
public class DictColumnController {
    @Autowired
    private DictColumnService dictColumnService;

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
}
