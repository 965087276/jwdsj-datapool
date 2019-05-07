package cn.ict.jwdsj.datapool.common.entity.dictionary.meta;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class MetaTable {
    private String database;

    private String table;
}
