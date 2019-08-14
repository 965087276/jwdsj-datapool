package cn.ict.jwdsj.datapool.dictionary.entity.table.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"chTable"})
public class DictTbExcelDTO {
    private String enDatabase;
    private String enTable;
    private String chTable;
}
