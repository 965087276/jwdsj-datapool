package cn.ict.jwdsj.datapool.datasync.fullread.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.ict.jwdsj.datapool.datasync.fullread.client.IndexManageClient;
import cn.ict.jwdsj.datapool.datasync.fullread.entity.TableSyncMsg;
import cn.ict.jwdsj.datapool.datasync.fullread.kafka.KafkaTableFullReadProducer;
import cn.ict.jwdsj.datapool.datasync.fullread.service.TableFullReadService;
import com.alibaba.fastjson.JSONObject;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TableFullReadServiceImpl implements TableFullReadService {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private IndexManageClient indexManageClient;
    @Autowired
    private KafkaTableFullReadProducer kafkaTableFullReadProducer;

    @Override
    @Async
    public void fullRead(TableSyncMsg msg) throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        @Cleanup Connection con = DriverManager.getConnection(url, username, password);

        // 需要入搜索引擎的字段
        List<String> columns = indexManageClient.listColumnNamesByTableId(msg.getTableId());

        String sql = getSQL(msg.getDatabaseName(), msg.getTableName(), columns);

        @Cleanup PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(Integer.MIN_VALUE);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);

        @Cleanup ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            JSONObject record = new JSONObject();
            for (String column : columns) {
                record.put(column, rs.getObject(column));
            }
            // 添加一些额外信息
            record.put("md5_id", getMD5(record));
            record.put("elastic_database_id", msg.getDatabaseId());
            record.put("elastic_table_id", msg.getTableId());
            record.put("elastic_index_name", msg.getIndexName());
            kafkaTableFullReadProducer.send(record.toJSONString());
        }





    }

    /**
     * 计算该记录md5值，用于确保消息唯一
     * @param data
     * @return
     */
    private String getMD5(JSONObject data) {
        StringBuffer sb = new StringBuffer();
        data.forEach((key, value) -> {
            sb.append(value == null || StrUtil.isBlank(value.toString()) ? "null" : value);
        });
        return SecureUtil.md5(sb.toString());
    }

    private String getSQL(String database, String table, List<String> columns) {
        String columnStr = columns.stream().map(s -> '`' + s + '`').collect(Collectors.joining(","));
        database = '`' + database + '`';
        table = '`' + table + '`';
        return String.format("select %s from %s.%s", columnStr, database, table);
    }
}
