package cn.ict.jwdsj.datapool.dictionary.meta.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tables", catalog = "information_schema")
@IdClass(MetaTableMultiKeys.class)
public class MetaTable {
    @Id
    @Column(name = "TABLE_SCHEMA")
    private String database;

    @Id
    @Column(name = "TABLE_NAME")
    private String table;
}
