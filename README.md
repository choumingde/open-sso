# open-sso

#### 介绍
{**open-sso统一登录服务/单点登录/集群**
基于Spring Boot 搭建的sso统一登录平台 [https://github.com/choumingde/open-sso](https://github.com/choumingde/open-sso)}

#### 软件架构
    Java 1.8
    Maven 3.6.3
    Spring Boot 2.1.0.RELEASE
    Redis 6.2.1

#### 安装教程

# 客户端配置

1.接入方引入

    <dependency>
        <groupId>cn.cmd</groupId>
        <artifactId>open-sso-starter</artifactId>
        <version>${project.parent.version}</version>
    </dependency>

2.修改配置

    #sso服务端地址
    sso.server.url=http://opensso.cmd.cn:8080 
    #支持的应用的appId
    sso.server.appId=demo1
    #支持的应用的appSecret
    sso.server.appSecret=123456 
    #排除的urls
    sso.server.excludeUrls= 

# 服务端配置

1.修改open-sso-server配置

    #sso服务端地址
    sso.server.url=http://opensso.cmd.cn:8080
    #支持的应用的appId
    sso.server.appId=server1
    #支持的应用的appSecret
    sso.server.appSecret=123456
    #排除的urls
    sso.server.excludeUrls=/login,/logout,/oauth2/*,/custom/*,/assets/*
    #session存储方式（redis和local）
    sso.server.session.manager=redis
    #单点登录超时时间（默认7200）
    sso.server.timeout=7200
    
    #redis地址
    spring.redis.host=opensso.cmd.cn
    #redis端口
    spring.redis.port=6379
    #redis密码
    spring.redis.password=
    spring.session.store-type=redis

2.实现接口
    
    1.需要实现 cn.cmd.opensso.server.service.impl.AppServiceImpl 实现接入应用appId和appSecret
    2.需要实现 cn.cmd.opensso.server.service.impl.UserServiceImpl 实现用户账号管理

3.修改host文件

    127.0.0.1 demo.cmd.cn #测试demo访问地址(http://demo.cmd.cn:8082/demo)
    127.0.0.1 opensso.cmd.cn #sso服务器地址(http://opensso.cmd.cn:8080)
    

#### 使用说明

    1.  open-sso-demo作为参考
    2.  尚未支持sso服务端前后端分离
    3.  需自行实现用户和接入管理功能

#### 参与贡献
    
    1.  choumingde

#### 特技

    1.  支持单机和集群方式sso登录

#### 致敬

    1.  参考 [https://gitee.com/a466350665/smart-sso?_from=gitee_search](https://gitee.com/a466350665/smart-sso?_from=gitee_search) 致敬

