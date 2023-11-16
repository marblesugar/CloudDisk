package com.zjj.disk;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjj.disk.mapper.FileMapper;
import com.zjj.disk.pojo.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zjj
 * @create 2023-04-08 15:38
 */
@SpringBootTest
public class PageTest {
    @Autowired
    private FileMapper mapper;
    @Test
    public void testPage(){
        Integer currentPage = 1;
        Integer pageSize = 7;
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<File>();
        lambdaQueryWrapper.eq(File::getCreatorId, 1)
                .eq(File::getCurDir, '/')
                .eq(File::getIsDeleted, 0)
                .select(File::getId, File::getFileName,File::getFileType,File::getFileSize,File::getIsDir);
        Page<File> filePage = new Page<>(currentPage, pageSize);
        if (currentPage > filePage.getPages()){
            currentPage = (int)filePage.getPages();
            filePage = new Page<>(currentPage, pageSize);
        }
        mapper.selectPage(filePage, lambdaQueryWrapper);
    }
}
