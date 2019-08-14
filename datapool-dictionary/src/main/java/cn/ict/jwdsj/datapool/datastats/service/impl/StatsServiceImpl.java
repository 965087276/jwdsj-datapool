package cn.ict.jwdsj.datapool.datastats.service.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.datastats.dao.mapper.secondary.StatsMapper;
import cn.ict.jwdsj.datapool.datastats.service.StatsService;
import cn.ict.jwdsj.datapool.dictionary.service.table.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private StatsMapper statsMapper;

    @Override
    public long countTableRecords(long tableId) {
        DictTable dictTable = dictTableService.findById(tableId);
        String database = dictTable.getDictDatabase().getEnDatabase();
        String table = dictTable.getEnTable();
        return statsMapper.countTableRecords(database, table);
    }

    /**
     * 返回某个字段的20条数据
     *
     * @param database 所在库
     * @param table    所在表
     * @param column   字段名
     * @return
     */
    @Override
    public List<String> getColumnData(String database, String table, String column) {
        return statsMapper.getColumnData(database, table, column);
    }


    /**
     * 获取表的创建日期
     *
     * @param tableId 表id
     * @return
     */
    @Override
    public LocalDate getTableCreateTime(long tableId) {
        DictTable table = dictTableService.findById(tableId);

        String enTable = table.getEnTable();
        String enDatabase = table.getEnDatabase();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDate.parse((String) statsMapper.getTableStatus(enDatabase, enTable).get("Create_time"), df);

    }

    /**
     * 返回某个表的400条数据
     *
     * @param database 所在库
     * @param table    表名
     * @return
     */
    @Override
    public List<Map<String, Object>> getTableData(String database, String table) {
        return statsMapper.getTableData(database, table);
    }

}
