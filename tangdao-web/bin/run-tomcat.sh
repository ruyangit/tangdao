#!/bin/sh
echo ""
echo "[信息] 使用 Spring Boot Tomcat 运行 Web 工程。"
echo ""
cd ..
mvn clean spring-boot:run -Dmaven.test.skip=true -U
