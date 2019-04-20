package cn.ict.jwdsj.datapool.datastat.service;

import java.util.List;

public interface StatsColumnService {
    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    List<String> initAndListDefectedColumns(long tableId);

    /**
     * 删去字典中不存在的字段
     */
    void deleteColumnsNotExist();

    /**
     * 添加字典中存在但是统计模块不存在的字段
     */
    void saveColumnsNotAdd();

    /**
     * 更新各表的缺陷字段
     */
    void updateDefectedColumns();
}
