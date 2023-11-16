package com.zy.disk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.disk.controller.result.Result;
import com.zy.disk.mapper.UserInfoMapper;
import com.zy.disk.mapper.UserMapper;
import com.zy.disk.pojo.User;
import com.zy.disk.pojo.UserInfo;
import com.zy.disk.service.IUserInfoService;
import com.zy.disk.utils.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author zjj
 * @create 2023-04-11 19:51
 */
@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Autowired
    private UserInfoMapper mapper;
    @Autowired
    private UserMapper  userMapper;
    @Value("${application.avatarfile}")
    private String avatarRootPath;
    @Override
    public Result getUserInfo() {
        Integer loginId = WebUtil.getLoginId();
        UserInfo userInfo = mapper.getUserInfo(loginId);
        if(userInfo == null){
            return new Result().fail().addMsg("用户不存在！");
        }else {
            StringBuffer doEmail = new StringBuffer(userInfo.getEmail());
            int i = doEmail.indexOf("@");
            doEmail.replace(1,i-1,"***");
            userInfo.setEmail(doEmail.toString());
        }
        return new Result().success().addMsg("查询成功！").addData("userInfo", userInfo);
    }

    @Override
    public Result uploadAvatar(MultipartFile avatarFile) {
        String suf = avatarFile.getOriginalFilename().substring(avatarFile.getOriginalFilename().lastIndexOf("."));
        String avatarUrl;
        if (!suf.equals(".jpg")) {
            return new Result().fail().addMsg("文件类型有误，只能是JPG格式");
        } else {
            try {
                Integer loginId = WebUtil.getLoginId();
                String location = avatarRootPath + loginId + "/";
                HttpServletRequest request = WebUtil.getRequest();
                avatarUrl = request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath()
                        + "/static/" + "avatar/" + WebUtil.getLoginId() + "/" + "avatar.jpg";
                UserInfo userInfo = mapper.getUserInfo(loginId);
                userInfo.setAvatarUrl(avatarUrl);
                mapper.updateById(userInfo);
                File file = new File(location + "avatar.jpg");

                if (file.exists()){
                    file.delete();
                }
                avatarFile.transferTo(new File(location + "avatar.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        return new Result().success().addMsg("上传成功！").addData("avatarUrl", avatarUrl);
    }

    @Override
    public Result changeInfo(UserInfo userInfo) {
        Integer loginId = WebUtil.getLoginId();
        if (userInfo.getCreatorId() != loginId){
            return new Result().fail().addMsg("用户无法进行此操作");
        }else {
            userInfo.setAvatarUrl(null);
            userInfo.setEmail(null);
            mapper.updateById(userInfo);
        }
        Result result = getUserInfo();
        userMapper.updateUsername(userInfo.getUsername(), loginId);
        return new Result().success().addMsg("修改成功!").addData("userInfo", result.getData().get("userInfo"));
    }
}
