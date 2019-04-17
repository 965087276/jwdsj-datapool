package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "datapool-dictionary")
public interface DictClient {

    @GetMapping("dict/dict_tables/id/{id}")
    DictTable findDictTableById(@PathVariable("id") long id);

    @GetMapping("dict/dict_databases/id/{id}")
    DictDatabase findDictDatabaseBy(@PathVariable("id") long id);
}
