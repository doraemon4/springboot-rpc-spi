package com.stephen.learning.blade.config;

import com.stephen.learning.blade.scan.RpcScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 16:58
 * @Version: 1.0
 */
@Configuration
public class AppConfig {
    @Bean
    public RpcScannerConfigurer init(){
        String basePackage = "com.stephen.learning.blade.service";
        RpcScannerConfigurer configurer = new RpcScannerConfigurer();
        configurer.setBasePackage(basePackage);
        return configurer;
    }
}
