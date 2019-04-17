package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.datastat.repo.StatsColumnRepo;
import cn.ict.jwdsj.datapool.datastat.service.DictClient;
import cn.ict.jwdsj.datapool.datastat.service.StatsColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StatsColumnServiceImpl implements StatsColumnService {
    @Autowired
    private StatsColumnRepo statsColumnRepo;
    @Autowired
    private DictClient dictClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    @Override
    public List<String> initAndListDefectedColumns(long tableId) {
        DictTable dictTable = dictClient.findDictTableById(tableId);
        DictDatabase dictDatabase = dictClient.findDictDatabaseBy(dictTable.getDictDatabase().getId());
        String database = "`" + dictDatabase.getEnDatabase() + "`";
        String table = "`" + dictTable.getEnTable() + "`";

        String sql = "select * from " + database + "." + table + " limit 400";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if (list.size() == 0) return null;

        return list.get(0).keySet().stream()
                .filter(column -> this.isDefectedColumn(column, list)) // 过滤出缺陷字段
                .collect(Collectors.toList());

    }

    /**
     * 判断表的某个字段是否为缺陷字段
     * 缺陷字段定义为90%以上的记录中该字段都为空
     * @param column 字段名
     * @param list 表的前300条数据
     * @return
     */
    private boolean isDefectedColumn(String column, List<Map<String, Object>> list) {
        double total = list.size();
        double defect = 0;
        for (Map<String, Object> record : list) {
            Object value = record.get(column);
            if (Objects.isNull(value) || StrUtil.isBlankIfStr(value) || "null".equals(value.toString().trim().toLowerCase())) {
                ++defect;
            }
        }
        return defect / total >= 0.90;
    }
}
