package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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

    /**
     * 获取某表需要加入到搜索引擎中的字段
     * @return 字段的名字
     */
    @ApiIgnore
    @GetMapping("index_manage/mapping_column_names/tableId/{tableId}")
    List<String> listColumnNamesByTableId(@PathVariable("tableId") long tableId) {
        return mappingColumnService.listColumnNamesByTableId(tableId);
    }

}
