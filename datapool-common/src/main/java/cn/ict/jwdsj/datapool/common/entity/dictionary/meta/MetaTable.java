package cn.ict.jwdsj.datapool.common.entity.dictionary.meta;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "TABLES", catalog = "INFORMATION_SCHEMA")
@IdClass(MetaTableMultiKeys.class)
public class MetaTable {
    @Id
    @Column(name = "TABLE_SCHEMA")
    private String database;

    @Id
    @Column(name = "TABLE_NAME")
    private String table;
}
