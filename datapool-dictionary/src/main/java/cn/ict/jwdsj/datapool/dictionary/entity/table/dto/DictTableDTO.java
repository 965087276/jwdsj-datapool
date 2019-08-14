package cn.ict.jwdsj.datapool.dictionary.entity.table.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(exclude = {"chTable"})
public class DictTableDTO {
    @NotBlank
    private String enTable;
    @NotBlank
    private String chTable;
}
