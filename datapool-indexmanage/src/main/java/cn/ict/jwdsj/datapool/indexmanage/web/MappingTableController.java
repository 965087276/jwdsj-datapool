package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class MappingTableController {

    @Autowired
    private MappingTableService mappingTableService;

    @ApiOperation(value = "数据同步管理--添加新表")
    @ApiImplicitParam(name = "mappingTableAddDTO", value = "表对象，包括库id、表id、索引id、更新周期", required = true, dataType = "MappingTableAddDTO")
    @PostMapping("index_manage/mapping_tables")
    public ResponseEntity addTableMapping(@Valid @RequestBody MappingTableAddDTO mappingTableAddDTO) throws IOException {
        mappingTableService.save(mappingTableAddDTO);
        return ResponseEntity.ok();
    }
}
