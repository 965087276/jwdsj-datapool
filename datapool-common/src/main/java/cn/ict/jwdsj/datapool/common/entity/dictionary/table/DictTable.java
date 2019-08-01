package cn.ict.jwdsj.datapool.common.entity.dictionary.table;
import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import com.github.dozermapper.core.Mapping;
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

    @Column(name = "en_database")
    private String enDatabase;

    @Column(name = "en_table")
    private String enTable;

    @Column(name = "ch_table")
    private String chTable;

    @Column(name = "is_add_to_se")
    private boolean addToSe = false;

    @Mapping(value = "tableId", optional = true)
    public long getId() {
        return super.getId();
    }

}
