package cn.ict.jwdsj.datapool.indexmanage.db.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class MappingColumnDTO {
    private long dictColumnId;

    @NotNull
    private boolean searched;

    @NotNull
    private boolean analyzed;

    @NotNull
    private boolean displayed;

    @Range(min = 0, max = 10)
    private double boost;
}
