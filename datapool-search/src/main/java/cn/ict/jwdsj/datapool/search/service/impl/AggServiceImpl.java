package cn.ict.jwdsj.datapool.search.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabasePageVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggDatabaseVO;
import cn.ict.jwdsj.datapool.search.entity.vo.AggTableVO;
import cn.ict.jwdsj.datapool.search.service.AggService;
import cn.ict.jwdsj.datapool.search.service.BaseSearch;
import cn.ict.jwdsj.datapool.search.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
public class AggServiceImpl extends BaseSearch implements AggService {


    /**
     * 某库下的表的聚合
     *
     * @param databaseId 数据库id
     * @param searchWord 搜索词
     * @return
     */
    @Override
    @Cacheable(value = "库下表聚合", key = "'databaseId: ' + #databaseId + ', searchWord: ' + #searchWord")
    public List<AggTableVO> aggByTable(long databaseId, String searchWord) throws IOException {
        SearchRequest request = new SearchRequest(indexPrefix + "*");
        searchWord = wordRegular(searchWord);

        // 使用query_string方式查询搜索词
        QueryStringQueryBuilder queryStringQuery = buildQueryStringBuilder(searchWord, defaultSearchFields);

        // 使用term_query过滤出相应数据库的数据
        TermQueryBuilder termQuery = QueryBuilders.termQuery("elastic_database_id", databaseId);

        // 前两个查询的汇总
        QueryBuilder query = QueryBuilders.boolQuery()
                .filter(termQuery)
                .must(queryStringQuery);

        // 搜索结果按表分类聚合（都有哪些表，每个表命中多少条数据）
        TermsAggregationBuilder tableGroupByAgg = AggregationBuilders
                .terms("数据表分类")
                .field("elastic_table_id")
                .size(maxTableAgg);

        // queryDSL请求体构造
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .size(0)
                .query(query)
                .aggregation(tableGroupByAgg);
        request.source(sourceBuilder);

        // 返回结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 如果结果为空，直接返回
        if (response.getHits().totalHits == 0L) {
            return new ArrayList<>();
        }

        // 命中的数据表的聚合（存的是数据库id）
        Terms tableGroupBy = (Terms) response.getAggregations().get("数据表分类");

        // 数据表id列表
        List<Long> tableIds = tableGroupBy.getBuckets()
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());

        // 每个数据表的命中数（id -> hit)
        Map<Long, Long> tableHits = tableGroupBy.getBuckets()
                .stream()
                .collect(Collectors.toMap(bucket -> bucket.getKeyAsNumber().longValue(), Terms.Bucket::getDocCount));

        // 数据表信息(id -> dto)
        Map<Long, TableNameDTO> tableNameDTOMap = dictService.listTableNameDTOByIdIn(tableIds)
                .stream()
                .collect(Collectors.toMap(TableNameDTO::getTableId, tb -> tb));

        List<AggTableVO> aggTables = tableIds
                .stream()
                .map(id -> AggTableVO.builder()
                        .tableId(id)
                        .enTable(tableNameDTOMap.get(id).getEnTable())
                        .chTable(tableNameDTOMap.get(id).getChTable())
                        .totalHit(tableHits.get(id))
                        .build()
                )
                .collect(Collectors.toList());
        return aggTables;
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
    @Cacheable(value = "库聚合", key = "'curPage: ' + #curPage + 'pageSize: ' + #pageSize + ', searchWord: ' + #searchWord")
    public AggDatabasePageVO aggByDatabase(String searchWord, int curPage, int pageSize) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        long startTime = System.currentTimeMillis();
        searchWord = wordRegular(searchWord);
        Assert.isTrue(StrUtil.isNotBlank(searchWord), "搜索词无效");

        log.info("search word is {}", searchWord);

        SearchRequest request = new SearchRequest(indexPrefix + "*");

        // 使用query_string查询方式
        QueryStringQueryBuilder queryStringQuery = buildQueryStringBuilder(searchWord, defaultSearchFields);

        // 命中多少个库
        CardinalityAggregationBuilder databaseCountAgg = AggregationBuilders
                .cardinality("命中数据库个数").field("elastic_database_id");
        // 库的分类统计（都命中哪些库，每个库下多少数据）
        TermsAggregationBuilder databaseGroupByAgg = AggregationBuilders
                .terms("数据库分类").field("elastic_database_id").size(curPage * pageSize);

        // queryDSL请求体构造
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(queryStringQuery)
                .size(0) // 因为是聚合，所以不返回查询结果
                .aggregation(databaseCountAgg)
                .aggregation(databaseGroupByAgg);
        request.source(sourceBuilder);

        // 返回结果
        AggDatabasePageVO databasePageVO = new AggDatabasePageVO();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        log.info("response is {}", response);
        // 所有的聚合
        Map<String, Aggregation> aggMap = response.getAggregations().asMap();

        // 如果搜索结果为空，直接返回
        if (response.getHits().totalHits == 0L) {
            return new AggDatabasePageVO();
        }

        // 命中的总文档数
        databasePageVO.setDocHit(response.getHits().getTotalHits());

        // 命中的总数据库数
        Cardinality databaseCount = (Cardinality) aggMap.get("命中数据库个数");
        databasePageVO.setDatabaseHit(databaseCount.getValue());

        // 命中的数据库列表（存的是数据库id）
        Terms databaseGroupBy = (Terms) aggMap.get("数据库分类");
        List<? extends Terms.Bucket> buckets = databaseGroupBy.getBuckets() // 取出本页的数据库
                .subList((curPage - 1) * pageSize, Math.min(curPage * pageSize, (int) databaseCount.getValue()));

        // 数据库id列表
        List<Long> databaseIds = buckets
                .stream()
                .map(Terms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());

        // 每个数据库的命中数（id -> hit)
        Map<Long, Long> databaseHits = buckets
                .stream()
                .collect(Collectors.toMap(bucket -> bucket.getKeyAsNumber().longValue(), Terms.Bucket::getDocCount));

        // dict_databases信息
        Future<List<DictDatabase>> futureDictDatabases = dictService.listDatabasesByIds(databaseIds);

        // stat_databases信息
        Future<List<StatsDatabase>> futureStatsDatabases = statsService.listDatabasesByIds(databaseIds);

        // 异步任务返回
        Map<Long, DictDatabase> dictDatabaseMap = futureDictDatabases.get(5, TimeUnit.SECONDS)
                .stream()
                .collect(Collectors.toMap(DictDatabase::getId, db -> db));

        Map<Long, StatsDatabase> statsDatabaseMap = futureStatsDatabases.get(5, TimeUnit.SECONDS)
                .stream()
                .collect(Collectors.toMap(db -> db.getDatabaseId(), db -> db));

//        log.info("dictDatabaseMap is {}", dictDatabaseMap);
//        log.info("statsDatabaseMap is {}", statsDatabaseMap);

        List<AggDatabaseVO> aggDatabases = databaseIds
                .stream()
                .map(id -> AggDatabaseVO.builder()
                        .databaseId(id)
                        .chDatabase(dictDatabaseMap.get(id).getChDatabase())
                        .enDatabase(dictDatabaseMap.get(id).getEnDatabase())
                        .detail(dictDatabaseMap.get(id).getDetail())
                        .totalHit(databaseHits.get(id))
                        .updateDate(statsDatabaseMap.get(id).getUpdateDate())
                        .build()
                ).collect(Collectors.toList());

        databasePageVO.setAggDatabases(aggDatabases);

        long endTime = System.currentTimeMillis();
        // 耗时
        databasePageVO.setTook((endTime - startTime) + "ms");

        return databasePageVO;
    }


}
