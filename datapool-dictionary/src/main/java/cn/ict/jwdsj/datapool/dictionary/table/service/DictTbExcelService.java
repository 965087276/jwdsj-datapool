package cn.ict.jwdsj.datapool.dictionary.table.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface DictTbExcelService  {


//    /**
//     * 单库数据导入
//     * @param databaseId
//     * @param file
//     * @throws IOException
//     */
//    void saveAllToDb(long databaseId, MultipartFile file) throws IOException;

    /**
     * 多库数据导入
     * @param file
     * @throws IOException
     */
    void saveAll(MultipartFile file) throws IOException;
}
