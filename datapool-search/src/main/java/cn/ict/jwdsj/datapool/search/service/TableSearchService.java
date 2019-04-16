package cn.ict.jwdsj.datapool.search.service;

import cn.hutool.json.JSONObject;

import java.util.List;

public interface TableSearchService {
    /**
     * 在某表下搜索
     *
     * @param databaseId 库id
     * @param tableId 表id
     * @param searchWord 搜索词
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @return
     */
    List<JSONObject> searchInTable(long databaseId, long tableId, String searchWord, int curPage, int pageSize);
}
