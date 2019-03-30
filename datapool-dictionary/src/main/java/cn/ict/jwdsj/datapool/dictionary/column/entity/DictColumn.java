package cn.ict.jwdsj.datapool.dictionary.column.entity;

import cn.ict.jwdsj.datapool.dictionary.BaseEntity;
import cn.ict.jwdsj.datapool.dictionary.table.entity.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "dict_column")
@Data
public class DictColumn extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @Column(name = "en_column")
    private String enColumn;

    @Column(name = "ch_column")
    private String chColumn;

}
