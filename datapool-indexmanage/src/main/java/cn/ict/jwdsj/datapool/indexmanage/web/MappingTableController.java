package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingTable;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.MappingTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.MappingTableVO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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

    @ApiOperation(value = "数据同步管理--表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "表名搜索", paramType = "query", required = false)
    })
    @GetMapping("index_manage/mapping_tables")
    public ResponseEntity<Page<MappingTableVO>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<MappingTableVO> mappingTableVOS = mappingTableService.listMappingTableVO(curPage, pageSize, databaseId, nameLike);
        return ResponseEntity.ok(mappingTableVOS);
    }

    @ApiIgnore
    @GetMapping("index_manage/mapping_table/need_to_update")
    public List<MappingTable> findTableNeedToUpdate() {
        return mappingTableService.listTableNeedToUpdate();
    }
}
