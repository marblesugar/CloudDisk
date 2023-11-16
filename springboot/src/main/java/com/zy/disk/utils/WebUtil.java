package com.zy.disk.utils;

import io.jsonwebtoken.Claims;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WebUtil {

    /** 获取request对象 **/
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }
    /** 获取response对象 **/
    public static HttpServletResponse getResponse(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }

    /**
     *
     * @return 登录用户的email
     */
    public static Integer getLoginId(){
        Claims token = JWTUtil.getClaimsByToken(getRequest().getHeader("token"));
        return Integer.parseInt(token.getSubject());
    }
}
