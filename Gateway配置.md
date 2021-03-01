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



#Gateway 路由谓词工厂：

- 示例：

spring:
    cloud:
        gateway:
            routes:
                - id: nacos-consumer-a #服务的id，可以自定
                  uri: lb://nacos-consumer-a #负载的服务名，服务项目中的 spring.application.name
                  predicates:
                    - Path:/nacos-consumer-a/**,a/**  #Path是路由谓词，  

 