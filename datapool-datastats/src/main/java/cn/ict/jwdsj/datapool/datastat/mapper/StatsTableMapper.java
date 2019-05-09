package cn.ict.jwdsj.datapool.datastat.mapper;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface StatsTableMapper {

    List<StatsTable> listTablesNotAdd();

}
