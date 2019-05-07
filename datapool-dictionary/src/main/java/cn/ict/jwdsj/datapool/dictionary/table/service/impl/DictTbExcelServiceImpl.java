package cn.ict.jwdsj.datapool.dictionary.table.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.utils.ExcelJudgeUtil;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaTable;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaTableService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.DictTbExcelDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTbExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DictTbExcelServiceImpl implements DictTbExcelService {
    @Autowired
    private DictTableService dictTableService;
    @Autowired
    private DictDatabaseService dictDatabaseService;
    @Autowired
    private MetaTableService metaTableService;

    private final String NOT_EXISTS_DATABASE = "数据库不存在，请先加入数据库的中英信息";
    private String NOT_EXISTS_TABLE = "数据库中不存在这个表";
    private final String EMPTY_CHTABLE = "chTable列存在空值";
    private final String DUPLICATE_OBJECT = "存在重复对象";
    private String EXISTS_IN_DICT_TABLE = "表之前已经加入过dict_table，不能重复加入";
    private final String WRONG_HEADER = "表头错误";

//    @Override
//    public void saveAll(String enDatabase, File file) {
//
//        DictDatabase dictDatabase = dictDatabaseService.findByEnDatabase(enDatabase);
//        ExcelReader reader = ExcelUtil.getReader(file);
//
//        // 库名必须在数据库中存在
//        assert !BeanUtil.isEmpty(dictDatabase) : NOT_EXISTS_DATABASE;
//
//        // 表头必须正确
//        assert ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictTbExcelDTO.class);
//
//
//        List<DictTbExcelDTO> tbExcelDTOS = reader.readAll(DictTbExcelDTO.class);
//
//        // 中文表名不能为空
//        assert tbExcelDTOS.stream().map(DictTbExcelDTO::getChTable).allMatch(StrUtil::isNotBlank) : EMPTY_CHTABLE;
//        // 不能存在重复记录
//        assert tbExcelDTOS.size() == tbExcelDTOS.stream().distinct().count() : DUPLICATE_OBJECT;
//        // 所有表必须真实存在
//        assert allTableExistsInMetaDatabase(dictDatabase.getEnDatabase(), tbExcelDTOS) : NOT_EXISTS_TABLE;
//        // dict_table中不存在该表
//        assert allTableNotExistsInDictTable(dictDatabase, tbExcelDTOS) : EXISTS_IN_DICT_TABLE;
//
//        List<DictTable> dictTables = tbExcelDTOS.stream()
//                .map(tbExcelDTO -> BeanUtil.toBean(tbExcelDTO, DictTable.class))
//                .collect(Collectors.toList());
//        dictTables.forEach(dictTable -> dictTable.setDictDatabase(dictDatabase));
//        dictTableService.saveAll(dictTables);
//
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(long databaseId, MultipartFile file) throws IOException {

        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        Assert.isTrue(!BeanUtil.isEmpty(dictDatabase), "数据库不存在");
        // 表头必须正确
        Assert.isTrue(ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictTbExcelDTO.class), WRONG_HEADER);

        List<DictTbExcelDTO> tbExcelDTOS = reader.readAll(DictTbExcelDTO.class);

        // 中文表名不能为空
        Assert.isTrue(tbExcelDTOS.stream().map(DictTbExcelDTO::getChTable).allMatch(StrUtil::isNotBlank), EMPTY_CHTABLE);
        // 不能存在重复记录
        Assert.isTrue(tbExcelDTOS.size() == tbExcelDTOS.stream().distinct().count(), DUPLICATE_OBJECT);
        // 所有表必须真实存在
        Assert.isTrue(allTableExistsInMetaDatabase(dictDatabase.getEnDatabase(), tbExcelDTOS), NOT_EXISTS_TABLE);
        // dict_table中不存在该表
        Assert.isTrue(allTableNotExistsInDictTable(dictDatabase, tbExcelDTOS), EXISTS_IN_DICT_TABLE);

        List<DictTable> dictTables = tbExcelDTOS.stream()
                .map(tbExcelDTO -> this.convertToDictTable(tbExcelDTO, dictDatabase))
                .collect(Collectors.toList());

        dictTableService.saveAll(dictTables);
    }

    private DictTable convertToDictTable(DictTbExcelDTO tbExcelDTO, DictDatabase dictDatabase) {
        DictTable dictTable = new DictTable();
        dictTable.setEnDatabase(dictDatabase.getEnDatabase());
        dictTable.setDictDatabase(dictDatabase);
        dictTable.setEnTable(tbExcelDTO.getEnTable());
        dictTable.setChTable(tbExcelDTO.getChTable());
        dictTable.setAddToSe(false);
        return dictTable;
    }

    private boolean allTableNotExistsInDictTable(DictDatabase dictDatabase, List<DictTbExcelDTO> tbExcelDTOS) {
        EXISTS_IN_DICT_TABLE = "下列表之前已经加入过, 不能重复加入: ";
        List<String> wrongTables = new ArrayList<>();

        List<String> dictTables = dictTableService.listEnTablesByDictDatabase(dictDatabase);
        Set<String> dictTableSet = new HashSet<>(dictTables);

        for (DictTbExcelDTO tbExcelDTO : tbExcelDTOS) {
            if (dictTableSet.contains(tbExcelDTO.getEnTable())) {
                wrongTables.add(tbExcelDTO.getEnTable());
            }
        }
        EXISTS_IN_DICT_TABLE += wrongTables.stream().collect(Collectors.joining(","));
        return wrongTables.isEmpty();
    }

    private boolean allTableExistsInMetaDatabase(String enDatabase, List<DictTbExcelDTO> tbExcelDTOS) {
        NOT_EXISTS_TABLE = "数据表不存在: ";
        List<String> wrongTables = new ArrayList<>();

        Set<String> metaTables = metaTableService.listByDatabase(enDatabase)
                .stream()
                .map(MetaTable::getTable)
                .collect(Collectors.toSet());
        for (DictTbExcelDTO tbExcelDTO : tbExcelDTOS) {
            if (!metaTables.contains(tbExcelDTO.getEnTable())) {
                wrongTables.add(tbExcelDTO.getEnTable());
            }
        }
        NOT_EXISTS_TABLE += wrongTables.stream().collect(Collectors.joining(","));
        return wrongTables.isEmpty();
    }
}
