package cn.ict.jwdsj.datapool.indexmanage.db.entity.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class MappingColumnVO {
    private long dictColumnId;
    private String enColumn;
    private String chColumn;
    @NotNull
    private boolean searched;

    @NotNull
    private boolean analyzed;

    @NotNull
    private boolean displayed;

    @Range(min = 0, max = 10)
    private double boost;
}
