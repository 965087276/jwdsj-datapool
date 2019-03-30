package cn.ict.jwdsj.datapool.dictionary.meta.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MetaTableMultiKeys implements Serializable{

    private String database;

    private String table;
}
