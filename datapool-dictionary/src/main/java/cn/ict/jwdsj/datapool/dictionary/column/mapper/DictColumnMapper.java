package cn.ict.jwdsj.datapool.dictionary.column.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
}
