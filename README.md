# 综合权限管理系统（Tangdao）

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

**前端：[预览](http://121.37.180.48/#/dashboard),[前端地址](https://gitee.com/ruyangit/quasar-element-pro/tree/tangdao-spa)**

## 架构

<img alt="data_model" src="./docs/data_model.png" width="100%" />

## 主要功能

用户，用户组，角色，资源，权限策略，服务，设置。

RBAC Role-Based Authorization

PBAC Policy-Based Authorization

## 模块描述

| 模块 | 说明 | 版本 |
| --- | --- | --- |
| module-core-api | 基础数据服务 | 0.2.2 |
| module-security | 访问控制核心 | 0.2.6 |

## 访问控制

**策略元素**

```javascript
Version: <string>
Statement: [
	{
		Sid: <string>
		Effect: <Allow|Deny>
		Action: [...]
		Resource: [...]
		Condition: [
			{
				Bool,
				StringEquals,
				StringNotEquals,
				.
				.
				.
			}
		]
	}
]
```
**操作（Action）**

```
所有的服务操作都是通过通配符匹配
action:["*"] or action:["*:*"]

// iam 服务下的所有操作
action:["iam:*"]

// iam 服务下的创建，更新用户操作，多个操作同时指定
action:["iam:CreateUser","ima:UpdateUser"]

```

**判定规则**

根据参数条件判断策略对象是否通过。该方法首先查找策略，对策略的资源操作条件的组合进行显示判断，其效力具有显示拒绝，如果存在这样的策略则拒绝优先。如果没有这样的策略，通过evaluate判断如果找到这样的策略，evaluate返回true，如果找不到匹配返回false。请在[Amazon](https://docs.aws.amazon.com/IAM/latest/UserGuide/accesspolicyluage_EvaluationLogic.html) 查找到更多的详细解释。

**策略**


```javascript
[{
  "Version": "2020-01-01",
  "Statement": [{
    "Sid": "Test1",
    "Effect": "Allow",
    "Action": [
      "iam:CreateUser",
      "iam:EnableUserStatus"
    ],
    "Resource": [
      "core:iam:user/${aws:username}"
    ]
  }, {
    "Sid": "Test2",
    "Effect": "Allow",
    "Action": [
      "iam:DeleteUser"
    ],
    "Resource": [
      "core:iam:user/${aws:username}"
    ],
    "Condition": {
      "DateGreaterThan": {
        "CurrentTime": "2020-01-30T01:01:01Z"
      },
      "Bool": {
        "MultiFactorAuthPresent": true
      },
      "NumericLessThanEquals": {
        "FailedLoginAttempts": 3
      }
    }
  }]
}, {
  "Version": "2020-01-01",
  "Statement": [{
    "Sid": "Test3",
    "Effect": "Allow",
    "Action": [
      "iam:ListUsers",
      "iam:ListRoles"
    ],
    "Resource": ["*"]
  }]
}]
```
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

