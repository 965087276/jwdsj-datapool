package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.search.entity.vo.SearchTableVO;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface TableSearchService {
    /**
     * 在某表下搜索（使用表id）
     *
     * @param tableId 表id
     * @param searchWord 搜索词
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @return
     */
    SearchTableVO searchByTableId(long tableId, String searchWord, int curPage, int pageSize) throws IOException, InterruptedException, ExecutionException, TimeoutException;


    SearchTableVO searchByEnDatabaseAndEnTable(String enDatabase, String enTable, String searchWord, int curPage, int pageSize);
}
