package cn.ict.jwdsj.datapool.datastat.service;

import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StatsDatabaseService {

    void save(StatsDatabase statsDatabase);

    void saveAll(List<StatsDatabase> statsDatabases);

    void update(StatsDatabase statsDatabase);

    void deleteByDatabaseId(long databaseId);

    List<StatsDatabase> listAll();

    /**
     * 获取多个库的统计信息
     * @param ids 用逗号隔开的库id
     * @return
     */
    List<StatsDatabase> listByDatabaseIds(String ids);

    /**
     * 库统计列表
     * @param curPage 第几页
     * @param pageSize 每页多少条
     * @param nameLike 库名搜索
     * @return
     */
    Page<StatsDatabase> listAll(int curPage, int pageSize, String nameLike);

    /**
     * 更新库的中英文名
     * @param databaseId
     * @param enDatabase
     * @param chDatabase
     */
    void updateDatabaseInfo(long databaseId, String enDatabase, String chDatabase);

    /**
     * 批量存入dict_database表新增的数据
     * @param currentTime 插入时的时间
     */
    void saveAllFromDictDatabase(String currentTime);
}
