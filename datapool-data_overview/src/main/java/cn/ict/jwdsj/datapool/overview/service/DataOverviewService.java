package cn.ict.jwdsj.datapool.overview.service;

import java.util.List;
import java.util.Map;

public interface DataOverviewService {

    /**
     * 返回某表的数据
     * @param database 库名
     * @param table 表名
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @return
     */
    List<Map<String, Object>> getAllRecords(String database, String table, int curPage, int pageSize);
}
