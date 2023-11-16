package com.zy.disk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.UserInfo;

import org.springframework.web.multipart.MultipartFile;


public interface IUserInfoService extends IService<UserInfo> {
    /**
     * 通过token获得用户信息
     * @return
     */
    Result getUserInfo();

    /**
     * 头像上传
     * @param avatarFile 文件
     * @return
     */
    Result uploadAvatar(MultipartFile avatarFile);

    /**
     * 修改基本信息
     * @param userInfo
     * @return
     */
    Result changeInfo(UserInfo userInfo);
}
