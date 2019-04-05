package cn.ict.jwdsj.datapool.dictionary.web.controller;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictDatabaseController {
    @Autowired
    private DictDatabaseService dictDatabaseService;

}
