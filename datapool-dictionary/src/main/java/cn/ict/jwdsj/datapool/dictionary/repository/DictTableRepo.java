package cn.ict.jwdsj.datapool.dictionary.repository;

import cn.ict.jwdsj.datapool.dictionary.entity.DictTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictTableRepo extends JpaRepository<DictTable, Long> {
}
