package cn.ict.jwdsj.datapool.datastat.service;

public interface ManualService {
    /**
     * 手动更新
     */
    void manualSync();

    /**
     * 手动库更新
     */
    void manualSyncDatabase();

    /**
     * 手动表更新
     */
    void manualSyncTable();

    /**
     * 手动字段更新
     */
    void manualSyncColumn();
}
