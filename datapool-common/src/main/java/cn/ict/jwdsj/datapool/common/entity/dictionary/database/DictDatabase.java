package cn.ict.jwdsj.datapool.common.entity.dictionary.database;


import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import com.github.dozermapper.core.Mapping;
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

    @Column(name = "detail", nullable = true)
    private String detail;

    public static DictDatabase buildById(long id) {
        DictDatabase dictDatabase = new DictDatabase();
        dictDatabase.setId(id);
        return dictDatabase;
    }

    @Mapping(value = "databaseId", optional = true)
    public long getId() {
        return super.getId();
    }

}
