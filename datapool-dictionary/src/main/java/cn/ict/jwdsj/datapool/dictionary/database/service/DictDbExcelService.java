package cn.ict.jwdsj.datapool.dictionary.database.service;

import java.io.File;

public interface DictDbExcelService {

    /**
     * 解析excel文件导入dict_database表
     * @param file
     */
    void saveByExcel(File file);
}
