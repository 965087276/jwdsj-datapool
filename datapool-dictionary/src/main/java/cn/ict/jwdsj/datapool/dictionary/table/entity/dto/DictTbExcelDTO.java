package cn.ict.jwdsj.datapool.dictionary.table.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"chTable"})
public class DictTbExcelDTO {
    private String enDatabase;
    private String enTable;
    private String chTable;
}
