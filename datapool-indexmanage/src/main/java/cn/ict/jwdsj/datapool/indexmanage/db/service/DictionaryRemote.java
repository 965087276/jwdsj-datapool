package cn.ict.jwdsj.datapool.indexmanage.db.service;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "datapool-dictionary")
public interface DictionaryRemote {
    @GetMapping("remote/dict/database_names")
    List<DatabaseNameDTO> listDatabaseNames();

    @GetMapping("remote/dict/column_names")
    List<ColumnNameDTO> listColumnNamesByTableId(@RequestParam("tableId") long tableId);
}
