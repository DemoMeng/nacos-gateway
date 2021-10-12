# Nacos客户端  nacos-consumer-a 项目地址： https://github.com/DemoMeng/nacos-consumer-a
- 配置Nacos服务端
- 配置Portainer
- 配置Nacos配置中心
- Maven多环境配置+Nacos多环境命名空间  

# Gateway网关  nacos-gateway 项目地址： https://github.com/DemoMeng/nacos-gateway
- 配置Nacos注册中心
- 配置Nacos配置中心
- 配置Gateway全局过滤器、Routes路由转发过滤配置
- Maven多环境配置+Nacos多环境命名空间




# 为什么需要网关：
- 应用api网关是基于服务方调用方式采用api调用的方式 （服务调用方式分为RPC和API）
- 若不使用api网关可能无法实现流量监控，服务间调用混乱，
- 使用网关优点：限流、熔断、流量监控、系统鉴权、灰度发布、线上测试


# 名词解释：

- 路由（routes）：即配置中的routes，其包括：id、uri、predicates组成，如果predicates为true，这匹配该路由

- 断言（predicates）：可以匹配http请求中的cookie、header、query、path等，如果匹配成功，则进入该路由

- 过滤器（filter）： GatewayFilter实例，可以对请求前、后修改请求


# 路由配置：

- 方式1： yml中配置
````
spring:
  cloud:
    gateway:
      routes:
        - id: nacos-consumer-a # 服务名
          uri: lb://nacos-consumer-a # 需要路由到的服务地址
          predicates:
            - Path=/nacos-consumer-a/** #断言匹配的路由谓词规则
          metedata:
            response-timeout: 200 # 响应超时时间设置，  单位：毫秒
            connect-timeout: 200 # 连接超时时间设置，单位：毫秒 


````

- 方式2：代码中配置
- 基本和yml配置一致
````
@Configuration
public class Routes {
    /**
     *
     * 代码配置网关路由
     *
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator routeLocator1(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes().route("route-1",r -> r.path("/nacos-consumer-a/**").uri("lb:nacos-consumer-a")).build();
    }

}

````


# 跨域配置：

````
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "**" # **为允许所有域名访问，可以修改 http://mengqizhang.xyz
            allowedMethods:
            - GET
````


# 内置GlobalFilter过滤器：

官方文档： https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories


- AddRequestHeader :添加请求头过滤器

````
spring:
  cloud:
    gateway:
      routes:
      - id: add_request_header_route
        uri: https://example.org
        filters:
            - AddRequestHeader=version, 0.0.1 # 添加了一个version=0.0.1的请求头参数和值
````

- AddRequestParameter : 添加请求url参数

````

    filters:
        - AddRequestHeader=version, 0.0.1 # 添加了一个version=0.0.1的请求头参数和值
        
````


- StripPrefix: 截取掉多少位url
- 比如原来请求是 http://cloud.com/user/name/getByName，经过这个StripPrefix过滤器，截取掉了域名后面的两位url（不包含 / ）为：http://cloud.com/getByName
````
    filters:
        - StripPrefix=2
````

- PrefixPath： 增加域名后url的前缀，和StripPrefix相反
- 比如原来的请求是 http://cloud.com/user/name/getByName，经过这个PrefixPath后，变成了： http://cloud.com/addPath/user/name/getByName
````
    filters:
        - PrefixPath=/addPath
````

            






#Gateway 路由谓词工厂：

    Path:  请求路径校验谓词
    Cookie: 请求cookie校验谓词
    After: 请求时间校验谓词-XX范围之后
    Before: 请求时间校验谓词-XX范围之前
    Between: 请求时间校验谓词-XX-XX范围之间
    Header: 请求头校验谓词
    Host: 请求客户端Host谓词
    Method: 请求方法谓词
    Query: 请求url查询参数校验谓词
    RemoteAddr: 请求客户端ip地址谓词
    Weight: 
    


````
##请求路径校验谓词示例：
spring:
    cloud:
        gateway:
            routes:
                - id: nacos-consumer-a #服务的id，可以自定
                  uri: lb://nacos-consumer-a #负载的服务名，服务项目中的 spring.application.name
                  predicates:
                    - Path=/nacos-consumer-a/**,a/**  #Path是路由谓词， 
                    
                    
##请求cookie校验谓词：

    predicates:
        - Path=/user/** #可以两个谓词共同使用
        - Cookie=version,gray # Cookie种version=gray请求会打到该节点，这里也能使用正则表达式！！
        
##请求时间校验谓词-XX范围之后示例：

    predicates:
        - After=2019-09-15T21:05:50.187697+08:00[Asia/Shanghai] # 这里的时间格式是java种的ZonedDateTime对象
        
        
##请求时间校验谓词-XX范围之前示例：

    predicates:
        - Before=2019-09-10T21:05:50.187697+08:00[Asia/Shanghai] # 这里的时间格式是java种的ZonedDateTime对象
        
        
##请求时间校验谓词-XX-XX范围之间示例：

    predicates:
        - Between=2019-09-10T21:05:50.187697+08:00[Asia/Shanghai],2019-09-15T21:05:50.187697+08:00[Asia/Shanghai] # 这里的时间格式是java种的ZonedDateTime对象
        

##请求头校验谓词示例：

    predicates:
        - Header=token,^(?!\d+$)[\da-zA-Z]+$ # 这里可以使用正则表达式进行匹配指定的格式的token值


##请求Host校验谓词示例：


    predicates:
        - Host=**.megnqizhang.xyz,**.aliyun.com  # 匹配域名下的子域名可访问，逗号隔开表示多个值

##请求参数校验谓词示例：


    predicates:
        - Query=token # Query匹配 支持传入两个参数，一个是属性名，一个为属性值，属性值可以是正则表达式。
        - Query=token, \d+ # 只有属性名token，表示只要有token参数就行，不管值是什么，即可路由


##请求方法校验谓词示例：


    predicates:
        - Method=GET,POST # GET、POST、DELETE、PUT。

