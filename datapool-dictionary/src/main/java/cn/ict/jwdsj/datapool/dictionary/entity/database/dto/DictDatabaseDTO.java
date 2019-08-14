package cn.ict.jwdsj.datapool.dictionary.entity.database.dto;

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
