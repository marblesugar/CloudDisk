package com.zy.disk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zy.disk.utils.WebUtil;

import javax.servlet.http.HttpServletResponse;


//@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowedOriginPatterns("*") // 允许跨域访问的源
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS", "HEAD") // 允许请求方法
                .maxAge(168000) // 预检时间间隔
                .allowCredentials(true) // 是否发送cookie
                .allowedHeaders("*"); // 允许头部设置
    }
}

