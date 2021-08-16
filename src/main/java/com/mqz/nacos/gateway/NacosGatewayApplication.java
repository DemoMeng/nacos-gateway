package com.mqz.nacos.gateway;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author mqz
 * @since
 * @description
 * @abount https://github.com/DemoMeng
 */
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(value = {"com.mqz"})//需要扫描到底层包mars项目
@Configuration
public class NacosGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosGatewayApplication.class, args);
    }

}
