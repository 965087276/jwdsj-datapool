package cn.ict.jwdsj.datapool.dictionary.table.entity;
import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.dictionary.database.entity.DictDatabase;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "dict_table")
@Data
public class DictTable extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "database_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictDatabase dictDatabase;

    @Column(name = "en_table")
    private String enTable;

    @Column(name = "ch_table")
    private String chTable;

    public static DictTable builtById(long id) {
        DictTable dictTable = new DictTable();
        dictTable.setId(id);
        return dictTable;
    }

}
