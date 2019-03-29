package cn.ict.jwdsj.datapool.dictionary.entity.dict;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dict_database")
@Data
public class DictDatabase extends BaseEntity {
    @Column(name = "en_database", nullable = false)
    private String enDatabase;

    @Column(name = "ch_database", nullable = false)
    private String chDatabase;

    @Column(name = "detail", nullable = false)
    private String detail;
}
