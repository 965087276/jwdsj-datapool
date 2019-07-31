package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StatsColumnRepo extends JpaRepository<StatsColumn, Long> {

    List<StatsColumn> findByDictTableId(long dictTableId);

    @Transactional
    @Modifying
    @Query("update StatsColumn r set r.enColumn = :enColumn, r.chColumn = :chColumn where r.dictColumnId = :columnId")
    void updateColumnInfo(@Param("columnId") long columnId, @Param("enColumn") String enColumn, @Param("chColumn") String chColumn);
}
