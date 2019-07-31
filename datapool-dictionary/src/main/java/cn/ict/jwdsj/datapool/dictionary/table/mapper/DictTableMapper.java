package cn.ict.jwdsj.datapool.dictionary.table.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictTableMapper {

    /**
     * 批量插入
     * @param dictTables
     */
    void insertIgnore(List<DictTable> dictTables);

    @Select("select id from dict_table")
    List<Long> listTableId();
}
