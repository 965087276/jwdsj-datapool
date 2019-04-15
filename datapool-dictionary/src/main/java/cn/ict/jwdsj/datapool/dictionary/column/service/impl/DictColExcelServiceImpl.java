package cn.ict.jwdsj.datapool.dictionary.column.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.ict.jwdsj.datapool.common.utils.ExcelJudgeUtil;
import cn.ict.jwdsj.datapool.dictionary.column.entity.DictColExcelDTO;
import cn.ict.jwdsj.datapool.common.entity.dictionary.column.DictColumn;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColExcelService;
import cn.ict.jwdsj.datapool.dictionary.column.service.DictColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.database.DictDatabase;
import cn.ict.jwdsj.datapool.dictionary.database.service.DictDatabaseService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.meta.MetaColumn;
import cn.ict.jwdsj.datapool.dictionary.meta.service.MetaColumnService;
import cn.ict.jwdsj.datapool.common.entity.dictionary.table.DictTable;
import cn.ict.jwdsj.datapool.dictionary.table.entity.dto.TbIdNameDTO;
import cn.ict.jwdsj.datapool.dictionary.table.service.DictTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DictColExcelServiceImpl implements DictColExcelService {

    @Autowired private DictDatabaseService dictDatabaseService;
    @Autowired private DictTableService dictTableService;
    @Autowired private DictColumnService dictColumnService;
    @Autowired private MetaColumnService metaColumnService;

    private final String NOT_EXISTS_DATABASE = "数据库不存在，请先加入数据库的中英信息";
    private String NOT_EXISTS_TABLE = "某些表未加入dict_table";
    private final String EMPTY_CHTABLE = "chTable列存在空值";
    private final String DUPLICATE_OBJECT = "存在重复对象";
    private String EXISTS_IN_DICT_COLUMN = "某些表之前已经加入过dict_column，不能重复加入";
    private String COLUMN_NOT_EXISTS = "所有字段必须真实存在且对应到正确的表上";
    private final String WRONG_TITLE = "表头错误";

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void saveAll(String enDatabase, File file) {
//        DictDatabase dictDatabase = dictDatabaseService.findByEnDatabase(enDatabase);
//        ExcelReader reader = ExcelUtil.getReader(file);
//
//        // 库必须在dict_database中存在
//        assert !BeanUtil.isEmpty(dictDatabase) : NOT_EXISTS_DATABASE;
//        // 表头必须正确
//        assert ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictColExcelDTO.class) : WRONG_TITLE;
//
//
//        List<DictColExcelDTO> colExcelDTOList = reader.readAll(DictColExcelDTO.class);
//        // excel中所有表名
//        List<String> tableNames =
//                colExcelDTOList.stream().map(DictColExcelDTO::getEnTable).distinct().collect(Collectors.toList());
//        // excel中每个表的所有字段
//        Map<String, List<DictColExcelDTO>> excelColsGroupByTable =
//                colExcelDTOList.stream().collect(groupingBy(DictColExcelDTO::getEnTable));
//        // dict_table中该库的所有表
//        List<TbIdNameDTO> tbIdNameDTOList = dictTableService.listTbIdNameDTOByDictDatabase(dictDatabase);
//
//
//        // 中文字段不能为空
//        assert colExcelDTOList.stream().map(DictColExcelDTO::getChColumn).allMatch(StrUtil::isNotBlank) : EMPTY_CHTABLE;
//        // 不能存在重复记录
//        assert colExcelDTOList.size() == colExcelDTOList.stream().distinct().count() : DUPLICATE_OBJECT;
//        // 所有表必须先导入到dict_table中
//        assert allTableExistsInDictTable(tableNames, tbIdNameDTOList) : NOT_EXISTS_TABLE;
//        // 该表字段必须是第一次导入，即dict_column中不存在这些表
//        assert allTableNotExistsInDictColumn(dictDatabase, tableNames) : EXISTS_IN_DICT_COLUMN;
//        // 每个字段必须对应到正确的表上
//        assert allColumnsBelongToCorrectTable(dictDatabase, tableNames, excelColsGroupByTable) : COLUMN_NOT_EXISTS;
//
//        List<DictColumn> dictColumnList = mapColExcelsToDictColumns(colExcelDTOList, tbIdNameDTOList);
//
//        // TIDB中一次事务不能超过5000条，所以大于5000条的要分批插入
//        if (dictColumnList.size() < 5000) {
//            dictColumnService.saveAll(dictColumnList);
//        } else {
//            dictColumnList
//                    .stream()
//                    .collect(groupingBy(dictColumn -> dictColumn.getDictTable().getId()))
//                    .forEach((tableId, columns) -> {
//                        dictColumnService.saveAll(columns);
//                    });
//        }
//
//
//    }

    @Override
    public void saveAll(long databaseId, MultipartFile file) throws IOException {
        DictDatabase dictDatabase = dictDatabaseService.findById(databaseId);
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());

        // 库必须在dict_database中存在
        Assert.isTrue(!BeanUtil.isEmpty(dictDatabase), NOT_EXISTS_DATABASE);
        // 表头必须正确
        Assert.isTrue(ExcelJudgeUtil.judgeHeader(reader.readRow(0), DictColExcelDTO.class), WRONG_TITLE);

        List<DictColExcelDTO> colExcelDTOList = reader.readAll(DictColExcelDTO.class);
        // excel中所有表名
        List<String> tableNames =
                colExcelDTOList.stream().map(DictColExcelDTO::getEnTable).distinct().collect(Collectors.toList());
        // excel中每个表的所有字段
        Map<String, List<DictColExcelDTO>> excelColsGroupByTable =
                colExcelDTOList.stream().collect(groupingBy(DictColExcelDTO::getEnTable));
        // dict_table中该库的所有表
        List<TbIdNameDTO> tbIdNameDTOList = dictTableService.listTbIdNameDTOByDictDatabase(dictDatabase);


        // 中文字段不能为空
        Assert.isTrue(colExcelDTOList.stream().map(DictColExcelDTO::getChColumn).allMatch(StrUtil::isNotBlank), EMPTY_CHTABLE);
        // 不能存在重复记录
        Assert.isTrue(colExcelDTOList.size() == colExcelDTOList.stream().distinct().count(), DUPLICATE_OBJECT);
        // 所有表必须先导入到dict_table中
        Assert.isTrue(allTableExistsInDictTable(tableNames, tbIdNameDTOList), NOT_EXISTS_TABLE);
        // 该表字段必须是第一次导入，即dict_column中不存在这些表
        Assert.isTrue(allTableNotExistsInDictColumn(dictDatabase, tableNames), EXISTS_IN_DICT_COLUMN);
        // 每个字段必须对应到正确的表上
        Assert.isTrue(allColumnsBelongToCorrectTable(dictDatabase, tableNames, excelColsGroupByTable), COLUMN_NOT_EXISTS);

        List<DictColumn> dictColumnList = mapColExcelsToDictColumns(dictDatabase, colExcelDTOList, tbIdNameDTOList);

        // TIDB中一次事务不能超过5000条，所以大于5000条的要分批插入
        if (dictColumnList.size() < 5000) {
            dictColumnService.saveAll(dictColumnList);
        } else {
            dictColumnList
                    .stream()
                    .collect(groupingBy(dictColumn -> dictColumn.getDictTable().getId()))
                    .forEach((tableId, columns) -> {
                        dictColumnService.saveAll(columns);
                    });
        }


    }

    private List<DictColumn> mapColExcelsToDictColumns(DictDatabase dictDatabase, List<DictColExcelDTO> colExcelDTOList, List<TbIdNameDTO> tbIdNameDTOList) {
        Map<String, Long> tableAndId = tbIdNameDTOList.stream()
                .collect(Collectors.toMap(TbIdNameDTO::getEnTable, TbIdNameDTO::getId));
        return colExcelDTOList
                .stream()
                .map(colExcelDTO -> getDictColByColExcelAndTableId(colExcelDTO, tableAndId.get(colExcelDTO.getEnTable()), dictDatabase))
                .collect(Collectors.toList());

    }

    private DictColumn getDictColByColExcelAndTableId(DictColExcelDTO colExcelDTO, long tableId, DictDatabase dictDatabase) {
        DictColumn dictColumn = BeanUtil.toBean(colExcelDTO, DictColumn.class);
        dictColumn.setDictTable(DictTable.builtById(tableId));
        dictColumn.setDictDatabase(dictDatabase);
        return dictColumn;
    }

    private boolean allColumnsBelongToCorrectTable(DictDatabase dictDatabase, List<String> excelTbNames, Map<String, List<DictColExcelDTO>> excelColsGroupByTable) {

        COLUMN_NOT_EXISTS = "下列表的字段有误: ";
        List<String> wrongTables = new ArrayList<>();

        Map<String, List<MetaColumn>> correctTableCols =
                metaColumnService.listByDatabaseAndTableIn(dictDatabase.getEnDatabase(), excelTbNames)
                .stream()
                .collect(groupingBy(MetaColumn::getTable));

        for (Map.Entry<String, List<DictColExcelDTO>> entry : excelColsGroupByTable.entrySet()) {
            String table = entry.getKey();
            List<DictColExcelDTO> map = entry.getValue();

            Set<String> tbColSet = correctTableCols.get(table).
                    stream().map(MetaColumn::getColumn).collect(Collectors.toSet());
            Set<String> excelColSet = map.
                    stream().map(DictColExcelDTO::getEnColumn).collect(Collectors.toSet());

            // 两集合差集,求在excelColSet中但不在tbColSet中的元素，结果应该为空才对
            excelColSet.removeAll(tbColSet);

            if (!excelColSet.isEmpty()) {
                wrongTables.add(table);
            }
        }
        COLUMN_NOT_EXISTS += wrongTables.stream().collect(Collectors.joining(","));
        return wrongTables.isEmpty();

    }

    private boolean allTableExistsInDictTable(List<String> tableNames, List<TbIdNameDTO> tbIdNameDTOList) {
        NOT_EXISTS_TABLE = "下列表的中英表名未加入数据字典中：";
        List<String> wrongTables = new ArrayList<>();

        Set<String> enTables = new HashSet<>(
                tbIdNameDTOList.stream().map(TbIdNameDTO::getEnTable).collect(Collectors.toList())
        );
        for (String tableName : tableNames) {
            if (!enTables.contains(tableName)) {
                wrongTables.add(tableName);
            }
        }
        NOT_EXISTS_TABLE += wrongTables.stream().collect(Collectors.joining(","));
        return wrongTables.isEmpty();
    }

    private boolean allTableNotExistsInDictColumn(DictDatabase dictDatabase, List<String> tableNames) {
        EXISTS_IN_DICT_COLUMN = "下列表之前已经导入过，excel不支持再次导入: ";
        List<String> wrongTables = new ArrayList<>();
        Set<String> tablesInDictColumnByDatabase = new HashSet<>(
                dictColumnService.getEnTableByDictDatabase(dictDatabase)
        );
        for (String tableName : tableNames) {
            if (tablesInDictColumnByDatabase.contains(tableName)) {
                wrongTables.add(tableName);
            }
        }
        EXISTS_IN_DICT_COLUMN += wrongTables.stream().collect(Collectors.joining(","));
        return wrongTables.isEmpty();
    }

}
