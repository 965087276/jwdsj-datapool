package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MappingColumnController {
    @Autowired
    private MappingColumnService mappingColumnService;

    @ApiOperation(value = "表信息管理--表信息增加--加载字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true)
    })
    @GetMapping("index_manage/mapping_columns_init")
    ResponseEntity<List<MappingColumnVO>> getInitMappingColumns(
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "tableId", required = true) long tableId
    ) {
        List<MappingColumnVO> columns = mappingColumnService.getInitMappingColumns(databaseId, tableId);
        return ResponseEntity.ok(columns);
    }

}
