package cn.ict.jwdsj.datapool.search.service.impl;

import cn.hutool.json.JSONObject;
import cn.ict.jwdsj.datapool.search.service.TableSearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableSearchServiceImpl implements TableSearchService {

    @Autowired private RestHighLevelClient client;

    /**
     * 在某表下搜索
     *
     * @param databaseId 库id
     * @param tableId    表id
     * @param searchWord 搜索词
     * @param curPage    第几页
     * @param pageSize   每页多少条
     * @return
     */
    @Override
    public List<JSONObject> searchInTable(long databaseId, long tableId, String searchWord, int curPage, int pageSize) {
        return null;
    }
}
