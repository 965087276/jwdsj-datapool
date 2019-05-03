package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingColumnVO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
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
    public ResponseEntity<List<MappingColumnVO>> getInitMappingColumns(
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
    public ResponseEntity<List<MappingColumnVO>> listMappingColumnVOs(
            @RequestParam(value = "tableId", required = true) long tableId
    ) {
        List<MappingColumnVO> columns = mappingColumnService.listMappingColumnVOs(tableId);
        return ResponseEntity.ok(columns);
    }

    @ApiOperation(value = "表信息管理--查看字段--新增字段")
    @ApiImplicitParam(name = "seTableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "SeTableAddDTO")
    @PostMapping("index_manage/mapping_columns")
    public ResponseEntity add(@Valid @RequestBody SeTableAddDTO seTableAddDTO) {
        mappingColumnService.saveAll(seTableAddDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理--查看字段--更新字段（未配置数据同步，可任意增删改字段）前台传输所有字段")
    @ApiImplicitParam(name = "seTableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "SeTableAddDTO")
    @PutMapping("index_manage/mapping_columns/not_sync")
    public ResponseEntity updateColumnsNotSync(@Valid @RequestBody SeTableAddDTO seTableAddDTO) {
        mappingColumnService.updateColumnsNotSync(seTableAddDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理--查看字段--更新字段（已配置数据同步，可配置权重、非搜索字段的是否展示）前台只传输发生更新的字段")
    @ApiImplicitParam(name = "seTableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "SeTableAddDTO")
    @PutMapping("index_manage/mapping_columns/has_sync")
    public ResponseEntity updateColumnsHasSync(@Valid @RequestBody SeTableAddDTO seTableAddDTO) {
        mappingColumnService.updateColumnsHasSync(seTableAddDTO);
        return ResponseEntity.ok();
    }

    /**
     * 获取全量读取数据时的字段信息
     * @return 字段信息。包括要加入到搜索引擎的字段列表，表字段到索引字段的映射。
     */
    @ApiIgnore
    @GetMapping("index_manage/table_full_read_dtos/tableId/{tableId}")
    public TableFullReadDTO getTableFullReadDTOByTableId(@PathVariable("tableId") long tableId) {
        return mappingColumnService.getTableFullReadDTOByTableId(tableId);
    }

    /**
     * 返回某表需要在前端展示的字段（用于搜索引擎的表查询）
     * @param tableId 表id
     * @return
     */
    @ApiIgnore
    @GetMapping("index_manage/col_displayed_dtos")
    public List<ColDisplayedDTO> listColDisplayedDTOByTableId(@RequestParam(value = "tableId") long tableId) {
        return mappingColumnService.listColDisplayedDTOByTableId(tableId);
    }

}
