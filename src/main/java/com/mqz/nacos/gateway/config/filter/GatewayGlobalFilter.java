package com.mqz.nacos.gateway.config.filter;

import com.mqz.nacos.gateway.config.exception.WithoutLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author mqz
 * @description
 * @abount https://github.com/DemoMeng
 * @since 2021/2/26
 */
@Component
@RefreshScope  //动态刷新读取Nacos中的配置文件
public class GatewayGlobalFilter implements GlobalFilter,Ordered{

    @Value(value = "${about}")
    private String about;

    private static List<String> needPermission;
    static{
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("/nacos-consumer-a/a/gateway");
        needPermission = Collections.unmodifiableList(list);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("网关读取配置文件中的变量："+about);



        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        URI uri = serverHttpRequest.getURI();
        System.out.println(uri.getPath());

        //URL请求参数
        MultiValueMap<String,String> map = serverHttpRequest.getQueryParams();
        for(String key:map.keySet()){
            System.out.println("请求参数：【key:"+key+"】【value:"+map.get(key)+"】");
        }
        //Header参数
        HttpHeaders headers = serverHttpRequest.getHeaders();
        for(String key:headers.keySet()){
            System.out.println("请求头参数：【key："+key+"】【value:"+headers.get(key).toString()+"】");
        }
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        System.out.println(body);

        //根据请求路径拦截、放行指定的请求
        String path = uri.getPath();
        if(needPermission.contains(path)){
            String tokenValue = serverHttpRequest.getQueryParams().getFirst("token");
            if(StringUtils.isEmpty(tokenValue)){
                throw new WithoutLoginException();
            }
        }

        String tokenValue = serverHttpRequest.getQueryParams().getFirst("token");
        if(StringUtils.isEmpty(tokenValue)){
            throw new WithoutLoginException("请携带登录的token");
        }
        //模拟解析token
        String userId = tokenValue.split("zzz")[1];


        StringBuilder query = new StringBuilder();
        //获取请求uri的请求参数（GET请求参数通过拼接key=value形式进行传参）
        String originalQuery = uri.getRawQuery();

        //判断最后一个字符是否是&，如果不是则拼接一个&，以备后续的参数进行连接
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }
        //获取config中的key、value，然后拼接到uri请求参数后面
        // TODO urlencode?
        query.append("GATEWAY_USER_ID");
        query.append('=');
        query.append(userId);

        //把请求参数重新拼接回去，并放入request中传递到过滤链的下一个请求中去
        try {
            URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();
            ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(request).build());
        }catch (RuntimeException ex) {
            throw new IllegalStateException("Invalid URI query: " + query.toString());
        }
//        return chain.filter(exchange);
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
