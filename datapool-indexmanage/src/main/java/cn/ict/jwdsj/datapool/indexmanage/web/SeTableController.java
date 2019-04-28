package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.SeTableAddDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.vo.SeTableVO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.SeTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 搜索引擎表信息的管理
 */
@RestController
public class SeTableController {
    @Autowired
    private SeTableService seTableService;

    @ApiOperation(value = "表信息管理--添加表至搜索引擎--添加表及字段信息")
    @ApiImplicitParam(name = "seTableAddDTO", value = "表对象,包括库名,表名,各字段", required = true, dataType = "SeTableAddDTO")
    @PostMapping("index_manage/se_tables")
    public ResponseEntity add(@Valid @RequestBody SeTableAddDTO seTableAddDTO) {
        seTableService.save(seTableAddDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理--删除表")
    @ApiImplicitParam(name = "dictTableId", value = "表id", paramType = "path", required = true)
    @DeleteMapping("index_manage/se_tables/dictTableId/{dictTableId}")
    public ResponseEntity delete(@PathVariable("dictTableId") long dictTableId) {
        seTableService.deleteByDictTableId(dictTableId);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "表信息管理页--表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "databaseId", value = "数据库id", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "表名搜索", paramType = "query", required = false)
    })
    @GetMapping("index_manage/se_tables")
    public ResponseEntity<Page<SeTableVO>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "databaseId", required = true) long databaseId,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<SeTableVO> seTableVOS = seTableService.listSeTableVO(curPage, pageSize, databaseId, nameLike);
        return ResponseEntity.ok(seTableVOS);
    }

}
