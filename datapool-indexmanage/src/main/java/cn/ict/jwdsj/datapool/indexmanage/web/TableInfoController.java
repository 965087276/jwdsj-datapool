package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.TableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 搜索引擎表信息管理部分（添加表及表的字段）
 */
@RestController
public class TableInfoController {

    @Autowired
    private MappingColumnService mappingColumnService;

    @ApiOperation(value = "表信息管理--表信息增加--添加表及字段信息")
    @ApiImplicitParam(name = "tableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "TableAddDTO")
    @PostMapping("mapping/columns")
    public ResponseEntity addColumnsMapping(@Valid @RequestBody TableAddDTO tableAddDTO) {
        mappingColumnService.save(tableAddDTO);
        return ResponseEntity.ok();
    }



}
