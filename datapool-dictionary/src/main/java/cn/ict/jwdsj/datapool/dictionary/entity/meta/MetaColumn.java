package cn.ict.jwdsj.datapool.dictionary.entity.meta;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "columns", catalog = "information_schema")
@IdClass(MetaColumnMultiKeys.class)
public class MetaColumn {
    @Id
    @Column(name = "TABLE_SCHEMA")
    private String database;

    @Id
    @Column(name = "TABLE_NAME")
    private String table;

    @Id
    @Column(name = "COLUMN_NAME")
    private String column;
}
