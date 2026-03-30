# BookManage - 图书管理系统
基于SpringBoot+MyBatis-Plus的多模块图书管理系统，采用分层架构，具备可插拔的缓存、搜索引擎和会话管理能力

## 核心产品功能
#### 用户中心
提供用户登录、注册、登出、用户管理的产品功能
#### 图书管理中心
提供图书管理、图书类目管理产品功能，包含对不同角色的权限控制

## 技术栈
##### jdk21
##### SpringBoot 2.7.15
##### MyBatis-Plus
##### 数据库：H2（题目建议，方便部署），生产需要独立数据库
####  缓存:本地Caffeine/远程redis
##### 搜索引擎：Elasticsearch 代码待完善
##### 密码加密：BCrypt（Hutool）
##### API文档：Swagger2
##### 日志：Logback


## 架构设计

### 分层架构
1. Controller层（HTTP 处理、权限校验、Session 管理） 
2. 业务层（拆分会员中心和图书管理中心）,分别依赖各自的api接口
3. Manager层，数据访问层
4. Mapper 数据库映射层

### 核心设计

#### CQRS - 读写分离
图书模块将读写操作拆分到不同的 Service：

#### 策略模式 - 可插拔查询引擎
BookQueryStrategy - 可插拔查询引擎

#### 缓存体系
统一 `CacheService` 接口，通过 `cache.type` 配置切换实现：
CaffeineCacheService：开发环境，本地内存缓存
RedisCacheService：生产环境，分布式共享缓存

#### 会话管理
基于 Cookie 的自定义会话机制，委托 `CacheService` 存储会话数据：
- 开发环境：Caffeine 本地内存存储
- 生产环境：Redis 分布式共享存储


## 快速启动

### 环境要求

- JDK 21+
- Maven 3.6+

### 开发模式（H2 数据库，无需外部依赖）

```bash
mvn clean package -DskipTests
java -jar book-manage-web/target/book-manage-web.war
```

启动后访问：
- API 服务：`http://localhost:8080`
- Swagger 文档：`http://localhost:8080/swagger-ui.html`

### 生产模式
需要配置 `application-prod.properties` 中的外部服务地址：

```bash
java -jar book-manage-web/target/book-manage-web.war --spring.profiles.active=prod
```


## 日志体系
基于 Logback 统一输出，结合 AOP 实现操作日志打印，后续还有接入traceid方便问题排查

### AOP 日志切面
WebLog + ServiceLog

## 演进规划
### 1. 前后端分离
当前后端同时承担 API 和页面渲染职责，后续向“前端独立工程部署，通过 API 对接后端”演进
### 2. Elasticsearch 部署与全文检索增强
当前存在系统es未完全接入不支持模糊查询，后续接入查询可以平滑切换，通过数据库binlog的方式，将增量变更从db同步到es
### 3. Redis 缓存体系完善
### 4. 微服务化
本系统API 模块与实现模块已分离，具备微服务拆分的基础
