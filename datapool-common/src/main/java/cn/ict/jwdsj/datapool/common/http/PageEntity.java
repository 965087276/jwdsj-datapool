package cn.ict.jwdsj.datapool.common.http;

import lombok.Data;

import java.util.List;

@Data
public class PageEntity<T> {
    private long totalCount;
    List<T> contents;

    public static <T> PageEntity<T> build(long totalCount, List<T> contents) {
        PageEntity<T> pageEntity = new PageEntity<>();
        pageEntity.setTotalCount(totalCount);
        pageEntity.setContents(contents);
        return pageEntity;
    }

}
