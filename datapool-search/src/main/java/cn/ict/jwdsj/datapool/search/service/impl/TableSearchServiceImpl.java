package cn.ict.jwdsj.datapool.search.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.indexmanage.MappingColumn;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
//    @Cacheable(value = "单表搜索", key = "'curPage: ' + #curPage + 'pageSize: ' + #pageSize + ', searchWord: ' + #searchWord + ', tableId: ' + #tableId")
    public SearchTableVO searchByTableId(long tableId, String searchWord, int curPage, int pageSize) throws IOException {

        searchWord = wordRegular(searchWord);

        long startTime = System.currentTimeMillis();

//        // 数据库中的字段id和字段名（异步请求）
//        Future<List<ColumnNameDTO>> dbColumnReq = dictService.listColumnNameDTOByTableId(tableId);

        // 需要展示的字段
        List<MappingColumn> columnsDisplayed = indexManageService.listMappingColumnByTableId(tableId);

        // 需要搜索的字段及其权重
        Map<String, Float> esColumnSearched = columnsDisplayed
                .stream()
                .filter(MappingColumn::isSearched)
                .collect(Collectors.toMap(MappingColumn::getEsColumn, MappingColumn::getBoost));

        SearchRequest request = new SearchRequest(aliasPrefix + tableId);

        // 查询主体
        QueryBuilder queryBuilder = buildQueryStringBuilder(searchWord, esColumnSearched);


        // 搜索结果高亮显示
        // keyword字段
        HighlightBuilder.Field highLightKeyword = new HighlightBuilder
                .Field("KEYWORD-*")
                .highlighterType("unified");
        // text字段
        HighlightBuilder.Field highlightText = new HighlightBuilder
                .Field("TEXT-*")
                .highlighterType("fvh");

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field(highLightKeyword)
                .field(highlightText);

        // queryDSL请求体构造
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .from((curPage - 1) * pageSize)
                .size(pageSize)
                .query(queryBuilder)
                .highlighter(highlightBuilder);
        request.source(sourceBuilder);
        log.info("request is {}", request);
        // 执行查询
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        log.info("response is {}", response);

        // es字段名与原始字段名映射
        Map<String, String> esColAndDbCol = columnsDisplayed
                .stream()
                .collect(Collectors.toMap(MappingColumn::getEsColumn, MappingColumn::getEnColumn));

        // 数据表中英字段映射
        Map<String, String> enColAndChCol = columnsDisplayed
                .stream()
                .collect(Collectors.toMap(MappingColumn::getEnColumn, MappingColumn::getChColumn));

        // 数据
        List<JSONObject> contents = Arrays.stream(response.getHits().getHits())
            .map(searchHit -> this.getOriginAndHighlightFields(searchHit, esColAndDbCol))
            .collect(Collectors.toList());

        SearchTableVO vo = new SearchTableVO();
        // 命中结果数
        vo.setTotalHit(response.getHits().totalHits);
        vo.setContents(contents);
        vo.setFields(enColAndChCol);

        long endTime = System.currentTimeMillis();

        vo.setTook((endTime - startTime) + "ms");

        return vo;
    }

    /**
     * 完成es字段到原始表字段的映射以及字段的高亮展示
     * @param documentFields 原始记录
     * @param esColAndDbCol 字段映射map
     * @return
     */
    private JSONObject getOriginAndHighlightFields(SearchHit documentFields, Map<String, String> esColAndDbCol) {
        JSONObject doc = getOriginFields(documentFields, esColAndDbCol);

        // 用有高亮内容字段值替换原来的字段值
        documentFields.getHighlightFields().forEach((esCol, highlightContent) -> {
            doc.replace(esColAndDbCol.get(esCol), highlightContent.fragments()[0].toString());
        });

        return doc;
    }

    /**
     * 完成es字段到原始表字段的映射
     * @param documentFields 搜索结果
     * @param esColAndDbCol 字段映射map
     * @return
     */
    private JSONObject getOriginFields(SearchHit documentFields, Map<String, String> esColAndDbCol) {
        JSONObject doc = new JSONObject();

        documentFields.getSourceAsMap().forEach((esCol, value) -> {
            String chName = esColAndDbCol.get(esCol);
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
