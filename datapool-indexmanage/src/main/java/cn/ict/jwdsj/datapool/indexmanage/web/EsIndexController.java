package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class EsIndexController {
    @Autowired
    private EsIndexService esIndexService;

    @ApiOperation(value = "索引信息管理--增加索引")
    @ApiImplicitParam(name = "esIndexDTO", value = "索引信息，包括索引名，主分片数", required = true, dataType = "EsIndexDTO")
    @PostMapping("index_manage/indexes")
    public ResponseEntity addIndex(@Valid @RequestBody EsIndexDTO esIndexDTO) throws IOException {
        esIndexService.createIndex(esIndexDTO);
        return ResponseEntity.ok();
    }
}
