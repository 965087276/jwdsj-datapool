package cn.ict.jwdsj.datapool.common.entity.dictionary.column;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
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
