package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;

import java.util.List;

public interface StatsDatabaseService {

    void save(StatsDatabase statsDatabase);

    void saveAll(List<StatsDatabase> statsDatabases);

    void update(StatsDatabase statsDatabase);

    List<StatsDatabase> listAll();

    /**
     * 查找在dict_database中存在但是在stat_database中不存在的库
     * 这些库应该加入stat_database中
     * @return dict_database的id
     */
    void saveDatabasesNotAdd();

    /**
     * 查找在dict_database中不存在但是在stat_database中存在的库
     * 这些库应该从stat_database中删除
     * @return stat_database的id
     */
    void deleteDatabasesNotExist();
}
