package com.zjj.disk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.disk.controller.result.Result;
import com.zjj.disk.pojo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zjj
 * @create 2023-04-11 19:50
 */
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
