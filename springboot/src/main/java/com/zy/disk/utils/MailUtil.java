package com.zy.disk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;


@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Async
    public void sendMail(String activationUrl, String email){
        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            helper.setSubject("disk账户激活注册");
            // 设置邮件发送者
            helper.setFrom(mailUsername);
            // 设置接收方
            helper.setTo(email);
            // 设置抄送人
            // helper.setCc();
            // 设置秘密抄送人
            // helper.setBcc();
            // 设置邮件发送日期
            helper.setSentDate(new Date());
            // 创建上下文环境
//            Context context = new Context();
//            context.setVariable("activationUrl", activationUrl);
//            String text = templateEngine.process("active-account.html", context);
            //设置邮件正文
            helper.setText("您的激活链接是：<b><a href='"+activationUrl+"'>"+activationUrl+"</a></b> 点击进行激活<br>（有效期为5分钟）",true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //发送邮件
        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendValidationCode(String code, String email, Integer status){
        //创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            if (status == 1||status == 2){
                helper.setSubject("disk账户修改邮箱");
            }else if (status == 3){
                helper.setSubject("disk注销账户");
            }
            // 设置邮件发送者
            helper.setFrom(mailUsername);
            // 设置接收方
            helper.setTo(email);
            // 设置抄送人
            // helper.setCc();
            // 设置秘密抄送人
            // helper.setBcc();
            // 设置邮件发送日期
            helper.setSentDate(new Date());
            // 创建上下文环境
//            Context context = new Context();
//            context.setVariable("activationUrl", activationUrl);
//            String text = templateEngine.process("active-account.html", context);
            //设置邮件正文
            helper.setText("您的验证码是：<b>"+code+"</b><br>（有效期为120秒）", true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //发送邮件
        javaMailSender.send(mimeMessage);
    }
}
