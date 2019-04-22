package cn.ict.jwdsj.datapool.indexmanage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "datapool-datastats")
public interface StatClient {
    @GetMapping("stats/stats_column/defect_column_names/tableId/{tableId}")
    List<String> getDefectColumnsByTable(@PathVariable("tableId") long tableId);

}