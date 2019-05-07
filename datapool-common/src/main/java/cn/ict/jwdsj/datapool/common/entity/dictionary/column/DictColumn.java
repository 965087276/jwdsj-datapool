package cn.ict.jwdsj.datapool.common.entity.dictionary.column;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "dict_column")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DictColumn extends BaseEntity {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "database_id")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictDatabase dictDatabase;
    @Column(name = "database_id")
    private long dictDatabaseId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "table_id")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictTable dictTable;
    @Column(name = "table_id")
    private long dictTableId;

    @Column(name = "en_database")
    private String enDatabase;

    @Column(name = "en_table")
    private String enTable;

    @Column(name = "en_column")
    private String enColumn;

    @Column(name = "ch_column")
    private String chColumn;

    public static DictColumn builtById(long id) {
        DictColumn dictColumn = new DictColumn();
        dictColumn.setId(id);
        return dictColumn;
    }

}
