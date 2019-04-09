package cn.ict.jwdsj.datapool.indexmanage.db.service.feignclient;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-dictionary")
public interface DictClient {

    @GetMapping("dict/dict_columns/tableId/{tableId}")
    List<DictColumn> listDictColumnsByTableId(@PathVariable("tableId") long tableId);

    @GetMapping("dict/dict_tables/{id}")
    DictTable findDictTableById(@PathVariable("id") long id);

    @GetMapping("dict/dict_databases/{id}")
    DictDatabase findDictDatabaseBy(@PathVariable("id") long id);

}
