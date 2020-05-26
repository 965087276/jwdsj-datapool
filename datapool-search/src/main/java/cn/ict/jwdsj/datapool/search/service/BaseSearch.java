package cn.ict.jwdsj.datapool.search.service;

import cn.hutool.core.util.StrUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class BaseSearch {

    @Autowired
    protected RestHighLevelClient client;

    @Autowired
    protected DictService dictService;

    @Autowired
    protected StatsService statsService;

    // 索引的公共前缀，使用indexPrefix + "*"进行全索引的搜索
    @Value("${elasticsearch.index-prefix}")
    protected String indexPrefix;

    // 索引别名的前缀，该前缀加上表id即可制定到具体表
    @Value("${elasticsearch.alias-prefix}")
    protected String aliasPrefix;

    // 表聚合的最大数目
    @Value("${elasticsearch.max-table-agg}")
    protected int maxTableAgg;

    protected Map<String, Float> defaultSearchFields;

    /**
     * query_string查询构建
     * @param searchWord 搜索词
     * @param fields 搜索字段及其权重
     * @return
     */
    protected QueryStringQueryBuilder buildQueryStringBuilder(String searchWord, Map<String, Float> fields) {
        QueryStringQueryBuilder queryBuilder = QueryBuilders
                .queryStringQuery(searchWord)
                .defaultOperator(Operator.AND)
                .fields(fields)
                .fuzzyTranspositions(false);
        return queryBuilder;
    }

    @PostConstruct
    public void init() {
        defaultSearchFields = new HashMap<>();
        defaultSearchFields.put("all_fields_text", 1.0f);
        defaultSearchFields.put("all_fields_keyword", 1.0f);
    }

    /**
     * 规范化搜索词
     * @param word
     * @return
     */
    protected String wordRegular(String word) {
        word = word.replaceAll("[[\\p{P}\\p{S}]&&[^_]]"," ");
        String[] words = StrUtil.trimToEmpty(word).split("\\s+");;
        return Arrays.stream(words).collect(Collectors.joining(" AND "));
    }


}
