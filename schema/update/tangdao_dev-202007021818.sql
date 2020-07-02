-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: huawei-server    Database: tangdao_dev
-- ------------------------------------------------------
-- Server version	5.7.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_by_name` varchar(100) DEFAULT NULL COMMENT '用户名称',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `request_uri` varchar(500) DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `request_params` longtext COMMENT '操作提交的数据',
  `class_name` varchar(200) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(200) DEFAULT NULL COMMENT '方法名',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `is_exception` char(1) DEFAULT NULL COMMENT '是否异常',
  `exception_name` varchar(500) DEFAULT NULL COMMENT '异常名称',
  `exception_info` text COMMENT '异常信息',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称/操作系统',
  `browser_name` varchar(100) DEFAULT NULL COMMENT '浏览器名称',
  `execute_time` decimal(19,0) DEFAULT NULL COMMENT '执行时间',
  `service_name` varchar(200) DEFAULT NULL COMMENT '服务名称',
  `operation` varchar(500) DEFAULT NULL COMMENT '操作信息',
  `server_addr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='审计日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES ('1277423102933872641','用户详情','1270957117494263809','ruyang','2020-06-29 10:06:16','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',506,NULL,'访问【ruyang】详情信息接口','http://172.28.221.9:4001'),('1277432985590812673','用户认证',NULL,NULL,'2020-06-29 10:45:32','/admin/login','POST',NULL,NULL,NULL,'172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：172.28.221.9。','http://172.28.221.9:4001'),('1277434615631564801','用户认证','1270957117494263809','ruyang','2020-06-29 10:52:01','/admin/login','POST',NULL,NULL,NULL,'172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：172.28.221.9。','http://172.28.221.9:4001'),('1277435164821147649','用户详情','1270957117494263809','ruyang','2020-06-29 10:54:12','/admin/users/detail','GET','{\"username\":\"test01\"}','com.tangdao.web.controller.admin.UserController','detail','172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',22,NULL,'访问【test01】详情信息接口','http://172.28.221.9:4001'),('1277488791937961986','用户详情','1270957117494263809','ruyang','2020-06-29 14:27:17','/admin/users/detail','GET','{\"username\":\"test01\"}','com.tangdao.web.controller.admin.UserController','detail','172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',62,NULL,'访问【test01】详情信息接口','http://172.28.221.9:4001'),('1277880867477295106','用户认证','1270957117494263809','ruyang','2020-06-30 16:25:15','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：127.0.0.1。','http://127.0.0.1:4001'),('1277901662173859842','用户详情','1270957117494263809','ruyang','2020-06-30 17:47:53','/admin/users/detail','GET','{\"username\":\"log\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',57,NULL,'访问【log】详情信息接口','http://127.0.0.1:4001'),('1277904685033918466','用户详情','1270957117494263809','ruyang','2020-06-30 17:59:54','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',21,NULL,'访问【ruyang】详情信息接口','http://127.0.0.1:4001'),('1277904695288991746','用户详情','1270957117494263809','ruyang','2020-06-30 17:59:56','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',21,NULL,'访问【ruyang】详情信息接口','http://127.0.0.1:4001'),('1277904914487513090','用户认证','1271377165513981954','test03','2020-06-30 18:00:49','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【test03】登录，IP地址：127.0.0.1','http://127.0.0.1:4001'),('1277904973581062146','用户认证','1270957117494263809','ruyang','2020-06-30 18:01:03','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：127.0.0.1','http://127.0.0.1:4001'),('1277908223579459585','用户详情','1270957117494263809','ruyang','2020-06-30 18:13:58','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',21,NULL,'访问【ruyang】详情信息接口','http://127.0.0.1:4001'),('1277921738260287490','用户详情','1270957117494263809','ruyang','2020-06-30 19:07:40','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',22,NULL,'访问【ruyang】详情信息接口','http://127.0.0.1:4001'),('1277923696421441538','用户详情','1270957117494263809','ruyang','2020-06-30 19:15:27','/admin/users/detail','GET','{\"username\":\"log\"}','com.tangdao.web.controller.admin.UserController','detail','172.28.221.9','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',39,NULL,'访问【log】详情信息接口','http://172.28.221.9:4001'),('1278129338847399938','用户认证','1270957117494263809','ruyang','2020-07-01 08:52:36','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：127.0.0.1','http://127.0.0.1:4001'),('1278129675863920642','用户详情','1270957117494263809','ruyang','2020-07-01 08:53:56','/admin/users/detail','GET','{\"username\":\"test02\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',24,NULL,'访问【test02】详情信息接口','http://127.0.0.1:4001'),('1278133577418084353','用户详情','1270957117494263809','ruyang','2020-07-01 09:09:26','/admin/users/detail','GET','{\"username\":\"test02\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',29,NULL,'访问【test02】详情信息接口','http://127.0.0.1:4001'),('1278152747736174594','用户详情','1270957117494263809','ruyang','2020-07-01 10:25:37','/admin/users/detail','GET','{\"username\":\"ruyang\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',26,NULL,'访问【ruyang】详情信息接口','http://127.0.0.1:4001'),('1278207125402710017','用户认证','1270957117494263809','ruyang','2020-07-01 14:01:41','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：127.0.0.1','http://127.0.0.1:4001'),('1278250692225581057','用户详情','1270957117494263809','ruyang','2020-07-01 16:54:48','/admin/users/detail','GET','{\"username\":\"test01\"}','com.tangdao.web.controller.admin.UserController','detail','127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',80,NULL,'访问【test01】详情信息接口','http://127.0.0.1:4001'),('1278502401506025474','用户认证','1270957117494263809','ruyang','2020-07-02 09:35:01','/admin/login','POST',NULL,NULL,NULL,'127.0.0.1','0',NULL,NULL,'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0','Windows 10 or Windows Server 2016','Firefox',0,NULL,'用户【ruyang】登录，IP地址：127.0.0.1','http://127.0.0.1:4001');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `p_id` varchar(128) NOT NULL COMMENT '父节点',
  `p_ids` varchar(500) DEFAULT NULL,
  `name` varchar(128) NOT NULL COMMENT '权限名称',
  `path` varchar(256) DEFAULT NULL COMMENT '地址',
  `permission` varchar(128) DEFAULT NULL,
  `sort` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '排序',
  `menu_type` char(1) NOT NULL DEFAULT '0' COMMENT '类型，1-路由地址，2-功能权限',
  `is_show` char(1) NOT NULL DEFAULT '0' COMMENT '是否显示，1-是，0-否',
  `status` varchar(32) NOT NULL DEFAULT '0' COMMENT '状态',
  `remark` varchar(256) DEFAULT NULL,
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '更新日期',
  `icon` varchar(128) DEFAULT NULL,
  `badge` varchar(100) DEFAULT NULL COMMENT '徽章',
  `opened` char(1) NOT NULL DEFAULT '0' COMMENT '是否显示，1-是，0-否',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES ('1','0','0,','系统设置','/system',NULL,90,'1','1','0',NULL,NULL,NULL,'broken_image',NULL,'0'),('10','8','0,1,8','测试2','/test/test2',NULL,2,'1','1','0',NULL,NULL,NULL,NULL,NULL,'0'),('11','0','0,','商城','/shop',NULL,30,'1','1','0',NULL,NULL,NULL,NULL,NULL,'0'),('12','0','0,','内容','/cms',NULL,50,'1','1','0',NULL,NULL,NULL,NULL,NULL,'0'),('13','0','0,','财务','/account',NULL,80,'1','1','0',NULL,NULL,NULL,NULL,NULL,'0'),('14','1','0,1,','配置管理','/system/config',NULL,5,'1','1','0',NULL,NULL,NULL,'developer_board',NULL,'0'),('15','3','0,1,2,3,','用户新增',NULL,'admin:users:POST',2,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('16','3','0,1,2,3,','用户修改密码',NULL,'admin:users:password:modify:POST',2,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('17','3','0,1,2,3,','用户删除',NULL,'admin:users:delete:POST',2,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('18','7','0,1,2,7','角色新增',NULL,'admin:roles:POST',3,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('19','1','0,1,','监控信息','/system/metrics',NULL,10,'1','1','0',NULL,NULL,NULL,'developer_board',NULL,'0'),('2','1','0,1,','管理员','/system/admin',NULL,1,'1','1','0',NULL,NULL,NULL,'group','New','0'),('3','2','0,1,2,','用户列表',NULL,'admin:users:GET',0,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('4','3','0,1,2,3,','用户详情',NULL,'admin:users:detail:GET',1,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('6','3','0,1,2,3,','用户编辑',NULL,'admin:users:update:POST',2,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('7','2','0,1,2,','角色列表',NULL,'admin:roles:GET',3,'2','0','0',NULL,NULL,NULL,NULL,NULL,'0'),('8','1','0,1,','测试','/test',NULL,2,'1','1','0',NULL,NULL,NULL,'developer_board',NULL,'0'),('9','8','0,1,8','测试1','/test/test1',NULL,2,'1','1','0',NULL,NULL,NULL,NULL,NULL,'0');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `role_name` varchar(128) NOT NULL COMMENT '角色名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `status` varchar(32) NOT NULL DEFAULT '0' COMMENT '状态',
  `created` datetime NOT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_role_name` (`role_name`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('1','系统管理员','客户方使用的系统管理员，用于一些常用的基础数据配置。','0','2020-06-05 11:28:00',NULL),('2','财务','','0','2020-06-05 11:28:02',NULL),('3','商铺管理员','','0','2020-06-05 11:28:04',NULL),('4','普通员工','','0','2020-06-05 11:28:06',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_menu` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `menu_id` varchar(128) NOT NULL COMMENT '菜单编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES ('1275331506385993730','1275272223367663618','8','2020-06-23 15:35:00'),('1275349476869009410','2','3','2020-06-23 16:46:25'),('1275349476961284098','2','2','2020-06-23 16:46:25'),('1275349477061947393','2','1','2020-06-23 16:46:25'),('1275935278724907009','4','7','2020-06-25 07:34:11'),('1275935278812987394','4','2','2020-06-25 07:34:11'),('1275935278976565250','4','1','2020-06-25 07:34:11'),('1275935279056257026','4','14','2020-06-25 07:34:11'),('1278208745293340674','1','3','2020-07-01 14:08:08'),('1278208745385615362','1','4','2020-07-01 14:08:08'),('1278208745494667266','1','6','2020-07-01 14:08:08'),('1278208745570164737','1','15','2020-07-01 14:08:08'),('1278208745649856514','1','16','2020-07-01 14:08:08'),('1278208745729548290','1','17','2020-07-01 14:08:08'),('1278208745830211585','1','18','2020-07-01 14:08:08'),('1278208745901514753','1','7','2020-07-01 14:08:08'),('1278208745981206529','1','2','2020-07-01 14:08:08'),('1278208746060898305','1','11','2020-07-01 14:08:08'),('1278208746140590082','1','12','2020-07-01 14:08:08'),('1278208746224476161','1','14','2020-07-01 14:08:08'),('1278208746299973633','1','13','2020-07-01 14:08:08'),('1278208746379665410','1','1','2020-07-01 14:08:08'),('1278208746459357186','1','19','2020-07-01 14:08:08');
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `username` varchar(128) NOT NULL COMMENT '登录名',
  `nickname` varchar(128) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(128) DEFAULT NULL COMMENT '实名',
  `identity` varchar(128) DEFAULT NULL COMMENT '身份',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮件',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机电话',
  `gender` varchar(16) DEFAULT NULL COMMENT '性别',
  `signature` varchar(2048) DEFAULT NULL COMMENT '签名',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `status` varchar(32) DEFAULT '0' COMMENT '状态',
  `created` datetime NOT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '更新日期',
  `create_source` varchar(128) DEFAULT NULL COMMENT '用户来源',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(32) DEFAULT NULL COMMENT '最后登录IP',
  `activated` datetime DEFAULT NULL COMMENT '激活时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1270957117494263809','ruyang',NULL,NULL,NULL,'$2a$10$Lg9cGpDFMKRYgDobEl5Rtu1whonVDb2.o9lI11sP03kyZFsbWPIRW',NULL,NULL,'1',NULL,NULL,NULL,'0','2020-06-11 13:52:45',NULL,NULL,'2020-07-02 09:35:01','127.0.0.1',NULL),('1271323640947875841','test01',NULL,NULL,NULL,'$2a$10$Nmqihlm5gN47304lJRoh2euk4EMEVA0wEyG5bF.eVJOFtTcvyNqT2',NULL,NULL,NULL,NULL,NULL,NULL,'0','2020-06-12 14:09:11',NULL,NULL,'2020-06-12 16:52:34','172.28.223.3',NULL),('1271327694386176001','test02',NULL,NULL,NULL,'$2a$10$W8PxMFzVPFOZWNiYWvsnG.uyH2C4590c2.Rrn1fSZP5NPsu0HBuTq',NULL,NULL,'2',NULL,NULL,NULL,'2','2020-06-12 14:25:17',NULL,NULL,NULL,NULL,NULL),('1271377165513981954','test03','小王','王小二',NULL,'$2a$10$xgTi9VfoiNrLTFV7LCFD4.W.iRnIxm9f1cMiAfw2zDWGpejPidjYa','wangxiaoer@quasar.cn','13800000001','1','天大地大，我最大！',NULL,'','0','2020-06-12 17:41:52',NULL,'网页注册','2020-06-30 18:00:49','127.0.0.1',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `user_id` varchar(128) NOT NULL COMMENT '用户编码',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('1270620369442619393','1270620369220321281','3','2020-06-10 15:34:38'),('1270620369509728257','1270620369220321281','4','2020-06-10 15:34:38'),('1270620529618894850','1270620529488871426','2','2020-06-10 15:35:16'),('1270957117783670785','1270957117494263809','1','2020-06-11 13:52:45'),('1274963683113467905','1271323640947875841','4','2020-06-22 15:13:24'),('1274963683184771073','1271323640947875841','2','2020-06-22 15:13:24'),('1274963714285535234','1271327694386176001','2','2020-06-22 15:13:32'),('1275620202024923138','1271377165513981954','3','2020-06-24 10:42:11');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tangdao_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-02 18:18:05
