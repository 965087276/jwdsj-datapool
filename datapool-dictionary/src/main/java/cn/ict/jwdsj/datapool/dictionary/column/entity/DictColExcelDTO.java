package cn.ict.jwdsj.datapool.dictionary.column.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"chColumn"})
public class DictColExcelDTO {
    private String enDatabase;
    private String enTable;
    private String enColumn;
    private String chColumn;
}
