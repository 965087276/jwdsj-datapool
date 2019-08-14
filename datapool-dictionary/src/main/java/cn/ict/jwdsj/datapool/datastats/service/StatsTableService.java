package cn.ict.jwdsj.datapool.datastats.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface StatsTableService {

    void save(StatsTable statsTable);

    void saveAll(List<StatsTable> statsTables);

    void update(StatsTable statsTable);

    StatsTable findByTableId(long tableId);

    List<StatsTable> listAll();



    /**
     * 获取某个库下所有表的最新更新日期（这个日期作为库的更新日期）
     * @param databaseId 库id
     * @return
     */
    LocalDate getDatabaseUpdateDate(long databaseId);

    /**
     * 获取某个库下的表的数目
     * @param databaseId
     * @return
     */
    int countTablesByDatabaseId(long databaseId);

    /**
     * 获取某个库的记录数
     * @param databaseId 库id
     * @return
     */
    long countDatabaseRecords(long databaseId);


    /**
     * 表信息列表
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @param databaseId 库的dict_id
     * @param nameLike 表名搜索
     * @return
     */
    Page<StatsTable> listAll(int curPage, int pageSize, long databaseId, String nameLike);

    /**
     * 更新表名信息
     * @param tableId
     * @param enTable
     * @param chTable
     */
    void updateTableInfo(long tableId, String enTable, String chTable);

    /**
     * 批量存入dict_table表新增的数据
     * @param currentTime 插入时的时间
     */
    void saveAllFromDictTable(String currentTime);

    /**
     * 根据table_id删除
     * @param tableId
     */
    void deleteByTableId(long tableId);
}
