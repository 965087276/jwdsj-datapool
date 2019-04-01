package cn.ict.jwdsj.datapool.dictionary.meta.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "COLUMNS", catalog = "INFORMATION_SCHEMA")
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
