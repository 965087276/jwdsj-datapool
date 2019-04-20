package cn.ict.jwdsj.datapool.dictionary.table.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 用于DictColumn的更新
 */
@Data
public class UpdateColumnDTO {
    @Min(value = 1)
    private long columnId;
    @NotBlank
    private String chColumn;
}
