package cn.ict.jwdsj.datapool.dictionary.database.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 用于DictDatabase的更新
 */
@Data
public class UpdateDatabaseDTO {
    @Min(value = 1)
    private long databaseId;
    @NotBlank
    private String chDatabase;
    @NotBlank
    private String detail;
}
