package cn.ict.jwdsj.datapool.datasync.fullread.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.ict.jwdsj.datapool.common.dto.indexmanage.TableFullReadDTO;
import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.kafka.KafkaTableFullReadProducer;
import cn.ict.jwdsj.datapool.datasync.fullread.service.TableFullReadService;
import cn.ict.jwdsj.datapool.indexmanage.db.repo.MappingTableRepo;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingColumnService;
import cn.ict.jwdsj.datapool.indexmanage.db.service.MappingTableService;
import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TableFullReadServiceImpl implements TableFullReadService {

    @Value("${jwdb.driver-class-name}")
    private String driver;
    @Value("${jwdb.jdbc-url}")
    private String url;
    @Value("${jwdb.username}")
    private String username;
    @Value("${jwdb.password}")
    private String password;

    @Autowired
    private KafkaTableFullReadProducer kafkaTableFullReadProducer;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MappingColumnService mappingColumnService;
    @Autowired
    private MappingTableRepo mappingTableRepo;


    @Override
    @Async
    public void fullRead(TableSyncMsg msg) throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        @Cleanup Connection con = DriverManager.getConnection(url, username, password);
        TableFullReadDTO tableFullReadDTO = mappingColumnService.getTableFullReadDTOByTableId(msg.getTableId());
        // 需要入搜索引擎的字段
        List<String> columns = tableFullReadDTO.getColumns();
        // 表字段到索引字段的映射
        Map<String, String> colAndEsColMap = tableFullReadDTO.getColAndEsColMap();

        String sql = getSQL(msg.getEnDatabase(), msg.getEnTable(), columns);
        @Cleanup Connection con = jdbcTemplate.getDataSource().getConnection();
        @Cleanup PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(Integer.MIN_VALUE);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);

        @Cleanup ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            JSONObject record = new JSONObject();
            for (String column : columns) {
                String esColumn = colAndEsColMap.get(column);
                String value = StrUtil.toString(rs.getObject(column)).trim();
                // 规范化null值
                value = "null".equalsIgnoreCase(value) ? null : value;
                record.put(esColumn, value);
            }
            // 添加一些额外信息
            record.put("elastic_database_id", msg.getDatabaseId());
            record.put("elastic_table_id", msg.getTableId());
            record.put("elastic_index_name", msg.getIndexName());
            record.put("md5_id", getMD5(record));
            kafkaTableFullReadProducer.send(record.toJSONString());
        }
        // 数据更新完成后，将mapping_table表的updateDate字段更新
        mappingTableRepo.updateUpdateDate(msg.getTableId());
    }

    /**
     * 计算该记录md5值，用于确保消息唯一
     * @param data
     * @return
     */
    private String getMD5(JSONObject data) {
        StringBuffer sb = new StringBuffer();
        data.forEach((key, value) -> {
            sb.append(value == null || StrUtil.isBlank(value.toString()) ? "null" : value.toString().trim());
        });
        return SecureUtil.md5(sb.toString());
    }

    /**
     * 全量读取的SQL语句
     * @param database 库名
     * @param table 表名
     * @param columns 需要读取的字段
     * @return
     */
    private String getSQL(String database, String table, List<String> columns) {
        String columnStr = columns.stream().map(s -> '`' + s + '`').collect(Collectors.joining(","));
        database = '`' + database + '`';
        table = '`' + table + '`';
        return String.format("select %s from %s.%s", columnStr, database, table);
    }
}
