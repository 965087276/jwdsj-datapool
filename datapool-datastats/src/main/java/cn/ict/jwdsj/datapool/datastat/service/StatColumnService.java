package cn.ict.jwdsj.datapool.datastat.service;

import java.util.List;

public interface StatColumnService {
    /**
     * 计算并返回缺陷字段
     * @param tableId
     * @return
     */
    List<String> initAndListDefectedColumns(long tableId);
}
