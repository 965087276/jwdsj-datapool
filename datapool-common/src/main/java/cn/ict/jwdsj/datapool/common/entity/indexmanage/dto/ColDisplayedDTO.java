package cn.ict.jwdsj.datapool.common.entity.indexmanage.dto;

import lombok.Data;

@Data
public class ColDisplayedDTO {
    private long dictColumnId;
    private String esColumn;
    private boolean searched;
    private float boost;
}
