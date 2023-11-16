package com.zy.disk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zy.disk.controller.result.Result;
import com.zy.disk.mapper.FileMapper;
import com.zy.disk.mapper.UserInfoMapper;
import com.zy.disk.mapper.UserMapper;
import com.zy.disk.pojo.EmailDTO;
import com.zy.disk.pojo.PasswordDTO;
import com.zy.disk.pojo.User;
import com.zy.disk.pojo.UserInfo;
import com.zy.disk.pojo.ValidationCode;
import com.zy.disk.service.IUserService;
import com.zy.disk.utils.JWTUtil;
import com.zy.disk.utils.MD5Util;
import com.zy.disk.utils.MailUtil;
import com.zy.disk.utils.SignUtil;
import com.zy.disk.utils.WebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


@Transactional
@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserInfoMapper  userInfoMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private MailUtil mailUtil;
    @Value("${application.uploadfile}")
    private String fileRootPath;

    @Value("${application.avatarfile}")
    private String avatarRootPath;

    private ValidationCode validationCode;

    private Boolean allowChangeEmail = false;
    private Boolean allowCancelAccount = false;


    @Override
    public Boolean register(User user) {
        String activationCode = UUID.randomUUID().toString(); //生成确认码（雪花算法更好）
        String salt = SignUtil.random(); //生成盐
        String passwordMD5 = MD5Util.MD5(user.getPassword() + salt); //md5加密密码
        LocalDateTime activationTime = LocalDateTime.now().plusMinutes(5); //设置激活过期时间
        user.setActivationCode(activationCode);
        user.setSalt(salt);
        user.setPassword(passwordMD5);
        user.setActivationTime(activationTime);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        if (mapper.selectList(lambdaQueryWrapper).size()>0){
            return false;
        }
        String activationUrl = "http://localhost/disk/user/active?activationCode="+activationCode;
        mailUtil.sendMail(activationUrl, user.getEmail()); //异步发送邮件
        return mapper.insert(user)>0;
    }

    @Override
    public Integer active(String activationCode) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getActivationCode, activationCode);
        List<User> users = mapper.selectList(lambdaQueryWrapper);
        if (0==users.size()){
            return 1;
        }
        User user = users.get(0);
        if (LocalDateTime.now().isAfter(user.getActivationTime()) && 0 == user.getIsVaild()){
            mapper.deleteById(user);
            return 2;
        }else {
            user.setIsVaild(1);
            mapper.updateById(user);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            userInfo.setCreatorId(user.getId());
            userInfo.setAvatarUrl("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
            userInfoMapper.insert(userInfo);
        }

        File file = new File(fileRootPath + user.getId());
        if(!file.exists()){
            file.mkdir();
        }
        File file1 = new File(avatarRootPath + user.getId());
        if (!file1.exists()){
            file1.mkdir();
        }
        return 3;
    }

    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        List<User> users = mapper.selectList(lambdaQueryWrapper);
        if (users.size()>1){
            return Result.fail().addMsg("账号异常！请联系管理员");
        }
        if (1 == users.size()){
            if (users.get(0).getIsVaild() == 0){
                return Result.fail().addMsg("请前往邮箱激活账号");
            }else {
                String passwordMD5 = MD5Util.MD5(user.getPassword() + users.get(0).getSalt());
                if(passwordMD5.equals(users.get(0).getPassword())){
                    String token = JWTUtil.generateToken(users.get(0).getId());
                    return Result.success().addMsg("登录成功！").addData("token",token);
                }
            }
        }
        return Result.fail().addMsg("登录失败，邮箱或密码错误");
    }

    @Override
    public Result getUserName() {
        Integer loginId = WebUtil.getLoginId();
        User user = mapper.selectById(loginId);
        if (user != null){
            return new Result().success().addMsg("获取用户名成功！").addData("username", user.getUsername());
        }
        return new Result().fail().addMsg("用户不存在！");
    }

    @Override
    public Result changePassword(PasswordDTO passwordDTO) {
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();
        Integer loginId = WebUtil.getLoginId();
        User user = mapper.selectById(loginId);

        if (MD5Util.MD5(oldPassword+user.getSalt()).equals(user.getPassword())){
            if (newPassword.equals(confirmPassword)){
                if (newPassword.equals(oldPassword)){
                    return new Result().fail().addMsg("两次密码不能一致");
                }else {
                    String newPasswordMD5 = MD5Util.MD5(newPassword + user.getSalt());
                    user.setPassword(newPasswordMD5);
                }
            }else {
                return new Result().fail().addMsg("两次密码不一致");
            }
        }else {
            return new Result().fail().addMsg("密码错误");
        }
        mapper.updateById(user);

        return new Result().success().addMsg("修改成功！");
    }

    @Override
    public Result sendValidationCode(Integer status, String email) {
        String sendEmail = null;
        String code = ("" + Math.random()).substring(2, 8);
        Long expiredTime = new Date().getTime()+120000;
        if (status==1||status==3){
            Integer loginId = WebUtil.getLoginId();
            User user = mapper.selectById(loginId);
            sendEmail = user.getEmail();
        }else if (status==2){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getEmail, email);
            if (mapper.selectCount(lambdaQueryWrapper)>0){
                return new Result().fail().addMsg("该邮箱已被注册！");
            }

            String pattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
            if (StringUtils.isBlank(email)){
                return new Result().fail().addMsg("邮箱不能为空");
            }else if (!Pattern.matches(pattern, email)){
                return new Result().fail().addMsg("邮箱地址不合法");
            }
            sendEmail = email;
        }
        this.validationCode = new ValidationCode(sendEmail, code, expiredTime, status);
        mailUtil.sendValidationCode(code, sendEmail, status);
        return new Result().success().addMsg("邮件发送成功！");
    }

    @Override
    public Result verifyValidationCode(Integer status, String validationCode) {
        Integer loginId = WebUtil.getLoginId();
        User user = mapper.selectById(loginId);
        if (status!=this.validationCode.getStatus()){
            return Result.fail().addMsg("操作有误");
        }

        if (status!=2 && !user.getEmail().equals(this.validationCode.getEmail())){
            return Result.fail().addMsg("身份认证有误");
        }

        if (StringUtils.isBlank(validationCode)){
            return new Result().fail().addMsg("验证码验证码不能为空");
        }
        long time = new Date().getTime();
        if (this.validationCode.getExpiredTime() < time){
            return new Result().fail().addMsg("验证码已过期");
        }else if (!this.validationCode.getCode().equals(validationCode)){
            return new Result().fail().addMsg("验证码有误");
        }
        if (this.validationCode.getStatus() == 1){
            this.allowChangeEmail = true;
        }else if (this.validationCode.getStatus() == 3){
            this.allowCancelAccount = true;
        }
        return new Result().success().addMsg("验证码正确");
    }

    @Override
    public Result changeEmail(EmailDTO emailDTO) {
        Result result = verifyValidationCode(2, emailDTO.getValidationCode());
        if (result.getCode()==400){
            return result;
        }else if(!this.allowChangeEmail){
            return new Result().fail().addMsg("请先验证旧邮箱！");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, emailDTO.getEmail());
        Long count = mapper.selectCount(lambdaQueryWrapper.eq(User::getEmail, emailDTO.getEmail()));
        if (count>0){
            return new Result().fail().addMsg("该邮箱已被注册！");
        }
        Integer loginId = WebUtil.getLoginId();
        User user = mapper.selectById(loginId);
        user.setEmail(emailDTO.getEmail());
        mapper.updateById(user);
        UserInfo userInfo = userInfoMapper.getUserInfo(loginId);
        userInfo.setEmail(emailDTO.getEmail());
        userInfoMapper.updateById(userInfo);
        this.allowChangeEmail = false;
        return new Result().success().addMsg("修改成功！");
    }

    @Override
    public Result cancelAccount() {
        Integer loginId = WebUtil.getLoginId();
        if (this.allowCancelAccount){
            mapper.deleteById(loginId);
            userInfoMapper.deleteById(userInfoMapper.getUserInfo(loginId));
            LambdaQueryWrapper<com.zy.disk.pojo.File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(com.zy.disk.pojo.File::getCreatorId, loginId);
            fileMapper.delete(lambdaQueryWrapper);
            //TODO 销毁文件(硬盘，数据库)逻辑
            String filePath = fileRootPath + loginId;
            String avatarPath = avatarRootPath + loginId;
            cancelAccountDeleteDirAndFile(filePath);
            cancelAccountDeleteDirAndFile(avatarPath);

        }else {
            return new Result().fail().addMsg("请先验证旧邮箱！");
        }
        this.allowCancelAccount = false;
        return new Result().success().addMsg("账户注销成功！");
    }

    public void cancelAccountDeleteDirAndFile(String path){
        File file = new File(path);
        if (!file.isDirectory()){
            file.delete();
        }else {
            for (String child : file.list()) {
                File childFile = new File(file.getAbsoluteFile() + "/" + child);
                if (!childFile.isDirectory()){
                    childFile.delete();
                }else {
                    cancelAccountDeleteDirAndFile(file.getAbsoluteFile() + "/" + child);
                }
            }
        }
        file.delete();
    }
}
