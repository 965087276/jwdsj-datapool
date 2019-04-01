package cn.ict.jwdsj.datapool.common.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "gmt_create", nullable = false)
    private Date createdTime;

    @Column(name = "gmt_modified", nullable = false)
    private Date modifiedTime;

    @PrePersist
    protected void prePersist() {
        if (this.createdTime == null) {
            this.createdTime = new Date();
        }
        if (this.modifiedTime == null) {
            this.modifiedTime = new Date();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedTime = new Date();
    }

}