//package com.mqz.nacos.gateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// *  版权所有 © Copyright 2012<br>
// *
// * @Author： 蒙大拿
// * @Date：2021/10/12 1:55 下午
// * @Description
// * @About： https://github.com/DemoMeng
// */
//@Configuration
//public class Routes {
//
//
//    /**
//     *
//     * 代码配置网关路由
//     *
//     * @param routeLocatorBuilder
//     * @return
//     */
//    @Bean
//    public RouteLocator routeLocator1(RouteLocatorBuilder routeLocatorBuilder){
//        return routeLocatorBuilder.routes().route("route-1",r -> r.path("/nacos-consumer-a/**").uri("lb://nacos-consumer-a")).build();
//    }
//
//}
