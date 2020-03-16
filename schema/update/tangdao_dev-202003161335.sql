-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tangdao_dev
-- ------------------------------------------------------
-- Server version	5.7.28-log

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
-- Table structure for table `sys_assertion`
--

DROP TABLE IF EXISTS `sys_assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_assertion` (
  `assertion_id` varchar(64) NOT NULL COMMENT '编号',
  `policy_id` varchar(64) DEFAULT NULL,
  `role` varchar(2048) DEFAULT NULL,
  `resource` varchar(2048) DEFAULT NULL,
  `action` varchar(4096) DEFAULT NULL,
  `effect` varchar(64) NOT NULL DEFAULT 'allow' COMMENT '效力（allow允许 deny拒绝）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`assertion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='断言表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_assertion`
--

LOCK TABLES `sys_assertion` WRITE;
/*!40000 ALTER TABLE `sys_assertion` DISABLE KEYS */;
INSERT INTO `sys_assertion` VALUES ('1','1',NULL,'[*]','[core:user:List*,core:user:Get*]','allow','0',NULL,'2020-02-26 19:01:14',NULL,'2020-02-26 19:01:20',NULL),('2','1',NULL,'[*]','[core:user:Update*,core:user:Delete*]','allow','0',NULL,'2020-02-26 19:02:42',NULL,'2020-02-26 19:02:46',NULL),('3','2',NULL,'[*]','[core:user:UpdateTest]','deny','0',NULL,'2020-02-26 19:03:06',NULL,'2020-02-26 19:03:11',NULL);
/*!40000 ALTER TABLE `sys_assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_group` (
  `group_id` varchar(64) NOT NULL COMMENT '用户组主键',
  `group_name` varchar(100) DEFAULT NULL COMMENT '用户组名称',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `corp_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '主体编码',
  `corp_name` varchar(100) DEFAULT 'Tangdao' COMMENT '主体名称',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_group`
--

LOCK TABLES `sys_group` WRITE;
/*!40000 ALTER TABLE `sys_group` DISABLE KEYS */;
INSERT INTO `sys_group` VALUES ('1237559274092634113','test','0','1237401195489280002','2020-03-11 10:01:38','1237401195489280002','2020-03-11 10:01:38','123','0','Tangdao'),('1237559665198899201','12','0','1237401195489280002','2020-03-11 10:03:12','1237401195489280002','2020-03-11 10:03:12','12','0','Tangdao'),('1237559796015046658','23','0','1237401195489280002','2020-03-11 10:03:43','1237401195489280002','2020-03-11 10:03:43','23','0','Tangdao'),('1237560800102064130',NULL,'0','1237401195489280002','2020-03-11 10:07:42','1237401195489280002','2020-03-11 10:07:42',NULL,'0','Tangdao'),('1237560871006773249',NULL,'0','1237401195489280002','2020-03-11 10:07:59','1237401195489280002','2020-03-11 10:07:59',NULL,'0','Tangdao'),('1237561475762495489','qwe','0','1237401195489280002','2020-03-11 10:10:23','1237401195489280002','2020-03-11 10:10:23','qweqwe','0','Tangdao'),('1237561777551056897','123123','0','1237401195489280002','2020-03-11 10:11:35','1237401195489280002','2020-03-11 10:11:35','123','0','Tangdao'),('1237563023234179073','123','0','1237401195489280002','2020-03-11 10:16:32','1237401195489280002','2020-03-11 10:16:32','123123','0','Tangdao'),('1237564677161492482','123123','0','1237401195489280002','2020-03-11 10:23:07','1237401195489280002','2020-03-11 10:23:07','123123213','0','Tangdao'),('1237565153974165506','2412','0','1237401195489280002','2020-03-11 10:25:00','1237401195489280002','2020-03-11 10:25:00','123412','0','Tangdao'),('1237566007481475073','123','0','1237401195489280002','2020-03-11 10:28:24','1237401195489280002','2020-03-11 10:28:24','123123','0','Tangdao'),('1237566619849859074','123','0','1237401195489280002','2020-03-11 10:30:50','1237401195489280002','2020-03-11 10:30:50','123123','0','Tangdao'),('1237566847734784002','12','0','1237401195489280002','2020-03-11 10:31:44','1237401195489280002','2020-03-11 10:31:44','12','0','Tangdao'),('1237567353626566658','1231','0','1237401195489280002','2020-03-11 10:33:45','1237401195489280002','2020-03-11 10:33:45','123','0','Tangdao'),('1237568252361056258','123','0','1237401195489280002','2020-03-11 10:37:19','1237401195489280002','2020-03-11 10:37:19','21313','0','Tangdao'),('1237568489645416449','123','0','1237401195489280002','2020-03-11 10:38:16','1237401195489280002','2020-03-11 10:38:16','13123','0','Tangdao'),('1237568688270876673','123','0','1237401195489280002','2020-03-11 10:39:03','1237401195489280002','2020-03-11 10:39:03','13123','0','Tangdao'),('1237569149199720449','1231','0','1237401195489280002','2020-03-11 10:40:53','1237401195489280002','2020-03-11 10:40:53','12313','0','Tangdao'),('1237570439455723522','123123','0','1237401195489280002','2020-03-11 10:46:00','1237401195489280002','2020-03-11 10:46:00','12312313','0','Tangdao'),('1237570524734312449','123123','0','1237401195489280002','2020-03-11 10:46:21','1237401195489280002','2020-03-11 10:46:21','12312321','0','Tangdao'),('1237572002786070530','123123','0','1237401195489280002','2020-03-11 10:52:13','1237401195489280002','2020-03-11 10:52:13','123123','0','Tangdao'),('1237572678144512002','123123123','0','1237401195489280002','2020-03-11 10:54:54','1237401195489280002','2020-03-11 10:54:54','13123123123','0','Tangdao'),('1237574396601192449','1234','0','1237401195489280002','2020-03-11 11:01:44','1237401195489280002','2020-03-11 11:01:44','1242134','0','Tangdao'),('1237575772454539265','124214','0','1237401195489280002','2020-03-11 11:07:12','1237401195489280002','2020-03-11 11:07:12','1234124214','0','Tangdao'),('1237579127708995585','123123','0','1237401195489280002','2020-03-11 11:20:32','1237401195489280002','2020-03-11 11:20:32','123123','0','Tangdao'),('1238494197339230210','123','0','1044886608224198656','2020-03-13 23:56:41','1044886608224198656','2020-03-13 23:56:41','12312313123123','0','Tangdao');
/*!40000 ALTER TABLE `sys_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_group_policy`
--

DROP TABLE IF EXISTS `sys_group_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_group_policy` (
  `group_id` varchar(100) NOT NULL COMMENT '用户组编码',
  `policy_id` varchar(64) NOT NULL COMMENT '策略编码',
  PRIMARY KEY (`group_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组与策略关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_group_policy`
--

LOCK TABLES `sys_group_policy` WRITE;
/*!40000 ALTER TABLE `sys_group_policy` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_group_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编码',
  `parent_id` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(1000) NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10,0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_names` varchar(1000) NOT NULL COMMENT '全节点名',
  `menu_name` varchar(100) NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) NOT NULL COMMENT '菜单类型（1菜单 2权限 3开发）',
  `menu_href` varchar(1000) DEFAULT NULL COMMENT '链接',
  `menu_target` varchar(20) DEFAULT NULL COMMENT '目标',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_visible` char(1) NOT NULL COMMENT '是否显示（1显示 0隐藏）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `tree_leaf` char(1) DEFAULT NULL COMMENT '是否最末级',
  `tree_level` char(1) DEFAULT NULL COMMENT '层次级别',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_policy`
--

DROP TABLE IF EXISTS `sys_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_policy` (
  `policy_id` varchar(64) NOT NULL COMMENT '编号',
  `policy_name` varchar(64) DEFAULT NULL,
  `policy_type` char(1) NOT NULL DEFAULT '0' COMMENT '策略类型（0自定义策略，1系统默认策略）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `corp_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '主体编码',
  `corp_name` varchar(100) NOT NULL DEFAULT 'Tangdao' COMMENT '主体名称',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_policy`
--

LOCK TABLES `sys_policy` WRITE;
/*!40000 ALTER TABLE `sys_policy` DISABLE KEYS */;
INSERT INTO `sys_policy` VALUES ('1','允许策略测试','1','0',NULL,'2020-02-26 19:10:14',NULL,'2020-02-26 19:10:08','允许访问用户','0',''),('1238846292147912705',NULL,'0','0','1044886608224198656','2020-03-14 23:15:47','1044886608224198656','2020-03-14 23:15:47',NULL,'0','Tangdao'),('1238846735771058178','123123213','0','0','1044886608224198656','2020-03-14 23:17:33','1044886608224198656','2020-03-14 23:17:33',NULL,'0','Tangdao'),('1238846774140551170','123123123','0','0','1044886608224198656','2020-03-14 23:17:42','1044886608224198656','2020-03-14 23:17:42','123123213123','0','Tangdao'),('2','拒绝策略测试','2','0',NULL,'2020-02-26 19:11:00',NULL,'2020-02-26 19:10:55',NULL,'0','');
/*!40000 ALTER TABLE `sys_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` varchar(64) NOT NULL COMMENT '角色主键',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `corp_id` varchar(100) NOT NULL DEFAULT '1' COMMENT '主体编码',
  `corp_name` varchar(100) NOT NULL DEFAULT 'Tangdao' COMMENT '主体名称',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES ('1','admin','0',NULL,'2020-02-26 19:14:32',NULL,'2020-02-26 19:14:27',NULL,'admin','1'),('1238753737712439297','1234567890','0','1044886608224198656','2020-03-14 17:08:01','1044886608224198656','2020-03-14 17:08:01',NULL,'1','Tangdao'),('1238753773343051778','0987654321','0','1044886608224198656','2020-03-14 17:08:09','1044886608224198656','2020-03-14 17:08:09',NULL,'1','Tangdao'),('1238753832566624258','8765','0','1044886608224198656','2020-03-14 17:08:23','1044886608224198656','2020-03-14 17:08:23','你好五环','1','Tangdao'),('1238753969766502401','asjdflkasjfalskf','0','1044886608224198656','2020-03-14 17:08:56','1044886608224198656','2020-03-14 17:08:56','你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉你好中国你好武汉','1','Tangdao'),('1238759359791075329','kasflksajf','0','1044886608224198656','2020-03-14 17:30:21','1044886608224198656','2020-03-14 17:30:21','aslkflaskfjslakfjlsakdfjlsakfjjflkajslfkjsalkfjsalfjlsakjfdlsakjdflksajflksajdflkjasldfkjsalfjsalkdfjsalkfjlsakfjklsadf','1','Tangdao'),('1238760546951073793','正常正常正常','0','1044886608224198656','2020-03-14 17:35:04','1044886608224198656','2020-03-14 17:35:04','正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常正常','1','Tangdao'),('1238837994082353153','2','0','1044886608224198656','2020-03-14 22:42:49','1044886608224198656','2020-03-14 22:42:49','34','1','Tangdao');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `role_id` varchar(100) NOT NULL COMMENT '角色编码',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编码',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_policy`
--

DROP TABLE IF EXISTS `sys_role_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_policy` (
  `role_id` varchar(100) NOT NULL COMMENT '角色编码',
  `policy_id` varchar(64) NOT NULL COMMENT '策略编码',
  PRIMARY KEY (`role_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与策略关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_policy`
--

LOCK TABLES `sys_role_policy` WRITE;
/*!40000 ALTER TABLE `sys_role_policy` DISABLE KEYS */;
INSERT INTO `sys_role_policy` VALUES ('1','1');
/*!40000 ALTER TABLE `sys_role_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `login_name` varchar(100) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '登录密码',
  `nickname` varchar(100) NOT NULL COMMENT '用户昵称',
  `email` varchar(300) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号码',
  `phone` varchar(100) DEFAULT NULL COMMENT '办公电话',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别',
  `avatar` varchar(1000) DEFAULT NULL COMMENT '头像路径',
  `sign` varchar(200) DEFAULT NULL COMMENT '个性签名',
  `last_login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `freeze_date` datetime DEFAULT NULL COMMENT '冻结时间',
  `freeze_cause` varchar(200) DEFAULT NULL COMMENT '冻结原因',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用 3冻结）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `mgr_type` char(1) DEFAULT NULL COMMENT '管理员类型（0非管理员 1系统管理员）',
  `user_type` varchar(64) DEFAULT NULL COMMENT '用户类型',
  `corp_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '主体编码',
  `corp_name` varchar(100) DEFAULT 'Tangdao' COMMENT '主体名称',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('1044886607284674560','admin','$2a$10$SBQgz0AC1VjHlEPIorlWNuB/eorm3PO50zpPhCDs1qnwmvq4IlzDO','系统管理员','admin@aliyeye.com','15888888888','',NULL,'/images/default.jpg',NULL,'172.28.220.60','2019-05-29 17:56:23',NULL,NULL,'0','1044886608224198656','2018-09-26 17:49:23','1044886608224198656','2019-12-23 13:51:03','客户方使用的系统管理员，用于一些常用的基础数据配置。',NULL,NULL,'0','Tangdao'),('1044886608224198656','system','$2a$10$D7kzh.bqcmrKHWc/5.NoEeE4IKQJEudZgWeAy1kfFrP2J9xwCSGbO','超级管理员','admin@aliyeye.com','13800000000','','2',NULL,'','192.168.113.1','2020-01-20 09:11:50',NULL,NULL,'0','1044886608224198656','2018-09-26 17:49:23','1044886608224198656','2020-01-20 09:11:50','开发者使用的最高级别管理员，主要用于开发和调试。',NULL,NULL,'0','Tangdao'),('1237401195489280002','test','$2a$10$tvVWak/9ZwbE6apF0a.MbO6O.S9yicB9jLtqQSZ2u5XvkWCjxNJ.i','test',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1044886607284674560','2020-03-10 23:33:29','1044886607284674560','2020-03-10 23:33:29',NULL,NULL,NULL,'0','Tangdao'),('1237573687965143042','3241242','$2a$10$oEKMALAXso29foEfuaxLpeqT91H9wmdtqYt5k1h2DOSM7ELNX3wFC','124',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 10:58:55','1237401195489280002','2020-03-11 10:58:55',NULL,NULL,NULL,'0','Tangdao'),('1237574100340723713','234214','$2a$10$IbMM/OyQedkT2onk7b2NSescXQrmvfoeiBTYKY5hNwcnYoeRmSmc.','1242314',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 11:00:33','1237401195489280002','2020-03-11 11:00:33',NULL,NULL,NULL,'0','Tangdao'),('1237574188345610242','2134214','$2a$10$adZ1xLbnytBo41vqtRshmuEDhXAA1bgXD5BaaG4lZpoCjciAV3Voq','1242',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 11:00:54','1237401195489280002','2020-03-11 11:00:54',NULL,NULL,NULL,'0','Tangdao'),('1237574344411467778','1234214','$2a$10$2xjikTIoUVdNXT7wXNO7mekmxsJqNUI1iwGg7aMARouWefoQEUGaK','1234214',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 11:01:31','1237401195489280002','2020-03-11 11:01:31',NULL,NULL,NULL,'0','Tangdao'),('1237575694448873473','2143213421','$2a$10$NLfFeNghsUk/ZU4tYbaPGeIxasgTbXclv105dP3ITViL0pveT5Ppi','2143213421',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 11:06:53','1237401195489280002','2020-03-11 11:06:53',NULL,NULL,NULL,'0','Tangdao'),('1237579053553700865','ruyang','$2a$10$A5A/tjQ9Gw5sfe.1albClOad8TNUJeZ5Mnc8hPLTqBoLzI7fzeDaW','ruyang',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1237401195489280002','2020-03-11 11:20:14','1237401195489280002','2020-03-11 11:20:14',NULL,NULL,NULL,'0','Tangdao'),('1238494125025234946','123123','$2a$10$IvLqUJKZdu3NCjKE9dwkP.KiKVXLhlOYb4mmkvvDxMOHgRvgWlKVG','123123',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1044886608224198656','2020-03-13 23:56:24','1044886608224198656','2020-03-13 23:56:24',NULL,NULL,NULL,'0','Tangdao'),('1238654685456965633','tangdao','$2a$10$ZAw0FQgqHyCAjhqpG/MzU.OSf9F/o.xU/Ntq/bVH01Ot2eVWNv7bG','tangdao',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1044886608224198656','2020-03-14 10:34:25','1044886608224198656','2020-03-14 10:34:25',NULL,NULL,NULL,'0','Tangdao'),('1238836474527956994','2313','$2a$10$6pSA6sCkd2TKTiI7sO/NLeuAkMWTmz/6ARcHNiEtkfTCXb9tOJ.3y','2313',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','1044886608224198656','2020-03-14 22:36:47','1044886608224198656','2020-03-14 22:36:47',NULL,NULL,NULL,'0','Tangdao');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_group`
--

DROP TABLE IF EXISTS `sys_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_group` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `group_id` varchar(64) NOT NULL COMMENT '用户组编码',
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与用户组关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_group`
--

LOCK TABLES `sys_user_group` WRITE;
/*!40000 ALTER TABLE `sys_user_group` DISABLE KEYS */;
INSERT INTO `sys_user_group` VALUES ('1044886608224198656','1237565153974165506'),('1044886608224198656','1237568489645416449');
/*!40000 ALTER TABLE `sys_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_policy`
--

DROP TABLE IF EXISTS `sys_user_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_policy` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `policy_id` varchar(64) NOT NULL COMMENT '策略编码',
  PRIMARY KEY (`user_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与策略关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_policy`
--

LOCK TABLES `sys_user_policy` WRITE;
/*!40000 ALTER TABLE `sys_user_policy` DISABLE KEYS */;
INSERT INTO `sys_user_policy` VALUES ('100000001000010000','1'),('100000001000010000','2');
/*!40000 ALTER TABLE `sys_user_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `role_id` varchar(64) NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
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

-- Dump completed on 2020-03-16 13:35:10
