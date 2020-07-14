# 权限管理系统（Tangdao）

<p align="left">
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/html/"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.2.6.RELEASE-brightgreen.svg"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/blob/master/LICENSE"><img alt="LICENSE" src="https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square"/></a>
</p>

<p align="left">
  <a href=“https://gitee.com/ruyangit/tangdao/stargazers"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/star.svg?theme=dark"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/members"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/fork.svg?theme=dark"/></a>
</p>


## 简介

Tangdao 是基于角色的权限管理系统（RBAC），采用Springboot开发。系统简单易懂，前端使用Vue、Quasarframework开发，页面简洁美观。后端核心框架使用Springboot、Mybatis-plus、SpringSecurity为主要，扩展基于框架的权限校验、参数校验、统一异常、统一响应的通用功能。

## 功能

1、系统管理，管理员用户、角色组、操作日志（开发中）

## 预览

**[前端预览](http://121.37.180.48:12001/#/user/login)**

**[前端项目](https://gitee.com/ruyangit/quasar-element-pro)**

<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/1.png' width="100%" />
<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/2.png' width="100%" />
<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/3.png' width="100%" />
<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/4.png' width="100%" />
<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/5.png' width="100%" />
<img src='https://ruyangit.gitee.io/2020/tangdao/spa/arct/6.png' width="100%" />

## 运行项目

```
1、开发环境

mvn springboot:run
```

```
2、生产环境

mvn package -f pom.xml -Dmaven.test.skip=true
```
## 参考资料

```
整理中
```
## 贡献
```
无
```
## 版权

Copyright 2020 ruyangit Inc.

Licensed under the Apache License, Version 2.0: <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</href>

