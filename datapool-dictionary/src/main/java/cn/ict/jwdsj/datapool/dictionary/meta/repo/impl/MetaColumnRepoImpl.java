package cn.ict.jwdsj.datapool.dictionary.meta.repo.impl;

import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.meta.repo.MetaColumnRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MetaColumnRepoImpl implements MetaColumnRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MetaColumn> findByDatabaseAndTable(String database, String table) {
        return jdbcTemplate.queryForList(showColumnsSQL(database, table))
                .stream()
                .map(row -> new MetaColumn(database, table, (String) row.get("Field"), (String) row.get("Comment")))
                .collect(Collectors.toList());
    }

    @Override
    public List<MetaColumn> findByDatabaseAndTableIn(String database, List<String> tables) {
        return tables
                .stream()
                .flatMap(table -> this.findByDatabaseAndTable(database, table).stream())
                .collect(Collectors.toList());

    }

    private String showColumnsSQL(String database, String table) {
        return "show full columns from `" + database + "`." + "`" + table + "`";
    }
}
