# 综合管理系统（Tangdao）

<p align="left">
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/html/"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.2.2.RELEASE-brightgreen.svg"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/blob/master/LICENSE"><img alt="LICENSE" src="https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square"/></a>
</p>

<p align="left">
  <a href=“https://gitee.com/ruyangit/tangdao/stargazers"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/star.svg?theme=dark"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/members"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/fork.svg?theme=dark"/></a>
</p>


## 简介

Tangdao 是一个基于角色的授权（RBAC - Role-Based Authorization）系统，用于提供和配置（集中授权）认证策略在服务运行时的访问权限。

前端访问：[预览](https://ruyangit.gitee.io/2020/tangdao/spa)

## 架构

<img alt="data_model" src="./docs/data_model.png" width="100%" />

## 主要功能

用户，用户组，角色，资源，权限策略，服务，设置。

## 模块描述

| 模块 | 说明 | 版本 |
| --- | --- | --- |
| core-api | 基础功能 | 0.1.0 |

## 运行项目

```
1、开发环境

mvn springboot:run
```

```
2、生产环境

mvn package -f pom.xml -Dmaven.test.skip=true
```

## 引用
```

```
## 贡献
```
无
```
## 版权

Copyright 2016 ruyangit Inc.

Licensed under the Apache License, Version 2.0: <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</href>

