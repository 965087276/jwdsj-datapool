package cn.ict.jwdsj.datapool.search.service.impl;

import cn.ict.jwdsj.datapool.api.feign.StatsClient;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.search.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.joining;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private StatsClient statsClient;

    @Override
    @Async
    public Future<List<StatsDatabase>> listDatabasesByIds(List<Long> ids) {
        String idStr = ids.stream().map(i -> i.toString()).collect(joining(","));
        List<StatsDatabase> databases = statsClient.listStatsDatabasesByIds(idStr);
        return new AsyncResult<>(databases);
    }
}
