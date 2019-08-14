package cn.ict.jwdsj.datapool.dictionary.service.database.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.dao.repo.table.DictTableRepo;
import cn.ict.jwdsj.datapool.dictionary.entity.database.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.dao.mapper.primary.database.DictDatabaseMapper;
import cn.ict.jwdsj.datapool.dictionary.dao.repo.database.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.service.database.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.entity.database.dto.UpdateDatabaseDTO;
import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;
import cn.ict.jwdsj.datapool.dictionary.service.table.DictTableService;
import com.github.dozermapper.core.Mapper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.constant.DictType.DATABASE;
import static cn.ict.jwdsj.datapool.common.constant.DictType.DATABASES;


@Service
public class DictDatabaseServiceImpl implements DictDatabaseService {

    @Autowired
    private DictDatabaseRepo dictDatabaseRepo;
    @Autowired
    private DictDatabaseMapper dictDatabaseMapper;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private ApplicationContext publisher;
    @Autowired
    private DictTableRepo dictTableRepo;
    @Autowired
    private Mapper mapper;
    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DictDatabase dictDatabase) {
        this.saveAllToDb(Arrays.asList(dictDatabase));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllToDb(List<DictDatabase> dictDatabases) {
        String currentTime = dictDatabaseMapper.getCurrentTimeStamp();
        dictDatabaseMapper.insertIgnore(dictDatabases);
        publisher.publishEvent(new DictAddEvent(currentTime, DATABASES));
    }

    @Override
    public boolean existsByEnDatabase(String enDatabase) {
        return dictDatabaseRepo.existsByEnDatabase(enDatabase);
    }

    @Override
    public Page<DictDatabaseVO> listVO(int curPage, int pageSize, String nameLike) {
        Pageable pageable = PageRequest.of(curPage-1, pageSize);

        QDictDatabase dictDatabase = QDictDatabase.dictDatabase;
        Predicate predicate = dictDatabase.isNotNull().or(dictDatabase.isNull());
        // 根据输入的待查询表名是中文还是英文来判断搜索哪个字段
        predicate = StrUtil.isBlank(nameLike) ? predicate : StrJudgeUtil.isContainChinese(nameLike) ?
                ExpressionUtils.and(predicate, dictDatabase.chDatabase.like('%' + nameLike + '%')) :
                ExpressionUtils.and(predicate, dictDatabase.enDatabase.like('%' + nameLike + '%'));
        return dictDatabaseRepo.findAll(predicate, pageable).map(this::convertToDictDatabaseVO);
    }

    /**
     * 库中英下拉框
     *
     * @return
     */
    @Override
    public List<DatabaseNameDTO> listDatabaseDropDownBox() {
        return dictDatabaseRepo.findAll()
                .stream()
                .map(this::convertToDatabaseNameDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(UpdateDatabaseDTO updateDatabaseDTO) {
        DictDatabase dictDatabase = dictDatabaseRepo.findById(updateDatabaseDTO.getDatabaseId()).get();
        dictDatabase.setChDatabase(updateDatabaseDTO.getChDatabase());
        dictDatabase.setDetail(updateDatabaseDTO.getDetail());
        dictDatabaseRepo.save(dictDatabase);
        // 发送变更信息
        kafkaSender.send(kafkaUpdateTopic, new DictUpdateMsg(DATABASE, updateDatabaseDTO.getDatabaseId()));
    }

    /**
     * 列出所有库的英文名
     *
     * @return
     */
    @Override
    public List<String> listEnDatabase() {
        return dictDatabaseMapper.listEnDatabase();
    }

    @Override
    public List<DictDatabase> listAll() {
        return dictDatabaseRepo.findAll();
    }

    /**
     * 删除库信息
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        DictDatabase dictDatabase = DictDatabase.buildById(id);
        // 该库下不能有表
        Assert.isTrue(!dictTableRepo.existsByDictDatabase(dictDatabase), "该库下还有表，请先删除表信息管理中该库的所有表");
        dictDatabaseRepo.deleteById(id);
        publisher.publishEvent(new DictDeleteEvent(id, DATABASE));
    }

    @Override
    public DictDatabase findById(long id) {
        return dictDatabaseRepo.findById(id).get();
    }

    @Override
    public DictDatabase findByEnDatabase(String enDatabase) {
        return dictDatabaseRepo.findByEnDatabase(enDatabase);
    }

    @Override
    public List<DictDatabase> listByIds(String ids) {
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return dictDatabaseRepo.findByIdIn(idList);
    }


    private DictDatabaseVO convertToDictDatabaseVO(DictDatabase dictDatabase) {
        return mapper.map(dictDatabase, DictDatabaseVO.class);
    }

    private DatabaseNameDTO convertToDatabaseNameDTO(DictDatabase dictDatabase) {
        return mapper.map(dictDatabase, DatabaseNameDTO.class);
    }


}
