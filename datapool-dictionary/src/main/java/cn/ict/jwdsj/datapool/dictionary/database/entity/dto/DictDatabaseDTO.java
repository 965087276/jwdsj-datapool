package cn.ict.jwdsj.datapool.dictionary.database.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DictDatabaseDTO {
    @NotEmpty
    private String enDatabase;
    @NotEmpty
    private String chDatabase;
    @NotEmpty
    private String detail;
}
