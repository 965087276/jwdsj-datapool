package cn.ict.jwdsj.datapool.datasync.fullread.service;

import cn.ict.jwdsj.datapool.common.entity.datasync.TableSyncMsg;

import java.sql.SQLException;

public interface TableFullReadService {
    void fullRead(TableSyncMsg msg) throws InterruptedException, SQLException;
}
