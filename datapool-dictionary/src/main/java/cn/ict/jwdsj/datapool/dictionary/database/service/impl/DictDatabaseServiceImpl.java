package cn.ict.jwdsj.datapool.dictionary.database.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.DatabaseNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.QDictDatabase;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.common.utils.StrJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.database.entity.vo.DictDatabaseVO;
import cn.ict.jwdsj.datapool.dictionary.database.repo.DictDatabaseRepo;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.database.entity.dto.UpdateDatabaseDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg.DictUpdateType.DATABASE;

@Service
public class DictDatabaseServiceImpl implements DictDatabaseService {

    @Autowired
    private DictDatabaseRepo dictDatabaseRepo;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private DictTableService dictTableService;

    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    @Override
    public void save(DictDatabase dictDatabase) {
        dictDatabaseRepo.save(dictDatabase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(List<DictDatabase> dictDatabases) {
        dictDatabaseRepo.saveAll(dictDatabases);
    }

    @Override
    public boolean exists(String enDatabase) {
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
        kafkaSender.send(kafkaUpdateTopic, new DictUpdateMsg(DATABASE, updateDatabaseDTO.getDatabaseId()));
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
    public void delete(long id) {
        DictDatabase dictDatabase = DictDatabase.buildById(id);
        // 该库下不能有表
        Assert.isTrue(!dictTableService.existsByDictDatabase(dictDatabase), "该库下还有表，请先删除表信息管理中该库的所有表");
        dictDatabaseRepo.deleteById(id);
    }

    @Override
    public DictDatabase findById(long id) {
        return dictDatabaseRepo.findById(id).get();
    }

    @Override
    public List<DictDatabase> listByIds(String ids) {
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return dictDatabaseRepo.findByIdIn(idList);
    }


    private DictDatabaseVO convertToDictDatabaseVO(DictDatabase dictDatabase) {
        return BeanUtil.toBean(dictDatabase, DictDatabaseVO.class);
    }

    private DatabaseNameDTO convertToDatabaseNameDTO(DictDatabase dictDatabase) {
        DatabaseNameDTO databaseNameDTO =  BeanUtil.toBean(dictDatabase, DatabaseNameDTO.class);
        databaseNameDTO.setDatabaseId(dictDatabase.getId());
        return databaseNameDTO;
    }


}
