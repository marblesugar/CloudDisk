package com.zjj.disk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.disk.pojo.EmailDTO;
import com.zjj.disk.pojo.User;
import com.zjj.disk.controller.result.Result;
import com.zjj.disk.pojo.PasswordDTO;

/**
 * @author zjj
 * @create 2023-03-25 20:51
 */
public interface IUserService extends IService<User> {
    /**
     * 用户注册，发送账户激活邮件
     * @param user
     * @return
     */
    Boolean register(User user);

    /**
     * 激活账户
     *
     * @param activationCode
     * @return
     */
    Integer active(String activationCode);

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    Result login(User user);

    /**
     * 获得当前登录用户的username
     * @return
     */
    Result getUserName();

    /**
     * 修改密码
     * @param passwordDTO
     * @return
     */
    Result changePassword(PasswordDTO passwordDTO);

    /**
     * 发送邮件
     * @param status 1为给登录用户邮箱发送,2为给新邮箱发送
     * @param email
     * @return
     */
    Result sendValidationCode(Integer status, String email);

    /**
     * 验证验证码
     * @param status
     * @param validationCode
     * @return
     */
    Result verifyValidationCode(Integer status, String validationCode);

    /**
     * 修改邮箱
     * @param emailDTO (邮箱，验证码)
     * @return
     */
    Result changeEmail(EmailDTO emailDTO);

    /**
     * 注销账号
     * @return
     */
    Result cancelAccount();
}
