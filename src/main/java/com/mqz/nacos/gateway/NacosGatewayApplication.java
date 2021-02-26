package com.mqz.nacos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author mqz
 * @since
 * @description
 * @abount https://github.com/DemoMeng
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosGatewayApplication.class, args);
    }

}
