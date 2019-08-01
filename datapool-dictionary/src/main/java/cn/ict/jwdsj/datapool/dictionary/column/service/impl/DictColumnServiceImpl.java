package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.ict.jwdsj.datapool.common.dto.dictionary.ColumnNameDTO;
import cn.ict.jwdsj.datapool.common.dto.dictionary.TableNameDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.QDictColumn;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.common.kafka.DictUpdateMsg;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.DictColumnMultiAddDTO;
import cn.ict.jwdsj.datapool.dictionary.column.entity.vo.DictColumnVO;
import cn.ict.jwdsj.datapool.dictionary.column.mapper.DictColumnMapper;
import cn.ict.jwdsj.datapool.dictionary.column.repo.DictColumnRepo;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.config.KafkaSender;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.dictionary.column.entity.dto.UpdateColumnDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import com.github.dozermapper.core.Mapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ict.jwdsj.datapool.common.constant.DictType.COLUMN;

@Service
public class DictColumnServiceImpl implements DictColumnService {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private DictColumnRepo dictColumnRepo;
    @Autowired
    private DictColumnMapper dictColumnMapper;
    @Autowired
    private DictTableService dictTableService;
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
    public void insertIgnore(List<DictColumn> dictColumns) {
        dictColumnMapper.insertIgnore(dictColumns);
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
        dictColumnRepo.saveAll(dictColumns);
    }


    @Override
    public List<DictColumn> listByTableId(long tableId) {
        return dictColumnRepo.findByTableId(tableId);
    }

    @Override
    public List<ColumnNameDTO> listColumnNameDTOsByTableId(long tableId) {
        QDictColumn dictColumn = QDictColumn.dictColumn;
        return jpaQueryFactory.select(dictColumn.id, dictColumn.enColumn, dictColumn.chColumn)
                .from(dictColumn)
                .where(dictColumn.tableId.eq(tableId))
                .fetch()
                .stream()
                .map(tuple -> ColumnNameDTO.builder()
                        .columnId(tuple.get(dictColumn.id))
                        .enColumn(tuple.get(dictColumn.enColumn))
                        .chColumn(tuple.get(dictColumn.chColumn))
                        .build()
                ).collect(Collectors.toList());
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
