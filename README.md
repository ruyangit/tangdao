# 权限管理系统（Tangdao）

<p align="left">
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.3.8.RELEASE/reference/html/"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.3.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/blob/master/LICENSE"><img alt="LICENSE" src="https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square"/></a>
</p>

<p align="left">
  <a href=“https://gitee.com/ruyangit/tangdao/stargazers"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/star.svg?theme=dark"/></a>
  <a href="https://gitee.com/ruyangit/tangdao/members"><img alt="star" src="https://gitee.com/ruyangit/tangdao/badge/fork.svg?theme=dark"/></a>
</p>

#### 介绍
Tangdao 是基于角色的权限管理系统（RBAC），采用Springboot开发。系统简单易懂，前端使用Vue、Quasarframework开发，页面简洁美观。


#### 组件示例
<img src="https://ruyangit.gitee.io/2021/p/P_2021-04-20T05-38-19.242Z.png" width="100%" />

#### 文档说明
预览：<a href="http://121.4.215.23:30000/tangdao/admin/#/overview">Live</a> 


#### 安装教程
```
后端安装
$ git clone https://gitee.com/ruyangit/tangdao.git
$ cd tangdao
$ mvn install
$ java -jar ./tangdao-web/target/tangdao.jar &

```

```
前端安装
$ cd admin
$ npm install
$ npm install @quasar/cli -g
$ quasar build

开发运行
$ quasar dev

```

#### 开发工具设置（vs code）
```json
setting.json shift+ctrl+p

{
    "editor.formatOnPaste": true,
    "editor.formatOnSave": true,
    "editor.codeActionsOnSave": {
        "source.fixAll": true
    },
    "javascript.format.insertSpaceBeforeFunctionParenthesis": true,
    "javascript.format.placeOpenBraceOnNewLineForControlBlocks": false,
    "javascript.format.placeOpenBraceOnNewLineForFunctions": false,
    "typescript.format.insertSpaceBeforeFunctionParenthesis": true,
    "typescript.format.placeOpenBraceOnNewLineForControlBlocks": false,
    "typescript.format.placeOpenBraceOnNewLineForFunctions": false,
    "vetur.format.defaultFormatter.html": "js-beautify-html",
    "vetur.format.defaultFormatter.js": "vscode-typescript"
}
```

#### 一个部署案例（nginx）
```bash
server {
    listen 80 http2;
    server_name quasar.myapp.com;

    root /home/user/quasar.myapp.com/public;

    add_header X-Frame-Options "SAMEORIGIN";
    add_header X-XSS-Protection "1; mode=block";
    add_header X-Content-Type-Options "nosniff";

    index index.html;

    charset utf-8;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location = /favicon.ico { access_log off; log_not_found off; }
    location = /robots.txt  { access_log off; log_not_found off; }

    access_log off;
    error_log  /var/log/nginx/quasar.myapp.com-error.log error;

    location ~ /\.(?!well-known).* {
        deny all;
    }
}
```

#### 更多自定义配置
See [Configuring quasar.conf.js](https://quasar.dev/quasar-cli/quasar-conf-js).


#### References

<table class="table table-docs">
    <thead>
    <tr>
        <th class="wd-30p">Plugins</th>
        <th class="wd-70p">Link</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    </tbody>
</table>

#### 版权

Copyright 2020 ruyangit Inc.

Licensed under the Apache License, Version 2.0: <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</href>
