package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.database.entity.dto.DictDatabaseDTO;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DictDatabaseController {
    @Autowired
    private DictDatabaseService dictDatabaseService;

    @ApiOperation(value = "库信息管理页--库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "enNameLike", value = "英文名搜索", paramType = "query", required = false),
            @ApiImplicitParam(name = "chNameLike", value = "中文名搜索", paramType = "query", required = false)
    })
    @GetMapping("dict/dict_databases")
    public ResponseEntity<Page<DictDatabaseVO>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "enNameLike", required = false) String enNameLike,
            @RequestParam(value = "chNameLike", required = false) String chNameLike) {

        Page<DictDatabaseVO> dictDatabases = dictDatabaseService.list(curPage, pageSize, enNameLike, chNameLike);
        return ResponseEntity.ok(dictDatabases);
    }

    @ApiOperation(value = "库信息管理页--添加库")
    @ApiImplicitParam(name = "dictDatabaseDTO", value = "库信息", dataType = "DictDatabaseDTO", required = true)
    @PostMapping("dict/dict_databases")
    public ResponseEntity addOne(@Valid @RequestBody DictDatabaseDTO dictDatabaseDTO) {
        DictDatabase dictDatabase = BeanUtil.toBean(dictDatabaseDTO, DictDatabase.class);
        dictDatabaseService.save(dictDatabase);
        return ResponseEntity.ok();
    }




}
