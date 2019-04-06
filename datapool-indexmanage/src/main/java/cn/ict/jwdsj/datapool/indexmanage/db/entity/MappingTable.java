package cn.ict.jwdsj.datapool.indexmanage.db.entity;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mapping_table")
@Data
public class MappingTable extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "table_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private DictTable dictTable;

    @ManyToOne
    @JoinColumn(name = "index_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private EsIndex esIndex;

}
