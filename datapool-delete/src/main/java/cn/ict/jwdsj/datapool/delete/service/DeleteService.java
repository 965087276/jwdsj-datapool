package cn.ict.jwdsj.datapool.delete.service;

import java.io.IOException;

public interface DeleteService {
    void scheduledDelete() throws IOException;

    void deleteByDatabaseId(long databaseId) throws IOException;

    void deleteByTableId(long tableId) throws IOException;

}
