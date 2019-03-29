package cn.ict.jwdsj.datapool.dictionary.service.meta;

import cn.ict.jwdsj.datapool.dictionary.entity.meta.MetaColumn;

import java.util.List;

public interface MetaColumnService {

    List<MetaColumn> listByDatabaseAndTable(String database, String table);

}
