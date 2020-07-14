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
INSERT INTO `user` VALUES ('1270957117494263809','ruyang',NULL,NULL,NULL,'$2a$10$Lg9cGpDFMKRYgDobEl5Rtu1whonVDb2.o9lI11sP03kyZFsbWPIRW',NULL,NULL,'1',NULL,NULL,NULL,'0','2020-06-11 13:52:45',NULL,NULL,'2020-07-14 17:17:08','183.157.195.127',NULL),('1271323640947875841','test01',NULL,NULL,NULL,'$2a$10$Nmqihlm5gN47304lJRoh2euk4EMEVA0wEyG5bF.eVJOFtTcvyNqT2',NULL,NULL,NULL,NULL,NULL,NULL,'0','2020-06-12 14:09:11',NULL,NULL,'2020-06-12 16:52:34','172.28.223.3',NULL),('1271327694386176001','test02',NULL,NULL,NULL,'$2a$10$W8PxMFzVPFOZWNiYWvsnG.uyH2C4590c2.Rrn1fSZP5NPsu0HBuTq',NULL,NULL,'2',NULL,NULL,NULL,'2','2020-06-12 14:25:17',NULL,NULL,NULL,NULL,NULL),('1271377165513981954','test03','小王','王小二',NULL,'$2a$10$xgTi9VfoiNrLTFV7LCFD4.W.iRnIxm9f1cMiAfw2zDWGpejPidjYa','wangxiaoer@quasar.cn','13800000001','1','天大地大，我最大！',NULL,'','0','2020-06-12 17:41:52',NULL,'网页注册','2020-07-13 16:45:24','222.72.47.2',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
  `menu_name` varchar(128) NOT NULL COMMENT '权限名称',
  `menu_path` varchar(256) DEFAULT NULL COMMENT '地址',
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
INSERT INTO `role_menu` VALUES ('1275331506385993730','1275272223367663618','8','2020-06-23 15:35:00'),('1275349476869009410','2','3','2020-06-23 16:46:25'),('1275349476961284098','2','2','2020-06-23 16:46:25'),('1275349477061947393','2','1','2020-06-23 16:46:25'),('1281505974355542017','4','2','2020-07-10 16:30:08'),('1281505974435233794','4','1','2020-07-10 16:30:08'),('1282481667654676481','1','2','2020-07-13 09:07:12'),('1282481667751145473','1','19','2020-07-13 09:07:12'),('1282481667826642945','1','1','2020-07-13 09:07:12'),('1282481667914723329','1','12','2020-07-13 09:07:12');
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
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
INSERT INTO `user_role` VALUES ('1270620369442619393','1270620369220321281','3','2020-06-10 15:34:38'),('1270620369509728257','1270620369220321281','4','2020-06-10 15:34:38'),('1270620529618894850','1270620529488871426','2','2020-06-10 15:35:16'),('1270957117783670785','1270957117494263809','1','2020-06-11 13:52:45'),('1274963714285535234','1271327694386176001','2','2020-06-22 15:13:32'),('1281505743043870721','1271377165513981954','4','2020-07-10 16:29:13'),('1281610903389224961','1271323640947875841','1','2020-07-10 23:27:05');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policy`
--

DROP TABLE IF EXISTS `policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `policy_name` varchar(128) NOT NULL COMMENT '策略名称',
  `policy_name_cn` varchar(256) DEFAULT NULL COMMENT '策略名称',
  `policy_type` char(1) NOT NULL DEFAULT '0' COMMENT '类型，1-系统策略，2-自定义策略',
  `content` varchar(5000) DEFAULT NULL COMMENT '策略内容',
  `version` varchar(128) DEFAULT NULL COMMENT '策略内容版本号',
  `status` varchar(32) NOT NULL DEFAULT '0' COMMENT '状态',
  `remark` varchar(256) DEFAULT NULL,
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `policy_UN` (`policy_name`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policy`
--

LOCK TABLES `policy` WRITE;
/*!40000 ALTER TABLE `policy` DISABLE KEYS */;
INSERT INTO `policy` VALUES ('1','CustomerPolicyAccess','自定义策略','2','[{\"permissions\":[\"admin:users*:POST\",\"admin:roles*:POST\",\"admin:policies*:POST\"],\"effect\":\"-1\"}]','1','0',NULL,'2020-07-10 21:33:36',NULL),('1281603543606247425','LogReadOnlyAccess','日志服务访问策略','2','[{\"permissions\":[\"admin:*:log:GET\"],\"effect\":\"1\"}]',NULL,'0',NULL,'2020-07-10 22:57:50',NULL),('2','OpenApiReadOnlyAccess','开放接口的访问权限','1','[{\"permissions\":[\"admin:login:GET\",\"admin:check_token:GET\",\"admin:authority:GET\"],\"effect\":\"1\"}]','1','0',NULL,'2020-07-10 21:33:39',NULL),('3','AdministratorAccess','管理所有接口的操作权限','1','[{\"permissions\":[\"admin:*\"],\"effect\":\"1\"}]','1','0',NULL,'2020-07-10 21:33:42',NULL);
/*!40000 ALTER TABLE `policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_policy`
--

DROP TABLE IF EXISTS `user_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `user_id` varchar(128) NOT NULL COMMENT '用户编码',
  `policy_id` varchar(128) NOT NULL COMMENT '策略编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与策略关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_policy`
--

LOCK TABLES `user_policy` WRITE;
/*!40000 ALTER TABLE `user_policy` DISABLE KEYS */;
INSERT INTO `user_policy` VALUES ('1281612574966169601','1271327694386176001','1281603543606247425','2020-07-10 23:33:44');
/*!40000 ALTER TABLE `user_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_policy`
--

DROP TABLE IF EXISTS `role_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `policy_id` varchar(128) NOT NULL COMMENT '策略编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与策略关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_policy`
--

LOCK TABLES `role_policy` WRITE;
/*!40000 ALTER TABLE `role_policy` DISABLE KEYS */;
INSERT INTO `role_policy` VALUES ('1','1','3',NULL),('1281611013523259394','4','2','2020-07-10 23:27:31'),('1281611013598756866','4','1281603543606247425','2020-07-10 23:27:31');
/*!40000 ALTER TABLE `role_policy` ENABLE KEYS */;
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
  `role_name_cn` varchar(100) DEFAULT NULL,
  `role_type` char(1) DEFAULT NULL COMMENT '角色类型',
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
INSERT INTO `role` VALUES ('1','系统管理员','系统管理员',NULL,'客户方使用的系统管理员，用于一些常用的基础数据配置。','0','2020-06-05 11:28:00',NULL),('2','财务','财务',NULL,'','0','2020-06-05 11:28:02',NULL),('3','商铺管理员','商铺管理员',NULL,'','0','2020-06-05 11:28:04',NULL),('4','普通员工','普通员工',NULL,'','0','2020-06-05 11:28:06',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-14 17:20:28
