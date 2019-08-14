package cn.ict.jwdsj.datapool.dictionary.service.database;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface DictDbExcelService {

    /**
     * 解析excel文件导入dict_database表
     * @param file
     */
    void saveByExcel(MultipartFile file) throws IOException;
}
