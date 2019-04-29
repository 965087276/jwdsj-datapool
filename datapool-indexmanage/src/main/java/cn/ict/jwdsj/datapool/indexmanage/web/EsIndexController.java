package cn.ict.jwdsj.datapool.indexmanage.web;

import cn.ict.jwdsj.datapool.common.entity.indexmanage.EsIndex;
import cn.ict.jwdsj.datapool.common.http.ResponseEntity;
import cn.ict.jwdsj.datapool.indexmanage.db.entity.dto.EsIndexDTO;
import cn.ict.jwdsj.datapool.indexmanage.db.service.EsIndexService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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

    @ApiOperation(value = "索引信息管理--删除索引")
    @ApiImplicitParam(name = "indexId", value = "索引id", paramType = "path", required = true)
    @DeleteMapping("index_manage/indexes/indexId/{indexId}")
    public ResponseEntity deleteIndex(@PathVariable("indexId") long indexId) throws IOException {
        esIndexService.deleteIndexById(indexId);
        return ResponseEntity.ok();
    }

    @ApiOperation(value = "索引信息管理--索引列表")
    @GetMapping("index_manage/indexes")
    public ResponseEntity<List<EsIndex>> addIndex() {
        List<EsIndex> list = esIndexService.listAll();
        return ResponseEntity.ok(list);
    }

}
