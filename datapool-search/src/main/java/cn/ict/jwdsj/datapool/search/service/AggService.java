package cn.ict.jwdsj.datapool.search.service;

import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabaseVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggTableVO;

import java.util.List;

public interface AggService {
    /**
     * 某库下的表的聚合
     * @param databaseId 数据库id
     * @param searchWord 搜索词
     * @return
     */
    List<AggTableVO> aggByTable(long databaseId, String searchWord);

    /**
     * 全库的库级聚合
     * @param searchWord 搜索词
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @return
     */
    List<AggDatabaseVO> aggByDatabase(String searchWord, int curPage, int pageSize);
}
