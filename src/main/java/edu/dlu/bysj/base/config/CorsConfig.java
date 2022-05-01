package edu.dlu.bysj.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 *
 * @create 01 21, 2022
 * @since 1.0.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路由
        registry.addMapping("/**")
         //设置允许跨域请求的域名
         .allowedOrigins("*")
         //设置允许的方法
         .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
         //是否允许证书（cookies）
         .allowCredentials(true)
         //允许跨域的时间
         .maxAge(3600)
         .allowedHeaders("*");
    }
}