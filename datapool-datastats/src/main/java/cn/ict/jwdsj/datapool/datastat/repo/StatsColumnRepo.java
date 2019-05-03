package cn.ict.jwdsj.datapool.datastat.repo;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StatsColumnRepo extends JpaRepository<StatsColumn, Long> {

    @Transactional
    void deleteAllByDictTableId(long dictTableId);

    @Transactional
    void deleteAllByIdIn(List<Long> ids);

    List<StatsColumn> findByDictTableId(long dictTableId);

}
