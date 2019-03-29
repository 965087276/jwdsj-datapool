package cn.ict.jwdsj.datapool.dictionary.repository.dict;

import cn.ict.jwdsj.datapool.dictionary.entity.dict.DictColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictColumnRepo extends JpaRepository<DictColumn, Long> {
}
