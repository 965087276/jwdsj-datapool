package cn.ict.jwdsj.datapool.dictionary.meta.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "schemata", catalog = "information_schema")
public class MetaDatabase {

    @Id
    @Column(name = "SCHEMA_NAME")
    private String database;
}
