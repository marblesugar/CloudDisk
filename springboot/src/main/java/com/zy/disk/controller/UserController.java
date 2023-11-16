package com.zy.disk.controller;

import com.zy.disk.controller.result.Result;
import com.zy.disk.pojo.EmailDTO;
import com.zy.disk.pojo.PasswordDTO;
import com.zy.disk.pojo.User;
import com.zy.disk.service.IUserService;
import com.zy.disk.utils.ValidUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @author zjj
 * @create 2023-03-25 20:53
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    /**
     * 用户注册
     * @param user 接收User（username，email，password）实体类
     * @param result 保存错误信息
     * @return
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody User user, BindingResult result){
        HashMap<String,String> map = ValidUtil.vaild(result);
        if (!map.isEmpty()){
            return Result.success().addMsg("请求参数格式有误").addData("errors", map);
        }
        if (!service.register(user)){
            return Result.fail().addMsg("注册失败！该邮箱已注册或未激活");
        }
        return Result.success().addMsg("注册成功！请前往邮箱验证");
    }

    /**
     * 激活账户
     * @param activationCode 用户的激活码
     * @return
     */
    @GetMapping("/active")
    public Result active(@RequestParam("activationCode") String activationCode){
        Integer type = service.active(activationCode);
        if (1 == type){
            return Result.fail().addMsg("激活链接不存在！");
        }else if(2 == type){
            return Result.fail().addMsg("激活链接已失效，请重新注册！");
        }
        return Result.success().addMsg("账户激活成功！");
    }

    @PostMapping("/login")
    public Result login(@Valid @RequestBody User user, BindingResult result){
        HashMap<String,String> map = ValidUtil.vaild(result);
        if (!map.isEmpty()){
            return Result.fail().addMsg("请求参数格式有误").addData("errors", map);
        }
        return service.login(user);
    }

    /**
     * 修改密码
     * @param passwordDTO
     * @return
     */
    @PostMapping("/changePassword")
    public Result changePassword(@Valid @RequestBody PasswordDTO passwordDTO, BindingResult result){
        HashMap<String, String> map = ValidUtil.vaild(result);
        if (!map.isEmpty()){
            return Result.fail().addMsg("请求参数格式有误").addData("errors", map);
        }
        return service.changePassword(passwordDTO);
    }

    /**
     * 发送邮件
     * @param status 1为给登录用户邮箱发送,2为给新邮箱发送
     * @param email
     * @return
     */
    @GetMapping("/sendValidationCode")
    public Result sendValidationCode(Integer status, @RequestParam(required = false) String email){
        return service.sendValidationCode(status, email);
    }

    /**
     * 验证验证码
     * @param status
     * @param validationCode
     * @return
     */
    @GetMapping("/verifyValidationCode")
    public Result verifyValidationCode(Integer status, String validationCode){
        return service.verifyValidationCode(status, validationCode);
    }
    /**
     * 修改邮箱
     * @param emailDTO (邮箱，验证码)
     * @return
     */
    @PostMapping("/changeEmail")
    public Result changeEmail(@RequestBody @Valid EmailDTO emailDTO, BindingResult result){
        HashMap<String, String> map = ValidUtil.vaild(result);
        if (!map.isEmpty()){
            return new Result().fail().addMsg("参数有误").addData("errors", map);
        }
        return service.changeEmail(emailDTO);
    }

    /**
     * 注销账号
     * @return
     */
    @GetMapping("/cancelAccount")
    public Result cancelAccount(){
        return service.cancelAccount();
    }


}
