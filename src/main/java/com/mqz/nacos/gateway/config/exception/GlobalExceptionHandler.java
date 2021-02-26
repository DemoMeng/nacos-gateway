package com.mqz.nacos.gateway.config.exception;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mqz
 * @description
 * @abount https://github.com/DemoMeng
 * @since 2021/2/5
 */
@Order(Integer.MIN_VALUE)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        System.out.println("GLOBAL EXCEPTION:{}, {}");
        Map<String,Object> map = new HashMap<>();
        if (throwable instanceof WithoutLoginException) {
            System.out.println("WithoutLoginException:{}");
            map.put("code",500);
            map.put("message","未登录");
            map.put("data",null);
        }
        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        return serverWebExchange.getResponse().writeWith(Flux.just(bufferFactory.wrap(JSON.toJSONString(map).getBytes())));
    }
}
