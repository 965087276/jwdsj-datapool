package cn.ict.jwdsj.datapool.dictionary.table.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictTableMapper {
    void insertIgnore(List<DictTable> dictTables);
}
