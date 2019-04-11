package cn.ict.jwdsj.datapool.common.entity.indexmanage;

import cn.ict.jwdsj.datapool.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 索引信息表
 */
@Entity
@Table(name = "es_index")
@Data
public class EsIndex extends BaseEntity {
    @Column(name = "index_name", nullable = false)
    private String indexName;

    @Column(name = "num_shards", nullable = false)
    private Integer numShards;

    public static EsIndex builtById(long id) {
        EsIndex esIndex = new EsIndex();
        esIndex.setId(id);
        return esIndex;
    }
}
