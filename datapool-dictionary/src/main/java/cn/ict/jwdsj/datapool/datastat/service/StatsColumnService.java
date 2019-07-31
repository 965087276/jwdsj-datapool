package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;

import java.util.List;

public interface StatsColumnService {
    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    List<String> initAndListDefectedColumns(long tableId);

    /**
     * 更新各表的缺陷字段
     */
    void updateDefectedColumns();

    /**
     * 获取某表的字段数
     * @param tableId
     * @return
     */
    int countColumnsByTableId(long tableId);

    /**
     * 获取某表的缺陷字段数
     * @param tableId
     * @return
     */
    int countDefectedColumnsByTableId(long tableId);

    /**
     * 字段列表展示
     * @param databaseId 库id
     * @param tableId 表id
     * @return
     */
    List<StatsColumn> listAll(long databaseId, long tableId);

    /**
     * 获取字段内容
     * @param enDatabase 库名
     * @param enTable 表名
     * @param enColumn 字段名
     * @return
     */
    List<String> listColumnData(String enDatabase, String enTable, String enColumn);

    /**
     * 更新字段名信息
     * @param columnId
     * @param enColumn
     * @param chColumn
     */
    void updateColumnInfo(long columnId, String enColumn, String chColumn);
}
