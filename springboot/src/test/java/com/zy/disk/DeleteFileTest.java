package com.zy.disk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zy.disk.service.IFileService;
import com.zy.disk.service.impl.FileServiceImpl;


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
