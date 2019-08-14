package cn.ict.jwdsj.datapool.dictionary.entity.column.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(exclude = {"chColumn"})
public class DictColumnAddDTO {
    @NotBlank
    private String enColumn;
    @NotBlank
    private String chColumn;
}
