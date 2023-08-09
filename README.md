# api_parent

api 网关项目 整合微服务

## api-backend -> api网关项目后端支撑，web后端
## api-client-sdk -> spring boot start 自行开发的sdk模块，直接install 安装到Maven 仓库
## api-client -> 公共接口与公共实体类，通过Dubbo进行远程调用
## api-gateway -> gateway网关，主要作用是 转发请求 统一鉴权 统一日志   访问控制 统一业务请求 
## api-interface 提供接口服务，通过sdk来进行调通接口
