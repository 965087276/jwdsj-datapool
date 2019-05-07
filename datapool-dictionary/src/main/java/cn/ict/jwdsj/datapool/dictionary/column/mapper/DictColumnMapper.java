package cn.ict.jwdsj.datapool.dictionary.column.mapper;

import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DictColumnMapper {
    void insertIgnore(List<DictColumn> dictColumns);
}
