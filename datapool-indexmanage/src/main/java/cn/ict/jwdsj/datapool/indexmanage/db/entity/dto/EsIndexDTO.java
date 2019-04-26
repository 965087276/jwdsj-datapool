package cn.ict.jwdsj.datapool.indexmanage.db.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class EsIndexDTO {

    @NotEmpty
    @Pattern(regexp = "^[a-z][a-z0-9_-]*$")
    private String indexName;

    @Range(min = 1, max = 8)
    private int numShards;

}
