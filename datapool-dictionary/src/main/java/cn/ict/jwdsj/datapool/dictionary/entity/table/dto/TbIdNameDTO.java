package cn.ict.jwdsj.datapool.dictionary.entity.table.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TbIdNameDTO {
    private long id;
    private String enTable;
}
