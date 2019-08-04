package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.datastat.mapper.StatsDatabaseMapper;
import cn.ict.jwdsj.datapool.datastat.repo.StatsDatabaseRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsDatabaseService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsDatabaseServiceImpl implements StatsDatabaseService {

    @Autowired
    private StatsDatabaseRepo statsDatabaseRepo;
    @Autowired
    private StatsDatabaseMapper statsDatabaseMapper;

    @Override
    public void save(StatsDatabase statsDatabase) {
        statsDatabaseRepo.save(statsDatabase);
    }

    @Override
    public void saveAll(List<StatsDatabase> statsDatabases) {
        statsDatabaseRepo.saveAll(statsDatabases);
    }

    @Override
    public void update(StatsDatabase statsDatabase) {
        statsDatabaseRepo.save(statsDatabase);
    }

    @Override
    public void deleteByDatabaseId(long databaseId) {
        statsDatabaseRepo.deleteByDatabaseId(databaseId);
    }

    @Override
    public List<StatsDatabase> listAll() {
        return statsDatabaseRepo.findAll();
    }

    @Override
    public List<StatsDatabase> listByDatabaseIds(String ids) {

        List<Long> databaseIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return statsDatabaseRepo.findByDatabaseIdIn(databaseIds);
    }

    /**
     * 库统计列表
     *
     * @param curPage  第几页
     * @param pageSize 每页多少条
     * @param nameLike 库名搜索
     * @return
     */
    @Override
    public Page<StatsDatabase> listAll(int curPage, int pageSize, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);

        QStatsDatabase statsDatabase = QStatsDatabase.statsDatabase;
        Predicate predicate = statsDatabase.isNotNull().or(statsDatabase.isNull());
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, statsDatabase.chDatabase.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, statsDatabase.enDatabase.like('%' + nameLike + '%'));

        return statsDatabaseRepo.findAll(predicate, pageable);
    }

    /**
     * 更新库的中英文名
     *
     * @param databaseId
     * @param enDatabase
     * @param chDatabase
     */
    @Override
    public void updateDatabaseInfo(long databaseId, String enDatabase, String chDatabase) {
        statsDatabaseRepo.updateDatabasesInfo(databaseId, enDatabase, chDatabase);
    }

    /**
     * 批量存入dict_database表新增的数据
     *
     * @param currentTime 插入时的时间
     */
    @Override
    public void saveAllFromDictDatabase(String currentTime) {
        statsDatabaseMapper.insertAll(currentTime);
    }
}
