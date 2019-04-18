package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabasePageVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggTableVO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface AggService {
    /**
     * 某库下的表的聚合
     * @param databaseId 数据库id
     * @param searchWord 搜索词
     * @return
     */
    List<AggTableVO> aggByTable(long databaseId, String searchWord) throws IOException;

    /**
     * 全库的库级聚合
     * @param searchWord 搜索词
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @return
     */
    AggDatabasePageVO aggByDatabase(String searchWord, int curPage, int pageSize) throws IOException, InterruptedException, ExecutionException, TimeoutException;
}
