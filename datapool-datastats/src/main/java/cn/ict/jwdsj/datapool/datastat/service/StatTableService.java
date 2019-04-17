package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatTable;

import java.util.Date;
import java.util.List;

public interface StatTableService {

    void save(StatTable statTable);

    void saveAll(List<StatTable> statTables);

    void update(StatTable statTable);

    List<StatTable> listAll();

    /**
     * 获取表的创建日期
     * @return
     */
    Date getTableCreateTime(long tableId);

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
