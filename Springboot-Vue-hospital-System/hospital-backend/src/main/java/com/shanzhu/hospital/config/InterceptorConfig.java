package com.shanzhu.hospital.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 
 * @date: 2023-11-10
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new JwtInterceptor())
                .addPathPatterns("/**")
                //文件导出
                .excludePathPatterns("/patient/pdf")
                //登录
                .excludePathPatterns("/**/login")
                //病患注册
                .excludePathPatterns("/**/addPatient");
    }

}
