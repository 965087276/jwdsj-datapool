package cn.ict.jwdsj.datapool.dictionary.database.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class DictDatabaseDTO {
    @NotBlank
    private String enDatabase;
    @NotBlank
    private String chDatabase;
    @NotBlank
    private String detail;
}
