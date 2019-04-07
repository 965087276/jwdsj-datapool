package cn.ict.jwdsj.datapool.dictionary.table.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface DictTbExcelService  {
    void saveAll(long databaseId, MultipartFile file) throws IOException;
}
