package cn.ict.jwdsj.datapool.common.entity.dictionary.meta;

import lombok.Data;

import java.io.Serializable;

@Data
public class MetaTableMultiKeys implements Serializable{

    private String database;

    private String table;
}
