package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaDatabaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetaDatabaseController {
    @Autowired
    private MetaDatabaseService metaDatabaseService;

    @ApiOperation(value = "库信息管理页--添加库--列出库英文名")
    @GetMapping("meta/database_names")
    public ResponseEntity<List<String>> listDatabasesNotAdd() {
        List<String> databases = metaDatabaseService.listDatabasesNotAdd();
        return ResponseEntity.ok(databases);
    }
}
