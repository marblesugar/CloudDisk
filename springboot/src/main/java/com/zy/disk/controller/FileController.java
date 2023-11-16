package com.zy.disk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.File;
import com.zy.disk.service.IFileService;
import com.zy.disk.utils.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Pipe;
import java.util.List;
import java.util.Map;


@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    private IFileService service;

    /**
     * 分页查询用户文件（文件夹优先）
     * @param currentPage
     * @param pageSize
     * @param curDir
     * @param op (1文件，2回收站)
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public Result getFileByPage(@PathVariable Integer currentPage,
                                @PathVariable Integer pageSize,
                                @RequestParam(defaultValue = "/") String curDir,
                                Integer op){
        Page<File> filePage = service.getFileByPage(currentPage, pageSize, curDir, op);
        return Result.success().addMsg("查询成功！").addData("filePage", filePage);
    }

    /**
     * 文件上传
     * @param uploadFile 文件
     * @param curDir 当前目录
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile uploadFile, @RequestParam(defaultValue = "/") String curDir){
        System.out.println(WebUtil.getLoginId());
        service.upload(uploadFile, curDir);
        return Result.success().addMsg("上传成功！");
    }

    /**
     * 下载文件
     * @param id
     * @return
     */
    @GetMapping("/download/{id}")
    public Result download(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response){
        return service.download(id, request, response);
    }

    /**
     * 创建目录
     * @param map ==> curDir 当前目录（在最前面需要加/） dirName 新建目录名
     * @return
     */
    @PostMapping("/createDir")
    public Result createDir(@RequestBody Map<String, String> map){
        String curDir = map.get("curDir");
        String dirName = map.get("dirName");
        return service.createDir(curDir, dirName);
    }

    /**
     * 删除文件或目录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteFile(@PathVariable Integer id){
        Boolean b = service.deleteFile(id);
        if (b){
            return Result.success().addMsg("删除成功！");
        }
        return Result.success().addMsg("删除失败！");
    }

    /**
     * 通过ids删除文件或文件夹
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestBody List<Integer> ids){
        return service.deleteList(ids);
    }

    @DeleteMapping("/deleteListForever")
    public Result deleteListForever(@RequestBody List<Integer> ids){
        return service.deleteListForever(ids);
    }

    /**
     * 修改文件名
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result updateFileName(@PathVariable Integer id, String newName){
        return service.updateName(id, newName);
    }
    /**
     * 获得当前文件或文件夹所在目录
     * @param id
     * @return
     */
    @GetMapping("/getFileCurDir/{id}")
    public Result getFileCurDir(@PathVariable Integer id){
        return service.getFileCurDir(id);
    }

    /**
     * 统计某个文件夹的大小
     * @param id
     * @return
     */
    @GetMapping("/countSize/{id}")
    public Result countSize(@PathVariable Integer id){
        return service.countSize(id);
    }

    /**
     * 在当前目录中，模糊搜索文件
     * @param curDir
     * @param fileKeyWord
     * @return
     */
    @GetMapping("/searchFileInCurDir")
    public Result searchFileInCurDir(@RequestParam(defaultValue = "/") String curDir, String fileKeyWord){
        if (fileKeyWord.contains("/") || fileKeyWord.contains("\\")){
            return Result.fail().addMsg("不允许出现斜杠");
        }
        return service.searchFileInCurDir(curDir, fileKeyWord);
    }


    /**
     * 删除回收站中的所有文件
     * @return
     */
    @DeleteMapping("/deleteAllForever")
    public Result deleteAllForever(){
        return service.deleteAllForever();
    }

    /**
     * 删除回收站中过期的文件
     * @return
     */
    @DeleteMapping("/deleteExpiredFile")
    public Result deleteExpiredFile(){
        return service.deleteExpiredFile();
    }

    /**
     * 通过id删除回收站中的文件
     * @param id
     * @return
     */
    @DeleteMapping("/deleteSingleForever/{id}")
    public Result deleteSingleForever(@PathVariable Integer id){
        return service.deleteSingleForever(id);
    }

    /**
     * 通过id恢复回收站中的文件
     * @param ids
     * @return
     */
    @PutMapping("/recovery")
    public Result recovery(@RequestBody List<Integer> ids){
        return service.recovery(ids);
    }

}
