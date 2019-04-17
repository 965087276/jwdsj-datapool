package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;

import java.util.List;
import java.util.concurrent.Future;

public interface DictService {
    Future<List<DictDatabase>> listDatabasesByIds(List<Long> ids);
}
