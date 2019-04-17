package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;

import java.util.List;
import java.util.concurrent.Future;

public interface StatsService {
    Future<List<StatsDatabase>> listDatabasesByIds(List<Long> ids);
}
