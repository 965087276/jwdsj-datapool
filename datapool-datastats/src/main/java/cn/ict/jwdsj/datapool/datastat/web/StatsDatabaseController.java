package cn.ict.jwdsj.datapool.datastat.web;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class StatsDatabaseController {

    @Autowired
    private StatsDatabaseService statsDatabaseService;

    @GetMapping("stats/stats_databases/ids/{ids}")
    List<StatsDatabase> listDatabasesByDictDatabaseIds(@PathVariable("ids") String ids) {

        return statsDatabaseService.listByDictDatabaseIds(ids);
    }
}
