package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.datastat.entity.StatTable;

import java.util.List;

public interface StatTableService {

    void save(StatTable statTable);

    void saveAll(List<StatTable> statTables);

    void update(StatTable statTable);

    List<StatTable> listAll();

    /**
     * 查找在dict_table存在但是在stat_table中不存在的表
     * 这些表需要加入到stat_table中
     */
    void saveTablesNotAdd();

    /**
     * 查找dict_table中不存在但是在stat_table中存在的表
     * 这些表需要从stat_table中删除
     */
    void deleteTablesNotExist();

}
