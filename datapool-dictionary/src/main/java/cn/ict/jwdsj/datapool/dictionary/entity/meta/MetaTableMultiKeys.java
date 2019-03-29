package cn.ict.jwdsj.datapool.dictionary.entity.meta;

import lombok.Data;

import java.io.Serializable;

@Data
public class MetaTableMultiKeys implements Serializable{

    private String database;

    private String table;
}
