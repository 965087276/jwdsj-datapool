package cn.ict.jwdsj.datapool.dictionary.meta.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "SCHEMATA", catalog = "INFORMATION_SCHEMA")
public class MetaDatabase {

    @Id
    @Column(name = "SCHEMA_NAME")
    private String database;
}
