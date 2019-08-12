package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.mapper.DictColumnMapper;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.UpdateColumnDTO;
import cn.ict.jwdsj.datapool.dictionary.event.DictAddEvent;
import cn.ict.jwdsj.datapool.dictionary.event.DictDeleteEvent;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.github.dozermapper.core.Mapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.constant.DictType.COLUMN;
import static cn.ict.jwdsj.datapool.common.constant.DictType.COLUMNS;

@Service
public class DictColumnServiceImpl implements DictColumnService {
    @Autowired
    private DictColumnRepo dictColumnRepo;
    @Autowired
    private DictColumnMapper dictColumnMapper;
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private ApplicationContext publisher;
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private Mapper mapper;
    @Value("${kafka.topic-name.dict-update}")
    private String kafkaUpdateTopic;

    /**
     * 批量插入（忽略错误）
     *
     * @param dictColumns
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllToDb(List<DictColumn> dictColumns) {
        String currentTime = dictColumnMapper.getCurrentTimeStamp();
        dictColumnMapper.insertIgnore(dictColumns);
        publisher.publishEvent(new DictAddEvent(currentTime, COLUMNS));
    }

    @Override
    public List<DictColumnVO> listDictColumnVOs(long databaseId, long tableId) {

        return dictColumnRepo.findByTableId(tableId)
                .stream()
                .map(dictColumn -> this.convertToDictColumnVO(dictColumn))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(DictColumnMultiAddDTO dictColumnMultiAddDTO) {
        // 判断是否有重复元素
        Assert.isTrue(dictColumnMultiAddDTO.getDictColumnAddDTOS().size() == dictColumnMultiAddDTO.getDictColumnAddDTOS().stream().distinct().count(), "有重复元素");
        List<DictColumn> dictColumns = this.convertToDictColumnList(dictColumnMultiAddDTO);
        this.saveAllToDb(dictColumns);
    }


    @Override
    public List<DictColumn> listByTableId(long tableId) {
        return dictColumnRepo.findByTableId(tableId);
    }

    @Override
    public List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId) {
        return dictColumnRepo.findByTableId(tableId)
                .stream()
                .map(col -> mapper.map(col, ColumnNameDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(UpdateColumnDTO updateColumnDTO) {
        DictColumn dictColumn = dictColumnRepo.getOne(updateColumnDTO.getColumnId());
        dictColumn.setChColumn(updateColumnDTO.getChColumn());
        dictColumnRepo.save(dictColumn);
        kafkaSender.send(kafkaUpdateTopic, new DictUpdateMsg(COLUMN, updateColumnDTO.getColumnId()));
    }

    @Override
    public DictColumn findById(long id) {
        return dictColumnRepo.getOne(id);
    }

    /**
     * 通过id删除字段
     *
     * @param id
     */
    @Override
    public void delete(long id) {
        DictColumn dictColumn = dictColumnRepo.getOne(id);
        // 该字段所在表不能加入到搜索引擎中
        Assert.isTrue(!dictTableService.findById(dictColumn.getTableId()).isAddToSe(), "该字段所在的表已经加入到搜索引擎表中，请从搜索引擎表中删除该表");
        dictColumnRepo.deleteById(id);
        publisher.publishEvent(new DictDeleteEvent(id, COLUMN));
    }

    /**
     * 删除某表下的所有字段
     *
     * @param tableId
     */
    @Override
    @Transactional
    public void deleteByTableId(long tableId) {
        dictColumnRepo.deleteAllByTableId(tableId);
        publisher.publishEvent(new DictDeleteEvent(tableId, COLUMNS));
    }

    private DictColumnVO convertToDictColumnVO(DictColumn dictColumn) {
        return mapper.map(dictColumn, DictColumnVO.class);
    }

    private List<DictColumn> convertToDictColumnList(DictColumnMultiAddDTO dictColumnMultiAddDTO) {
        List<DictColumn> columns = new ArrayList<>();
        for (DictColumnAddDTO columnDTO : dictColumnMultiAddDTO.getDictColumnAddDTOS()) {
            DictColumn dictColumn = DictColumn.builder()
                    .enColumn(columnDTO.getEnColumn())
                    .chColumn(columnDTO.getChColumn())
                    .databaseId(dictColumnMultiAddDTO.getDatabaseId())
                    .enDatabase(dictColumnMultiAddDTO.getEnDatabase())
                    .tableId(dictColumnMultiAddDTO.getTableId())
                    .enTable(dictColumnMultiAddDTO.getEnTable())
                    .build();
            columns.add(dictColumn);
        }
        return columns;
    }

}
