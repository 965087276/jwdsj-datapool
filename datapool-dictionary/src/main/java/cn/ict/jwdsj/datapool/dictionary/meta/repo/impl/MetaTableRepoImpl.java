package cn.ict.jwdsj.datapool.dictionary.meta.repo.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MetaTableRepoImpl implements MetaTableRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MetaTable> findByDatabase(String database) {
        String sql = "show tables from " + "`" + database + "`";
        return jdbcTemplate.queryForList(sql, String.class)
                .stream()
                .map(table -> new MetaTable(database, table))
                .collect(Collectors.toList());
    }
}
