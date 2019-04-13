package cn.ict.jwdsj.datapool.datasync.fullread.service;

import cn.ict.jwdsj.datapool.datasync.fullread.entity.TableSyncMsg;

import java.sql.SQLException;

public interface TableFullReadService {
    void fullRead(TableSyncMsg msg) throws InterruptedException, SQLException;
}
