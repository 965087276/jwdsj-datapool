package cn.ict.jwdsj.datapool.dictionary.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"chDatabase", "detail"})
public class DictDbExcelDTO {

    private String enDatabase;
    private String chDatabase;
    private String detail;

}
