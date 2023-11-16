package com.zjj.disk;

import com.zjj.disk.service.IFileService;
import com.zjj.disk.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zjj
 * @create 2023-04-12 16:39
 */
@SpringBootTest
public class DeleteFileTest {
    @Autowired
    private FileServiceImpl service;

    public void testDeleteFile(){
        String location = "D:\\idea\\workspace\\disk/src/main/resources/upload/1/test";
        Integer loginId = 1;
        service.realDelete(location, loginId);
    }
}
