package com.zjj.disk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.disk.controller.result.Result;
import com.zjj.disk.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zjj
 * @create 2023-03-26 16:40
 */
public interface IFileService extends IService<File> {
    /**
     * 文件上传
     * @param uploadFile 文件
     * @param curDir 当前目录
     * @return
     */
    Boolean upload(MultipartFile uploadFile, String curDir);

    /**
     * 分页显示用户上传的文件
     *
     * @param currentPage
     * @param pageSize
     * @param curDir
     * @param op
     * @return
     */
    Page<File> getFileByPage(Integer currentPage, Integer pageSize, String curDir, Integer op);

    /**
     * 下载文件
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    Result download(Integer id, HttpServletRequest request, HttpServletResponse response);

    /**
     * 创建目录
     * @param curDir 当前目录（在最前面需要加/）
     * @param dirName 新建目录名
     * @return
     */
    Result createDir(String curDir, String dirName);

    /**
     * 删除文件或目录
     * @param id
     * @return
     */
    Boolean deleteFile(Integer id);

    /**
     * 修改文件或文件夹名
     * @param id
     * @param newName
     * @return
     */
    Result updateName(Integer id, String newName);

    /**
     * 获得当前文件或文件夹所在目录
     * @param id
     * @return
     */
    Result getFileCurDir(Integer id);

    /**
     * 统计某个文件夹的大小
     * @param id
     * @return
     */
    Result countSize(Integer id);

    /**
     * 在当前目录中，模糊搜索文件
     *
     * @param curDir
     * @param fileKeyWord
     * @return
     */
    Result searchFileInCurDir(String curDir, String fileKeyWord);


    /**
     * 删除回收站中的所有文件
     * @return
     */
    Result deleteAllForever();

    /**
     * 删除回收站中所有过期的文件
     * @return
     */
    Result deleteExpiredFile();

    /**
     * 通过id删除回收站中的文件
     * @param id
     * @return
     */
    Result deleteSingleForever(Integer id);

    /**
     * 通过id恢复回收站中的文件
     * @param ids
     * @return
     */
    Result recovery(List<Integer> ids);

    /**
     * 通过ids删除文件或文件夹
     * @param ids
     * @return
     */
    Result deleteList(List<Integer> ids);

    /**
     * 通过ids永久删除文件
     * @param ids
     * @return
     */
    Result deleteListForever(List<Integer> ids);
}
