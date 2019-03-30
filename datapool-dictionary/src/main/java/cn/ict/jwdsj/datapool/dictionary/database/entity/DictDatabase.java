package cn.ict.jwdsj.datapool.dictionary.database.entity;


import cn.ict.jwdsj.datapool.dictionary.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Column(name = "detail", nullable = true)
    private String detail;

}
