# api_parent

api 网关项目：管理员可以接入并发布接口，可视化各接口调用情况；用户可以开通接口调用权限、浏览接口及在线调试，并通过客户端 SDK 轻松调用接口；

### api-backend：

api网关项目后端支撑，web后端，此模块向前端提供接口，此模块包括 用户管理，接口管理。

### api-client-sdk：

spring boot start 自行开发的sdk模块，直接install 安装到Maven 仓库。

首先将pom文件中的build文件并添加依赖 **configuration-processor**

```xml
!-- 必须要有configuration-processor依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

注册配置类 resources/META_INF/spring.factories

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.yupi.yuapiclientsdk.YuApiClientConfig
```

mvn install 打包为本地依赖创建新项目（复用 server 项目）、测试

### api-client：

公共接口与公共实体类，通过Dubbo进行远程调用

### api-gateway：

gateway网关，主要作用是 转发请求 统一鉴权 统一日志   访问控制 统一业务请求 

### api-interface：

提供接口服务，通过sdk来进行调通接口
