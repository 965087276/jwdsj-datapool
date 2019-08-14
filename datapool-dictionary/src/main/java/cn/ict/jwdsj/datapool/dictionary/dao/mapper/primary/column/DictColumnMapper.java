package cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.column;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictColumnMapper extends BaseMapper {
    /**
     * 批量插入（忽略错误）
     * @param dictColumns
     */
    void insertIgnore(List<DictColumn> dictColumns);

    @Select("select id column_id, en_column, ch_column from dict_column where table_id = #{tableId}")
    List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId);
}
