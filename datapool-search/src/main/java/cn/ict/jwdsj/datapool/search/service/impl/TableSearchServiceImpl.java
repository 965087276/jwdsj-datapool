package cn.ict.jwdsj.datapool.search.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.dto.ColDisplayedDTO;
import cn.ict.jwdsj.datapool.search.entity.vo.SearchTableVO;
import cn.ict.jwdsj.datapool.search.service.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TableSearchServiceImpl extends BaseSearch implements TableSearchService {

    @Autowired
    private DictService dictService;
    @Autowired
    private IndexManageService indexManageService;

    /**
     * 在某表下搜索
     *
     * @param tableId    表id
     * @param searchWord 搜索词
     * @param curPage    第几页
     * @param pageSize   每页多少条
     * @return
     */
    @Override
    public SearchTableVO searchByTableId(long tableId, String searchWord, int curPage, int pageSize) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        searchWord = wordRegular(searchWord);

        long startTime = System.currentTimeMillis();

        // 数据库中的字段id和字段名（异步请求）
        Future<List<ColumnNameDTO>> dbColumnReq = dictService.listColumnNameDTOByTableId(tableId);

        // 需要展示的字段
        List<ColDisplayedDTO> esColumnDisplayed = indexManageService.listColDisplayedDTOByTableId(tableId);

        log.info("esColDisplayed is {}", esColumnDisplayed);

        // 需要搜索的字段及其权重
        Map<String, Float> esColumnSearched = esColumnDisplayed
                .stream()
                .filter(ColDisplayedDTO::isSearched)
                .collect(Collectors.toMap(ColDisplayedDTO::getEsColumn, ColDisplayedDTO::getBoost));

        SearchRequest request = new SearchRequest(aliasPrefix + tableId);

        // 查询主体
        QueryBuilder queryBuilder = buildQueryStringBuilder(searchWord, esColumnSearched);
        // 搜索结果高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("*")
                .highlighterType("fvh");

        // queryDSL请求体构造
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .from((curPage - 1) * pageSize)
                .size(pageSize)
                .query(queryBuilder)
                .highlighter(highlightBuilder);

        // 执行查询
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        log.info("response is {}", response);

        // 数据库中的字段id和字段名
        Map<Long, String> dbColIdAndDbColNames = dbColumnReq.get(5, TimeUnit.SECONDS)
                .stream()
                .collect(Collectors.toMap(ColumnNameDTO::getColumnId, ColumnNameDTO::getChColumn));

        // es字段名与表中文字段名映射
        Map<String, String> esColAndChCol = esColumnDisplayed
                .stream()
                .collect(Collectors.toMap(k -> k.getEsColumn(), k -> dbColIdAndDbColNames.get(k.getDictColumnId())));

        // 数据
        List<JSONObject> contents = Arrays.stream(response.getHits().getHits())
            .map(searchHit -> this.getChineseAndHighlightFields(searchHit, esColAndChCol))
            .collect(Collectors.toList());

        SearchTableVO vo = new SearchTableVO();
        // 命中结果数
        vo.setTotalHit(response.getHits().totalHits);
        vo.setContents(contents);

        long endTime = System.currentTimeMillis();

        vo.setTook((endTime - startTime) + "ms");

        return vo;
    }

    /**
     * 完成es字段到中文字段的映射以及字段的高亮展示
     * @param documentFields 原始记录
     * @param esColAndChCol 字段映射map
     * @return
     */
    private JSONObject getChineseAndHighlightFields(SearchHit documentFields, Map<String, String> esColAndChCol) {
        JSONObject doc = getChineseFields(documentFields, esColAndChCol);

        // 用有高亮内容字段值替换原来的字段值
        documentFields.getHighlightFields().forEach((esCol, highlightContent) -> {
            doc.replace(esColAndChCol.get(esCol), highlightContent.getFragments()[0]);
        });

        return doc;
    }

    /**
     * 完成es字段到中文字段的映射
     * @param documentFields 原始记录
     * @param esColAndChCol 字段映射map
     * @return
     */
    private JSONObject getChineseFields(SearchHit documentFields, Map<String, String> esColAndChCol) {
        JSONObject doc = new JSONObject();

        documentFields.getSourceAsMap().forEach((esCol, value) -> {
            String chName = esColAndChCol.get(esCol);
            if (StrUtil.isNotBlank(chName))
                doc.put(chName, value);
        });
        return doc;
    }

    @Override
    public SearchTableVO searchByEnDatabaseAndEnTable(String enDatabase, String enTable, String searchWord, int curPage, int pageSize) {
        return null;
    }
}
