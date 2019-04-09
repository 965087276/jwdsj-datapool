package cn.ict.jwdsj.datapool.indexmanage.db.service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-datastat")
public interface StatClient {
    @GetMapping("stat/stat_column/defect_column_names/tableId/{tableId}")
    List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId);

}
