package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
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

    @ApiOperation(value = "表信息管理--查看字段--字段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true)
    })
    @GetMapping("index_manage/mapping_columns")
    ResponseEntity<List<MappingColumnVO>> listMappingColumnVOs(
            @RequestParam(value = "tableId", required = true) long tableId
    ) {
        List<MappingColumnVO> columns = mappingColumnService.listMappingColumnVOs(tableId);
        return ResponseEntity.ok(columns);
    }

    /**
     * 获取全量读取数据时的字段信息
     * @return 字段信息。包括要加入到搜索引擎的字段列表，表字段到索引字段的映射。
     */
    @ApiIgnore
    @GetMapping("index_manage/table_full_read_dtos/tableId/{tableId}")
    TableFullReadDTO getTableFullReadDTOByTableId(@PathVariable("tableId") long tableId) {
        return mappingColumnService.getTableFullReadDTOByTableId(tableId);
    }

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    @ApiIgnore
    @GetMapping("index_manage/col_displayed_dtos")
    List<ColDisplayedDTO> listColDisplayedDTOByTableId(@RequestParam(value = "tableId") long tableId) {

        return mappingColumnService.listColDisplayedDTOByTableId(tableId);
    }

}
