package cn.ict.jwdsj.datapool.datastat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.entity.datastats.QStatsTable;
import cn.ict.jwdsj.datapool.common.entity.datastats.StatsTable;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.datastat.mapper.StatsMapper;
import cn.ict.jwdsj.datapool.datastat.mapper.StatsTableMapper;
import cn.ict.jwdsj.datapool.datastat.repo.StatsTableRepo;
import cn.ict.jwdsj.datapool.datastat.service.StatsTableService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsTableServiceImpl implements StatsTableService {

    @Autowired
    private StatsTableRepo statsTableRepo;
    @Autowired
    private StatsTableMapper statsTableMapper;

    @Override
    public void save(StatsTable statsTable) {
        statsTableRepo.save(statsTable);
    }

    @Override
    public void saveAll(List<StatsTable> statsTables) {
        statsTableRepo.saveAll(statsTables);
    }

    @Override
    public void update(StatsTable statsTable) {
        statsTableRepo.save(statsTable);
    }

    @Override
    public StatsTable findByDictTableId(long dictTableId) {
        return statsTableRepo.findByDictTableId(dictTableId);
    }

    @Override
    public List<StatsTable> listAll() {
        return statsTableRepo.findAll();
    }

    /**
     * 获取某个库下所有表的最新更新日期（这个日期作为库的更新日期）
     * @param dictDatabaseId 库id
     * @return
     */
    @Override
    public LocalDate getDatabaseUpdateDate(long dictDatabaseId) {
        LocalDate date = statsTableMapper.getDatabaseUpdateDate(dictDatabaseId);
        return date != null ? date : LocalDate.of(2019, 1, 1);
    }

    /**
     * 获取某个库下的表的数目
     *
     * @param dictDatabaseId
     * @return
     */
    @Override
    public int countTablesByDatabaseId(long dictDatabaseId) {
        return statsTableMapper.countTablesByDatabaseId(dictDatabaseId);
    }

    /**
     * 获取某个库的记录数
     *
     * @param dictDatabaseId 库id
     * @return
     */
    @Override
    public long countDatabaseRecords(long dictDatabaseId) {
        return statsTableMapper.countDatabaseRecords(dictDatabaseId);
    }


    /**
     * 表信息列表
     *
     * @param curPage    第几页
     * @param pageSize   每页多少条
     * @param databaseId 库的dict_id
     * @param nameLike   表名搜索
     * @return
     */
    @Override
    public Page<StatsTable> listAll(int curPage, int pageSize, long databaseId, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);
        QStatsTable statsTable = QStatsTable.statsTable;

        Predicate predicate = statsTable.dictDatabaseId.eq(databaseId);
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, statsTable.chTable.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, statsTable.enTable.like('%' + nameLike + '%'));

        return statsTableRepo.findAll(predicate, pageable);
    }

    /**
     * 更新表名信息
     *
     * @param tableId
     * @param enTable
     * @param chTable
     */
    @Override
    public void updateTableInfo(long tableId, String enTable, String chTable) {
        statsTableRepo.updateTableInfo(tableId, enTable, chTable);
    }

}
