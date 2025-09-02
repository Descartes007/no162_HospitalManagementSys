package com.shanzhu.hospital.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis - plus 配置
 *
 * 什么是MybatisPlus? （https://blog.csdn.net/qq_52922453/article/details/127196313）
 *
 
 * @date: 2023-11-15
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 分页插件配置
     *
     * @return 分页插件拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁配置
     *
     * @return 乐观锁拦截器
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }


}
