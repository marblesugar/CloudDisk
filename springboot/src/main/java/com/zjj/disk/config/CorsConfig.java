package com.zjj.disk.config;

import com.zjj.disk.utils.WebUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zjj
 * @create 2023-03-29 11:35
 */
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

