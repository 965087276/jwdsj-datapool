package cn.ict.jwdsj.datapool.dictionary.entity.table.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 用于DictTable的更新
 */
@Data
public class UpdateTableDTO {
    @Min(value = 1)
    private long tableId;
    @NotBlank
    private String chTable;
}
