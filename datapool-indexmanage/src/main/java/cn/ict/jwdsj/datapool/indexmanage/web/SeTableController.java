package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索引擎表信息的管理
 */
@RestController
public class SeTableController {
    @Autowired
    private SeTableService seTableService;

    @ApiOperation(value = "表信息管理--添加表至搜索引擎--添加表及字段信息")
    @ApiImplicitParam(name = "seTableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "SeTableAddDTO")
    @PostMapping("mapping/se_tables")
    public ResponseEntity add(@RequestBody SeTableAddDTO seTableAddDTO) {
        seTableService.save(seTableAddDTO);
        return ResponseEntity.ok();
    }

}
