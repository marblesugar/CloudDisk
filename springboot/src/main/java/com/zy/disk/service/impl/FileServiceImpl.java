package com.zy.disk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.disk.controller.result.Result;
import com.zy.disk.mapper.FileMapper;
import com.zy.disk.pojo.File;
import com.zy.disk.service.IFileService;
import com.zy.disk.utils.TypeUtil;
import com.zy.disk.utils.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Autowired
    private FileMapper mapper;
    @Value("${application.uploadfile}")
    private String fileRootPath;

    @Override
    public Boolean upload(MultipartFile uploadFile, String curDir) {
        String suf = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suf;
        Integer loginId = WebUtil.getLoginId();
        String location = fileRootPath + loginId + curDir;
        if(!curDir.equals("/")){
            location+="/";
        }

        File file = new File();
        file.setFileName(uploadFile.getOriginalFilename());
        String type = TypeUtil.judgeType(suf);
        file.setFileType(type);
        file.setFileSize(uploadFile.getSize());
        file.setCurDir(location);
        file.setFileLocation(location + newFileName);
        file.setFileExtention(suf);
        file.setCreatorId(loginId);
        file.setIsDir(0);//TODO

        System.out.println(location);
        realUpload(uploadFile, location, newFileName);
        return mapper.insert(file) > 0;
    }

    @Override
    public Page<File> getFileByPage(Integer currentPage, Integer pageSize, String curDir, Integer op) {
        Integer loginId = WebUtil.getLoginId();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //文件
        if (op == 1){
            String location = fileRootPath + loginId + curDir;
            if (!curDir.equals("/")){
                location+="/";
            }
            lambdaQueryWrapper.eq(File::getCreatorId, loginId)
                    .eq(File::getCurDir, location)
                    .eq(File::getIsDeleted, 0)
                    .select(File::getId, File::getFileName,File::getFileType,File::getFileSize,File::getIsDir)
                    .orderByDesc(File::getIsDir)
                    .orderByAsc(File::getId);
        }else if (op == 2){//回收站
            LocalDateTime now = LocalDateTime.now();
            lambdaQueryWrapper.eq(File::getIsDeleted, 1)
                    .ge(File::getExpiredTime, now)
                    .eq(File::getCreatorId, loginId)
                    .eq(File::getIsDir, 0);
        }
        Page<File> filePage = new Page<>(currentPage, pageSize);
        mapper.selectPage(filePage, lambdaQueryWrapper);
        int size = filePage.getRecords().size();
        for (int i=0; i< size; i++){
            if(filePage.getRecords().get(i).getIsDir() == 1){
                Integer id = filePage.getRecords().get(i).getId();
                filePage.getRecords().get(i).setFileSize((Long) countSize(id).getData().get("countSize"));
            }
        }
        return filePage;
    }

    @Override
    public Result download(Integer id, HttpServletRequest request, HttpServletResponse response) {
        File file = mapper.selectById(id);
        if (file == null) {
            return Result.fail().addMsg("文件不存在！");
        }
        String fileName = file.getFileName();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new java.io.File(file.getFileLocation()));
            response.setContentType(request.getSession().getServletContext().getMimeType(file.getFileExtention()));//获取文件的mimetype
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            ServletOutputStream os = response.getOutputStream();
            FileCopyUtils.copy(fileInputStream,os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success().addMsg("下载成功！");
    }

    @Override
    public Result createDir(String curDir, String dirName) {
        if (StringUtils.isBlank(dirName)){
            return Result.fail().addMsg("文件夹名为空！");
        }
        if (dirName.contains("\\") || dirName.contains("/")) {
            return Result.fail().addMsg("文件夹中包含斜杠！");
        }
        int loginId = WebUtil.getLoginId();
        String location = fileRootPath + loginId + curDir;
        if(!curDir.equals("/")){
            location+="/";
        }
        java.io.File file = new java.io.File(location+dirName);
        if(!file.exists()){
            File dirFile = new File();
            dirFile.setFileName(dirName);
            dirFile.setCreatorId(loginId);
            dirFile.setCurDir(location);
            dirFile.setFileLocation(location + dirName);
            dirFile.setIsDir(1);
            mapper.insert(dirFile);
            file.mkdir();
        }else {
            LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(File::getCurDir, location)
                    .eq(File::getFileName, dirName);
            File dir = mapper.selectOne(lambdaQueryWrapper);
            if (dir.getIsDeleted() == 0){
                return Result.fail().addMsg("文件夹已存在！");
            }
            dir.setIsDeleted(0);
            mapper.updateById(dir);
        }
        return Result.success().addMsg("文件夹创建成功！");
    }

    @Override
    public Boolean deleteFile(Integer id) {
        int loginId = WebUtil.getLoginId();
        File file = mapper.getById(id);
        if (file==null || loginId != file.getCreatorId()){
            return false;
        }
        LocalDateTime expiredTime = LocalDateTime.now().plusDays(7);
        file.setIsDeleted(1);
        file.setExpiredTime(expiredTime);
        if (file.getIsDir() == 0){
            return mapper.updateById(file)>0;
        }
        realDelete(file.getFileLocation(), loginId);

        String sqlString = (file.getFileLocation()+"/").replace("\\","\\\\");
        LambdaQueryWrapper<File> lambdaQueryWrapperFile = new LambdaQueryWrapper<>();
        lambdaQueryWrapperFile.like(File::getCurDir, sqlString);

        LambdaQueryWrapper<File> lambdaQueryWrapperDir = new LambdaQueryWrapper<>();
        lambdaQueryWrapperDir.like(File::getCurDir, sqlString)
                .eq(File::getIsDir, 1);

        for (File childFile : mapper.selectList(lambdaQueryWrapperFile)) {
            if (childFile.getIsDir() == 0){
                childFile.setIsDeleted(1);
                childFile.setExpiredTime(expiredTime);
                childFile.setCurDir(fileRootPath+loginId+"/");
                String oldFileLocation = childFile.getFileLocation();
                String newFileLocation = childFile.getCurDir() + oldFileLocation.substring(oldFileLocation.lastIndexOf("/") + 1);
                childFile.setFileLocation(newFileLocation);
                mapper.updateById(childFile);
            }
        }
        mapper.delete(lambdaQueryWrapperDir);
        mapper.deleteById(file);
        return true;
    }

    @Override
    public Result updateName(Integer id, String newName) {
        if (StringUtils.isBlank(newName)){
            return new Result().fail().addMsg("新文件名为空！");
        }
        Integer loginId = WebUtil.getLoginId();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getIsDeleted, 0)
                        .eq(File::getCreatorId, loginId)
                .eq(File::getId, id);
        File file = mapper.selectOne(lambdaQueryWrapper);
        if (newName.equals(file.getFileName())){
            return new Result().success().addMsg("修改成功！");
        }
        if (file.getIsDir() == 1){
            LambdaQueryWrapper<File> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(File::getFileName, newName)
                    .eq(File::getIsDeleted, 0)
                    .eq(File::getIsDir, 1);
            if (mapper.selectCount(lambdaQueryWrapper1)>0) {
                return new Result().fail().addMsg("已存在相同名称的目录");
            }else {
                file.setFileName(newName);
                mapper.update(file, lambdaQueryWrapper);
                return new Result().success().addMsg("修改成功！");
            }
        }
        file.setFileName(newName+file.getFileExtention());
        mapper.update(file, lambdaQueryWrapper);
        return new Result().success().addMsg("修改成功！");
    }

    @Override
    public Result getFileCurDir(Integer id) {
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getId, id)
                .eq(File::getIsDeleted, 0);
        File file = mapper.selectOne(lambdaQueryWrapper);
        System.out.println(file.getCurDir());
        if(file==null){
            return new Result().fail().addMsg("文件或文件夹不存在！");
        }
        String curDir = file.getCurDir();
        String s = fileRootPath + WebUtil.getLoginId() + "/";
        int length = s.length();
        String relativeDir = curDir.substring(length - 1);
        System.out.println(relativeDir);
        if (!"/".equals(relativeDir)){
            relativeDir = relativeDir.substring(0,relativeDir.length()-1);
        }
        return new Result().success().addMsg("查找成功！").addData("relativeDir", relativeDir);
    }

    @Override
    public Result countSize(Integer id) {
        File dir = mapper.selectById(id);
        if (dir==null || dir.getIsDir()==0){
            return new Result().fail().addMsg("不存在此目录");
        }
        String dirFileLocation = dir.getFileLocation();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String replace = dirFileLocation.replace("\\", "\\\\");
        lambdaQueryWrapper.like(File::getCurDir, replace)
                .eq(File::getIsDeleted, 0);
        List<File> fileListist = mapper.selectList(lambdaQueryWrapper);
        Long countSize = 0L;
        for (File file : fileListist) {
            if (file.getIsDir() == 0){
                countSize+=file.getFileSize();
            }
        }
        return new Result().success().addMsg("统计成功！").addData("countSize", countSize);
    }

    @Override
    public Result searchFileInCurDir(String curDir, String fileKeyWord) {
        Integer loginId = WebUtil.getLoginId();
        String s = fileRootPath + loginId + "/";
        String substringDir = curDir.substring(1);
        String realDir=null;
        if (!curDir.equals("/")){
            realDir = s + substringDir + "/";
        }else {
            realDir = s + substringDir;
        }
        String sqlRealDir = realDir.replace("\\", "\\\\");
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(File::getCurDir, sqlRealDir)
                .like(File::getFileName, fileKeyWord)
                .eq(File::getIsDir, 0)
                .eq(File::getIsDeleted, 0)
                .orderByDesc(File::getIsDir)
                .orderByAsc(File::getId);
        List<File> searchFileList = mapper.selectList(lambdaQueryWrapper);
        return new Result().success().addMsg("查询成功！").addData("searchFileList", searchFileList);
    }

    @Override
    public Result deleteAllForever() {
        Integer loginId = WebUtil.getLoginId();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getCreatorId, loginId)
                .eq(File::getIsDeleted, 1);
        List<File> fileList = mapper.selectList(lambdaQueryWrapper);
        for (File file: fileList){
            java.io.File fileActual = new java.io.File(file.getFileLocation());
            fileActual.delete();
            mapper.deleteById(file);
        }
        return new Result().success().addMsg("已全部删除！");
    }

    @Override
    public Result deleteExpiredFile() {
        Integer loginId = WebUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getIsDeleted, 1)
                .le(File::getExpiredTime, now)
                .eq(File::getCreatorId, loginId);
        List<File> fileList = mapper.selectList(lambdaQueryWrapper);
        for (File file: fileList){
            java.io.File fileActual = new java.io.File(file.getFileLocation());
            fileActual.delete();
            mapper.deleteById(file);
        }
        return new Result().success().addMsg("清除成功！");
    }

    @Override
    public Result deleteSingleForever(Integer id) {
        Integer loginId = WebUtil.getLoginId();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getCreatorId, loginId)
                .eq(File::getId, id)
                .eq(File::getIsDeleted, 1);
        File file = mapper.selectOne(lambdaQueryWrapper);
        if (file==null){
            return new Result().fail().addMsg("文件不存在！");
        }
        java.io.File fileActual = new java.io.File(file.getFileLocation());
        if (fileActual.exists()){
            fileActual.delete();
        }
        mapper.deleteById(file);
        return new Result().success().addMsg("删除成功！").addData("id", file.getId());
    }

    @Override
    public Result recovery(List<Integer> ids) {
        ArrayList<Integer> recoveryIds = new ArrayList<>();
        Integer loginId = WebUtil.getLoginId();
        for (Integer id : ids) {
            LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(File::getCreatorId, loginId)
                    .eq(File::getIsDeleted, 1)
                    .eq(File::getId, id);
            File file = mapper.selectOne(lambdaQueryWrapper);
            if (file==null){
                return new Result().fail().addMsg("文件不存在");
            }
            //过期时间加99年
            LocalDateTime expiredTime = LocalDateTime.now().plusYears(99);
            file.setIsDeleted(0);
            file.setExpiredTime(expiredTime);
            mapper.updateById(file);
            recoveryIds.add(file.getId());
        }
        return new Result().success().addMsg("恢复成功！").addData("recoveryIds", recoveryIds);
    }

    @Override
    public Result deleteList(List<Integer> ids) {
        if (ids.size()==0){
            return new Result().fail().addMsg("参数为空");
        }else {
            for (Integer id : ids){
                deleteFile(id);
            }
        }

        return new Result().success().addMsg("删除成功");
    }

    @Override
    public Result deleteListForever(List<Integer> ids) {
        ArrayList<Integer> deleteForeverIds = new ArrayList<>();
        if (ids.size() == 0){
            return new Result().fail().addMsg("参数为空");
        }else {
            for (Integer id : ids){
                Result result = deleteSingleForever(id);
                if (result.getCode()==200){
                    deleteForeverIds.add((Integer) result.getData().get("id"));
                }
            }
        }

        return new Result().success().addMsg("永久删除成功").addData("deleteForeverIds",deleteForeverIds);
    }


    /**
     * 真实上传
     *
     * @param uploadFile
     * @return
     */
    public void realUpload(MultipartFile uploadFile, String location, String newFileName) {
        java.io.File file_dir = new java.io.File(location);
        if (!file_dir.exists()) {
            file_dir.mkdir();
        }
        try {
            uploadFile.transferTo(new java.io.File(location + newFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 遍历删除当前文件夹及内部目录，将目录中的文件移动到根目录
     * @param location
     * @param loginId
     */
    public void realDelete(String location, Integer loginId){
        java.io.File file = new java.io.File(location);
        for (String childFile : file.list()) {
            java.io.File file1 = new java.io.File(file.getAbsoluteFile() + "/" + childFile);
            if (file1.isDirectory()){
                realDelete(file1.getAbsolutePath(),loginId);
            }else {
                file1.renameTo(new java.io.File(fileRootPath+loginId+'/'+childFile));
                file1.delete();
            }
        }
        file.delete();
    }
}
