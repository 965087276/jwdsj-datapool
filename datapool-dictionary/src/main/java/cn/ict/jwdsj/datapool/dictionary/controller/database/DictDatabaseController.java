package cn.ict.jwdsj.datapool.dictionary.controller.database;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.entity.database.dto.DictDatabaseDTO;
import cn.ict.jwdsj.datapool.dictionary.entity.database.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.service.database.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.service.database.DictDbExcelService;
import cn.ict.jwdsj.datapool.dictionary.entity.database.dto.UpdateDatabaseDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class DictDatabaseController {
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private DictDbExcelService dictDbExcelService;

    @ApiOperation(value = "库信息管理页--库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "第几页", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", paramType = "query", required = true),
            @ApiImplicitParam(name = "nameLike", value = "库名搜索", paramType = "query", required = false)
    })
    @GetMapping("dict/dict_databases")
    public ResponseEntity<Page<DictDatabaseVO>> listAll(
            @RequestParam(value = "curPage", required = true) int curPage,
            @RequestParam(value = "pageSize", required = true) int pageSize,
            @RequestParam(value = "nameLike", required = false) String nameLike) {

        Page<DictDatabaseVO> dictDatabases = dictDatabaseService.listVO(curPage, pageSize, nameLike);
        return ResponseEntity.ok(dictDatabases);
    }

    @ApiOperation(value = "库中英下拉框")
    @GetMapping("dict/dict_database/database_drop_down_box")
    public ResponseEntity<List<DatabaseNameDTO>> getDatabaseDropDownBox() {
        List<DatabaseNameDTO> list = dictDatabaseService.listDatabaseDropDownBox();
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "库信息管理页--添加库--手动添加")
    @ApiImplicitParam(name = "dictDatabaseDTO", value = "库信息", dataType = "DictDatabaseDTO", required = true)
    @PostMapping("dict/dict_databases")
    public ResponseEntity addOne(@Valid @RequestBody DictDatabaseDTO dictDatabaseDTO) {
        DictDatabase dictDatabase = BeanUtil.toBean(dictDatabaseDTO, DictDatabase.class);
        dictDatabaseService.save(dictDatabase);
        return ResponseEntity.ok();
    }



    @ApiOperation(value = "库信息管理页--修改库信息")
    @ApiImplicitParam(name = "updateDatabaseDTO", value = "库信息", dataType = "UpdateDatabaseDTO", required = true)
    @PutMapping("dict/dict_databases")
    public ResponseEntity update(@Valid @RequestBody UpdateDatabaseDTO updateDatabaseDTO) {
        dictDatabaseService.update(updateDatabaseDTO);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "库信息管理页--删除库")
    @ApiImplicitParam(name = "id", value = "库id", paramType = "path", required = true)
    @DeleteMapping(("dict/dict_databases/id/{id}"))
    public ResponseEntity deleteById(@PathVariable("id") long id) {
        dictDatabaseService.delete(id);
        return ResponseEntity.ok();
    }


    @ApiOperation(value = "库信息管理页--添加库--excel添加")
    @PostMapping("dict/dict_databases_file")
    public ResponseEntity fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        dictDbExcelService.saveByExcel(file);
        return ResponseEntity.ok();
    }

    @ApiIgnore
    @GetMapping("dict/dict_databases/id/{id}")
    public DictDatabase getById(@PathVariable("id") long id) {
        return dictDatabaseService.findById(id);
    }

    @ApiIgnore
    @GetMapping("dict/dict_databases/ids/{ids}")
    public List<DictDatabase> listByIds(@PathVariable("ids") String ids) {
        List<DictDatabase> dictDatabases = dictDatabaseService.listByIds(ids);
        return dictDatabases;
    }

    @ApiIgnore
    @GetMapping("dict/dict_databases/all")
    public List<DictDatabase> listAll() {
        return dictDatabaseService.listAll();
    }


}
