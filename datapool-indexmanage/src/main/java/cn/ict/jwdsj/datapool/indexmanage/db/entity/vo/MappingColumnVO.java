package cn.ict.jwdsj.datapool.indexmanage.db.entity.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class MappingColumnVO {
    private long dictColumnId;

    private String enColumn;
    private String chColumn;

    private boolean searched;

    private boolean analyzed;

    private boolean displayed;

    private double boost;
}
