package cn.ict.jwdsj.datapool.indexmanage.db.entity.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class MappingColumnDTO {
    @NotBlank
    private String enColumn;

    @NotBlank
    private String chColumn;

    @Min(value = 1)
    private long columnId;

    @NotNull
    private boolean searched;

    @NotNull
    private boolean analyzed;

    @NotNull
    private boolean displayed;

    @DecimalMin(value = "0.1", message = "权重不能小于0.1")
    @DecimalMax(value = "10.0", message = "权重不能超过10")
    private float boost;
}
