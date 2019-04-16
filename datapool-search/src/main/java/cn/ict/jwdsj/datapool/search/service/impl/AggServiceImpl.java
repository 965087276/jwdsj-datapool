package cn.ict.jwdsj.datapool.search.service.impl;

import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabaseVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggTableVO;
import cn.ict.jwdsj.datapool.search.service.AggService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AggServiceImpl implements AggService {

    @Autowired private RestHighLevelClient client;

    /**
     * 某库下的表的聚合
     *
     * @param databaseId 数据库id
     * @param searchWord 搜索词
     * @return
     */
    @Override
    public List<AggTableVO> aggByTable(long databaseId, String searchWord) {
        return null;
    }

    /**
     * 全库的库级聚合
     *
     * @param searchWord 搜索词
     * @param curPage    第几页
     * @param pageSize   每页多少条
     * @return
     */
    @Override
    public List<AggDatabaseVO> aggByDatabase(String searchWord, int curPage, int pageSize) {
        return null;
    }
}
