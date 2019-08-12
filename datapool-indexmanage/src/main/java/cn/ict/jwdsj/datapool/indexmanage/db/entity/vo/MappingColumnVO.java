package cn.ict.jwdsj.datapool.indexmanage.db.entity.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class MappingColumnVO {
    private long columnId;

    private String enColumn;
    private String chColumn;

    private boolean searched = false;

    private boolean analyzed = false;

    private boolean displayed = false;

    private double boost = 1.0;
}
