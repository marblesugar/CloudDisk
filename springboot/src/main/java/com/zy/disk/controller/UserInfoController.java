package com.zy.disk.controller;

import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.UserInfo;
import com.zy.disk.service.IUserInfoService;
import com.zy.disk.utils.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zjj
 * @create 2023-04-11 20:38
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private IUserInfoService service;

    /**
     * 通过token获得用户信息
     * @return
     */
    @GetMapping
    public Result getUserInfo(){
        return service.getUserInfo();
    }

    /**
     * 头像上传
     * @param avatarFile 文件
     * @return
     */
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(MultipartFile avatarFile){
        return service.uploadAvatar(avatarFile);
    }

    /**
     * 修改基本信息
     * @param userInfo
     * @return
     */
    @PostMapping("/changeInfo")
    public Result changeInfo(@RequestBody UserInfo userInfo){
        return service.changeInfo(userInfo);
    }


}
