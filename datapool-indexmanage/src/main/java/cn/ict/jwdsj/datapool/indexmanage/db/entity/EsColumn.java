package cn.ict.jwdsj.datapool.indexmanage.db.entity;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "es_column")
@Data
public class EsColumn extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "index_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private EsIndex esIndex;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

}
