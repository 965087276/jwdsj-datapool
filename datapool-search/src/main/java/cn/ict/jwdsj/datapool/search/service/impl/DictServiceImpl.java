package cn.ict.jwdsj.datapool.search.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.search.client.DictClient;
import cn.ict.jwdsj.datapool.search.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.joining;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictClient dictClient;

    @Override
    public Future<List<DictDatabase>> listDatabasesByIds(List<Long> ids) {
        String idStr = ids.stream().map(i -> i.toString()).collect(joining(","));
        List<DictDatabase> databases = dictClient.listDatabasesByIds(idStr);
        return new AsyncResult<>(databases);
    }
}
