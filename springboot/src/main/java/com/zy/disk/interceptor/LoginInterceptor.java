package com.zy.disk.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import com.zy.disk.utils.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token != null) {
            Claims claimsByToken = JWTUtil.getClaimsByToken(token);
            String id = claimsByToken.getSubject();
            System.out.println("此请求的用户id："+id);
            return true;
        }
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            String json = "{\"code\": \"400\",\"message\": \"用户未登录！\",\"data\": {}}";
            writer.print(json);
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
        return false;
    }


}
