package cn.ict.jwdsj.datapool.common.entity.indexmanage;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "mapping_column")
@Data
public class MappingColumn extends BaseEntity {

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "table_id")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private DictTable dictTable;\

    @Column(name = "table_id")
    private long dictTableId;

    @Column(name = "column_id")
//    @NotFound(action = NotFoundAction.IGNORE)
    private long dictColumnId;

    @Column(name = "es_column", nullable = false)
    private String esColumn;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_searched", nullable = false)
    private boolean searched = false;

    @Column(name = "is_analyzed", nullable = false)
    private boolean analyzed = false;

    @Column(name = "is_displayed", nullable = false)
    private boolean displayed = true;

    @Column(name = "boost", nullable = false)
    private double boost = 1;

}
