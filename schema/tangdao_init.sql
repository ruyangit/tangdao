-- MySQL dump 10.13  Distrib 5.7.28, for Win64 (x86_64)
--
-- Host: huawei-server    Database: tangdao
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
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_code` varchar(64) NOT NULL COMMENT '菜单编码',
  `parent_code` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10,0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_names` varchar(1000) NOT NULL COMMENT '全节点名',
  `menu_name` varchar(100) NOT NULL COMMENT '菜单名称',
  `menu_type` char(1) NOT NULL COMMENT '菜单类型（1菜单 2权限 3开发）',
  `menu_href` varchar(1000) DEFAULT NULL COMMENT '链接',
  `menu_target` varchar(20) DEFAULT NULL COMMENT '目标',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `menu_color` varchar(50) DEFAULT NULL COMMENT '颜色',
  `permission` varchar(1000) DEFAULT NULL COMMENT '权限标识',
  `weight` decimal(4,0) DEFAULT NULL COMMENT '菜单权重',
  `is_show` char(1) NOT NULL COMMENT '是否显示（1显示 0隐藏）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`menu_code`),
  KEY `idx_sys_menu_pc` (`parent_code`),
  KEY `idx_sys_menu_ts` (`tree_sort`),
  KEY `idx_sys_menu_status` (`status`),
  KEY `idx_sys_menu_mt` (`menu_type`),
  KEY `idx_sys_menu_is` (`is_show`),
  KEY `idx_sys_menu_wt` (`weight`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES ('1359027058820374529','0','0,',30,'概览','概览','1','/system/overview',NULL,'broken_image',NULL,NULL,20,'1','0','system','2021-02-09 14:31:15','system','2021-02-09 14:33:13',NULL),('1359028156750749698','0','0,',40,'权限','权限','1',NULL,NULL,'security',NULL,NULL,20,'1','0','system','2021-02-09 14:35:37','system','2021-02-09 14:35:37',NULL),('1359028301957554177','0','0,',50,'系统设置','系统设置','1',NULL,NULL,'settings',NULL,NULL,20,'1','0','system','2021-02-09 14:36:11','system','2021-02-09 14:36:11',NULL),('1359028456572182530','0','0,',60,'日志','日志','1','/system/log',NULL,'assignment',NULL,NULL,20,'1','0','system','2021-02-09 14:36:48','system','2021-02-09 14:36:48',NULL),('1359028819748577282','1359028156750749698','0,1359028156750749698,',10,'权限/用户','用户','1','/system/user',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:38:15','system','2021-02-09 14:38:15',NULL),('1359029032605310978','1359028156750749698','0,1359028156750749698,',20,'权限/角色','角色','1','/system/role',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:39:06','system','2021-02-09 14:39:06',NULL),('1359029110380290050','1359028156750749698','0,1359028156750749698,',30,'权限/管理员','管理员','1','/system/admin',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:39:24','system','2021-02-09 14:39:24',NULL),('1359029586286993410','1359028301957554177','0,1359028301957554177,',10,'系统设置/菜单管理','菜单管理','1','/system/menu',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:41:18','system','2021-02-09 14:41:18',NULL),('1359029631207989250','1359028301957554177','0,1359028301957554177,',20,'系统设置/系统配置','系统配置','1','/system/menu',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:41:28','system','2021-02-09 14:41:28',NULL),('1359029679270518785','1359028301957554177','0,1359028301957554177,',30,'系统设置/字典管理','字典管理','1','/system/menu',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:41:40','system','2021-02-09 14:41:40',NULL),('1359029720815099906','1359028301957554177','0,1359028301957554177,',40,'系统设置/行政区域','行政区域','1','/system/menu',NULL,NULL,NULL,NULL,20,'1','0','system','2021-02-09 14:41:50','system','2021-02-09 14:41:50',NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_host_whitelist`
--

DROP TABLE IF EXISTS `paas_host_whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_host_whitelist` (
  `id` varchar(64) NOT NULL DEFAULT '',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态 默认0',
  `ip` varchar(16) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ip白名单信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_host_whitelist`
--

LOCK TABLES `paas_host_whitelist` WRITE;
/*!40000 ALTER TABLE `paas_host_whitelist` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_host_whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_user`
--

DROP TABLE IF EXISTS `paas_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_user` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `user_name` varchar(100) NOT NULL COMMENT '客户姓名',
  `user_name_en` varchar(100) DEFAULT NULL COMMENT '英文名',
  `user_type` char(1) DEFAULT NULL COMMENT '用户类型，1个人，2企业',
  `phone` varchar(100) DEFAULT NULL COMMENT '办公电话',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `company_code` varchar(100) DEFAULT NULL COMMENT '公司代码',
  `wechat` varchar(100) DEFAULT NULL COMMENT '微信',
  `dingding` varchar(64) DEFAULT NULL COMMENT '叮叮',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0在职 1删除 2离职）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_user`
--

LOCK TABLES `paas_user` WRITE;
/*!40000 ALTER TABLE `paas_user` DISABLE KEYS */;
INSERT INTO `paas_user` VALUES ('1105305559767789568','小明','xiaoming','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0','system','2021-02-24 17:35:06','system','2021-02-24 17:35:14',NULL);
/*!40000 ALTER TABLE `paas_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_provider`
--

DROP TABLE IF EXISTS `paas_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_provider` (
  `id` varchar(64) NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '提供商类型0:运营商1:第三方',
  `name` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '提供商名称',
  `code` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '提供商编号',
  `is_applied` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用(0:启用1:停用)',
  `priority` tinyint(4) NOT NULL DEFAULT '0' COMMENT '优先级(相同产品调用优先级)',
  `contact` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `phone` varchar(45) CHARACTER SET utf8 DEFAULT '',
  `mobile` varchar(45) CHARACTER SET utf8 DEFAULT '',
  `fax` varchar(45) CHARACTER SET utf8 DEFAULT '',
  `address` varchar(45) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `zip` varchar(45) CHARACTER SET utf8 DEFAULT '',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `fk_provider_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信接口提供商';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_provider`
--

LOCK TABLES `paas_provider` WRITE;
/*!40000 ALTER TABLE `paas_provider` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_message_submit`
--

DROP TABLE IF EXISTS `sms_mt_message_submit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_message_submit` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `sid` bigint(20) NOT NULL COMMENT '消息ID',
  `mobile` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '手机号码',
  `area_code` varchar(64) DEFAULT NULL COMMENT '省份代码',
  `cmcp` int(4) DEFAULT NULL COMMENT '运营商',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '短信内容',
  `fee` int(4) NOT NULL DEFAULT '1' COMMENT '计费条数',
  `attach` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '自定义内容',
  `passage_id` int(11) unsigned NOT NULL COMMENT '通道ID',
  `need_push` tinyint(1) DEFAULT '0' COMMENT '是否需要推送，0：不需要，1：需要',
  `push_url` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '推送地址',
  `destnation_no` varchar(32) DEFAULT NULL COMMENT '扩展号码',
  `msg_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '调用接口回执ID，默认与SID一致',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态,0:处理成功，1：处理失败',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_submit_msgid` (`msg_id`,`mobile`),
  KEY `idx_passage_id` (`passage_id`),
  KEY `idx_message_submit_sid` (`sid`),
  KEY `idx_message_submit_only_mobile` (`mobile`),
  KEY `idx_message_submit_only_appid` (`user_id`),
  KEY `idx_message_submit_province_cmcp` (`area_code`,`cmcp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行短信提交';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_message_submit`
--

LOCK TABLES `sms_mt_message_submit` WRITE;
/*!40000 ALTER TABLE `sms_mt_message_submit` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_message_submit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_passage_parameter`
--

DROP TABLE IF EXISTS `sms_passage_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_passage_parameter` (
  `id` varchar(64) NOT NULL,
  `passage_id` int(11) NOT NULL COMMENT '通道ID',
  `protocol` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '协议类型',
  `call_type` tinyint(4) NOT NULL COMMENT '1-发送 2-状态回执推送 3-状态回执自取 4-上行推送 5-上行自取 6-余额查询',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `params_definition` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '定义，直接取模板里的值',
  `params` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '具体的参数值，取模板中的key作为KEY，如{"username":"test", "password":"123456"}',
  `result_format` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `success_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `position` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '返回值的具体位置，json存储',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `inx_passage_parameter_pid` (`passage_id`,`url`),
  KEY `inx_passage_parameter_url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_passage_parameter`
--

LOCK TABLES `sms_passage_parameter` WRITE;
/*!40000 ALTER TABLE `sms_passage_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_passage_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_by_name` varchar(100) DEFAULT NULL COMMENT '用户名称',
  `create_date` datetime NOT NULL COMMENT '创建时间',
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
  `log_type` varchar(100) DEFAULT NULL COMMENT '日志类型',
  `tenant_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '租户代码',
  `tenant_name` varchar(100) NOT NULL DEFAULT 'tangdao' COMMENT '租户名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_task_packets`
--

DROP TABLE IF EXISTS `sms_mt_task_packets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_task_packets` (
  `id` varchar(64) NOT NULL,
  `sid` bigint(20) NOT NULL COMMENT '消息ID',
  `mobile` longtext NOT NULL COMMENT '手机号码（批量时为多个）',
  `cmcp` int(4) DEFAULT NULL COMMENT '运营商',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile_size` int(10) DEFAULT NULL COMMENT '号码总个数',
  `message_template_id` bigint(20) DEFAULT NULL COMMENT '短信模板ID',
  `passage_id` int(11) DEFAULT NULL COMMENT '通道ID',
  `final_passage_id` int(11) DEFAULT NULL COMMENT '最终使用的通道ID',
  `passage_protocol` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '协议类型',
  `passage_url` varchar(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '通道URL',
  `passage_parameter` varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '参数信息',
  `result_format` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '结果模板',
  `success_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '成功码',
  `position` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '定位',
  `priority` int(10) DEFAULT '1' COMMENT '优先级',
  `force_actions` varchar(32) CHARACTER SET utf8 DEFAULT '000' COMMENT '异常分包情况下允许的操作，如000,010，第一位:未报备模板，第二位：敏感词，第三位：通道不可用',
  `retry_times` int(11) DEFAULT '0' COMMENT '调用上家重试次数',
  `area_code` int(4) DEFAULT NULL COMMENT '省份代码',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_passage_id` (`passage_id`),
  KEY `idx_task_sid` (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行短信任务分包';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_task_packets`
--

LOCK TABLES `sms_mt_task_packets` WRITE;
/*!40000 ALTER TABLE `sms_mt_task_packets` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_task_packets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_api_failed_record`
--

DROP TABLE IF EXISTS `sms_api_failed_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_api_failed_record` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL COMMENT 'app_id',
  `app_type` tinyint(2) unsigned DEFAULT '2' COMMENT '调用类型 1:融合WEB平台,2:开发者平台,3:运营支撑系统',
  `submit_type` tinyint(2) DEFAULT '1' COMMENT '请求类型 1 短信发送 2 余额查询 3 通道测试',
  `app_key` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '接口账号',
  `app_secret` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '接口密码',
  `mobile` varchar(6144) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
  `timestamps` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '提交时间戳',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ext_number` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '拓展号码',
  `attach` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '自定义内容',
  `callback` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '回调URL（选填）',
  `submit_url` varchar(256) CHARACTER SET utf8 NOT NULL COMMENT '程序调用URL',
  `ip` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '提交IP',
  `resp_code` varchar(2048) CHARACTER SET utf8 NOT NULL COMMENT '错误码',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_dst_mobile` (`mobile`(255)),
  KEY `idx_create_date` (`create_date`),
  KEY `idx_ip` (`ip`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mt下行失败短信';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_api_failed_record`
--

LOCK TABLES `sms_api_failed_record` WRITE;
/*!40000 ALTER TABLE `sms_api_failed_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_api_failed_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_signature_extno`
--

DROP TABLE IF EXISTS `sms_signature_extno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_signature_extno` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `signature` varchar(64) NOT NULL COMMENT '签名',
  `ext_number` varchar(16) NOT NULL COMMENT '扩展号码',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_sms_signature_extno_appid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签名扩展';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_signature_extno`
--

LOCK TABLES `sms_signature_extno` WRITE;
/*!40000 ALTER TABLE `sms_signature_extno` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_signature_extno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_passage_template_detail`
--

DROP TABLE IF EXISTS `paas_passage_template_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_passage_template_detail` (
  `id` varchar(64) NOT NULL,
  `template_id` int(11) NOT NULL,
  `call_type` tinyint(2) NOT NULL COMMENT '1-发送 2-状态回执推送 3-状态回执自取 4-上行推送 5-上行自取',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'url',
  `params` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '参数',
  `position` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '具体值的位置，json存储',
  `result_format` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '结果格式',
  `success_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '成功码标记',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道模板内容';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_passage_template_detail`
--

LOCK TABLES `paas_passage_template_detail` WRITE;
/*!40000 ALTER TABLE `paas_passage_template_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_passage_template_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_type` varchar(100) DEFAULT NULL COMMENT '角色分类（高管、中层、基层、其它）',
  `role_sort` decimal(10,0) DEFAULT NULL COMMENT '角色排序（升序）',
  `is_inner` char(1) DEFAULT NULL COMMENT '是否内置角色（1是 0否）',
  `user_type` varchar(16) DEFAULT NULL COMMENT '用户类型（employee员工 member会员）',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围设置（0未设置  1全部数据 2自定义数据）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `tenant_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '租户代码',
  `tenant_name` varchar(100) NOT NULL DEFAULT 'tangdao' COMMENT '租户名称',
  PRIMARY KEY (`role_code`),
  KEY `idx_sys_role_cc` (`tenant_code`),
  KEY `idx_sys_role_is` (`is_inner`),
  KEY `idx_sys_role_status` (`status`),
  KEY `idx_sys_role_rs` (`role_sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES ('1359070807495561218','系统管理员',NULL,NULL,NULL,NULL,NULL,'0','system','2021-02-09 17:25:05','system','2021-02-09 17:25:05','客户方使用的系统管理员，用于一些常用的基础数据配置。','0','tangdao'),('1359316159360417793','测试',NULL,NULL,NULL,NULL,NULL,'0','system','2021-02-10 09:40:02','system','2021-02-10 09:40:02','系统测试','0','tangdao');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_message_template`
--

DROP TABLE IF EXISTS `sms_message_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_message_template` (
  `id` varchar(64) NOT NULL COMMENT '作为用户在系统中的唯一标识',
  `user_id` varchar(64) NOT NULL COMMENT '归属用户，如果为0，则表示为系统模板（使用所有人）',
  `content` varchar(1024) CHARACTER SET utf8 NOT NULL COMMENT '模板内容',
  `app_type` int(4) DEFAULT '1' COMMENT '操作方式，1:融合WEB平台,2:开发者平台,3:运营支撑系统',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_user` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '审批账号',
  `notice_mode` int(4) DEFAULT '0' COMMENT '审核后短信通知类型,0:不需要通知，1：需要通知',
  `mobile` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核后通知的手机号码',
  `regex_value` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '匹配正则表达式，后台自动生成',
  `submit_interval` int(8) NOT NULL DEFAULT '30' COMMENT '短信提交时间间隔（同一号码）',
  `limit_times` int(8) DEFAULT '10' COMMENT '短信每天提交次数上限（同一号码）',
  `white_word` varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '敏感词白名单 |符号隔开',
  `route_type` int(4) NOT NULL DEFAULT '0' COMMENT '模板路由类型',
  `priority` int(4) NOT NULL DEFAULT '5' COMMENT '优先级（越大越优先）',
  `ext_number` varchar(32) DEFAULT NULL COMMENT '扩展号',
  `ignore_blacklist` int(4) DEFAULT '0' COMMENT '忽略手机号码黑名单',
  `ignore_forbidden_words` int(4) DEFAULT '0' COMMENT '忽略短信中敏感词信息，强制放行',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0：待审核，1：审核成功，2：审核失败',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `index_sms_message_template_app_id_status` (`user_id`,`status`,`priority`),
  KEY `idx_sms_template_status` (`status`),
  KEY `indx_sms_template_status_routetype` (`user_id`,`status`,`route_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_message_template`
--

LOCK TABLES `sms_message_template` WRITE;
/*!40000 ALTER TABLE `sms_message_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_message_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mobile_blacklist`
--

DROP TABLE IF EXISTS `sms_mobile_blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mobile_blacklist` (
  `id` varchar(64) NOT NULL,
  `mobile` varchar(16) CHARACTER SET utf8 NOT NULL COMMENT '手机号码',
  `type` int(4) NOT NULL DEFAULT '0' COMMENT '类型',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_sms_mobile_blacklist_m` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='号码黑名单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mobile_blacklist`
--

LOCK TABLES `sms_mobile_blacklist` WRITE;
/*!40000 ALTER TABLE `sms_mobile_blacklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mobile_blacklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_user_balance`
--

DROP TABLE IF EXISTS `paas_user_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_user_balance` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `type` int(4) NOT NULL COMMENT '类型 默认0：1短信，2:流量，3语音',
  `balance` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '金额或者条数',
  `pay_type` int(4) DEFAULT '1' COMMENT '1：预付费， 2：后付费',
  `threshold` int(10) DEFAULT NULL COMMENT '告警阀值（小于等于此值告警）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '告警状态,0:正常，1：告警',
  `mobile` varchar(64) DEFAULT NULL COMMENT '告警手机号码',
  `remarks` varchar(555) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_user_balance_usercode` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_user_balance`
--

LOCK TABLES `paas_user_balance` WRITE;
/*!40000 ALTER TABLE `paas_user_balance` DISABLE KEYS */;
INSERT INTO `paas_user_balance` VALUES ('1','1105305559767789568',1,1000.00,1,1000,'0',NULL,'developer call','2021-02-24 18:17:50','2021-02-25 09:22:32','system','1105305559767789568');
/*!40000 ALTER TABLE `paas_user_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_config` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `config_name` varchar(100) NOT NULL COMMENT '名称',
  `config_key` varchar(100) NOT NULL COMMENT '参数键',
  `config_value` varchar(1000) DEFAULT NULL COMMENT '参数值',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_sys_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_message_push`
--

DROP TABLE IF EXISTS `sms_mt_message_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_message_push` (
  `id` varchar(64) NOT NULL,
  `msg_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '消息ID',
  `mobile` varchar(6000) CHARACTER SET utf8 NOT NULL COMMENT '手机号码',
  `content` varchar(256) DEFAULT NULL COMMENT '推送内容',
  `retry_times` int(4) DEFAULT '1' COMMENT '重试次数',
  `response_milliseconds` bigint(20) DEFAULT NULL COMMENT '推送相应时间',
  `response_content` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '响应内容',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_message_deliver_push_msgid` (`msg_id`),
  KEY `idx_message_push_only_mobile` (`mobile`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行短信推送';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_message_push`
--

LOCK TABLES `sms_mt_message_push` WRITE;
/*!40000 ALTER TABLE `sms_mt_message_push` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_message_push` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_submit_hour_report`
--

DROP TABLE IF EXISTS `sms_submit_hour_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_submit_hour_report` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `passage_id` int(11) NOT NULL COMMENT '通道ID',
  `province_code` int(4) NOT NULL COMMENT '省份代码',
  `submit_count` int(11) NOT NULL COMMENT '提交数量',
  `bill_count` int(11) NOT NULL COMMENT '计费数',
  `unknown_count` int(11) DEFAULT NULL COMMENT '未知数量',
  `success_count` int(11) DEFAULT NULL COMMENT '成功数量',
  `submit_failed_count` int(11) DEFAULT NULL COMMENT '发送失败数量',
  `other_count` int(11) DEFAULT NULL COMMENT '其他数量',
  `born_hours` int(11) DEFAULT '72' COMMENT '落地小时阀值',
  `hour_time` bigint(20) DEFAULT NULL COMMENT '当前小时毫秒数',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `indx_report_user_provice` (`user_id`,`passage_id`,`province_code`,`hour_time`),
  KEY `indx_report_app_id_stat` (`user_id`,`passage_id`,`hour_time`),
  KEY `indx_report_passage` (`passage_id`,`hour_time`),
  KEY `indx_report_provice` (`province_code`,`hour_time`),
  KEY `indx_sms_report_hour` (`hour_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提交报告（小时）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_submit_hour_report`
--

LOCK TABLES `sms_submit_hour_report` WRITE;
/*!40000 ALTER TABLE `sms_submit_hour_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_submit_hour_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `menu_code` varchar(64) NOT NULL COMMENT '菜单编码',
  PRIMARY KEY (`role_code`,`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES ('1359070807495561218','1359027058820374529'),('1359070807495561218','1359028456572182530'),('1359070807495561218','1359028819748577282'),('1359070807495561218','1359029032605310978'),('1359070807495561218','1359029110380290050'),('1359070807495561218','1359029586286993410'),('1359070807495561218','1359029631207989250'),('1359070807495561218','1359029679270518785'),('1359070807495561218','1359029720815099906'),('1359316159360417793','1359028456572182530');
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_forbidden_words`
--

DROP TABLE IF EXISTS `sms_forbidden_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_forbidden_words` (
  `id` varchar(64) NOT NULL DEFAULT '',
  `word` varchar(64) NOT NULL COMMENT '敏感词',
  `level` int(2) DEFAULT NULL COMMENT '告警级别，区分颜色',
  `label` varchar(32) DEFAULT NULL COMMENT '标签',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_sms_forbidden_words` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关键字';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_forbidden_words`
--

LOCK TABLES `sms_forbidden_words` WRITE;
/*!40000 ALTER TABLE `sms_forbidden_words` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_forbidden_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_message_deliver_log`
--

DROP TABLE IF EXISTS `sms_mt_message_deliver_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_message_deliver_log` (
  `id` varchar(64) NOT NULL,
  `passage_code` varchar(32) DEFAULT NULL COMMENT '通道简码',
  `msg_id` varchar(64) DEFAULT NULL COMMENT '消息ID',
  `deliver_time` varchar(32) DEFAULT NULL COMMENT '短信提供商回复的时间，可为空',
  `create_date` datetime NOT NULL COMMENT '插入数据时间',
  `report` longtext COMMENT '报文数据',
  PRIMARY KEY (`id`),
  KEY `idx_receive_time` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='下行短信回执状态日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_message_deliver_log`
--

LOCK TABLES `sms_mt_message_deliver_log` WRITE;
/*!40000 ALTER TABLE `sms_mt_message_deliver_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_message_deliver_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mo_message_push`
--

DROP TABLE IF EXISTS `sms_mo_message_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mo_message_push` (
  `id` varchar(64) NOT NULL,
  `msg_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '消息ID',
  `mobile` varchar(6000) CHARACTER SET utf8 NOT NULL COMMENT '手机号码',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '推送内容',
  `retry_times` int(4) DEFAULT '1' COMMENT '重试次数',
  `response_milliseconds` bigint(20) DEFAULT NULL COMMENT '推送相应时间',
  `response_content` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '响应内容',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0：待审核，1：审核成功，2：审核失败',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上行消息推送';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mo_message_push`
--

LOCK TABLES `sms_mo_message_push` WRITE;
/*!40000 ALTER TABLE `sms_mo_message_push` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mo_message_push` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_passage_template`
--

DROP TABLE IF EXISTS `paas_passage_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_passage_template` (
  `id` varchar(64) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '模板名称',
  `protocol` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '协议',
  `passage_type` tinyint(2) NOT NULL COMMENT '1-短信通道模板 2-流量通道模板 3-语音通道模板',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道模板';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_passage_template`
--

LOCK TABLES `paas_passage_template` WRITE;
/*!40000 ALTER TABLE `paas_passage_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_passage_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_area`
--

DROP TABLE IF EXISTS `sys_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_area` (
  `area_code` varchar(100) NOT NULL COMMENT '区域编码',
  `parent_code` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10,0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_names` varchar(1000) NOT NULL COMMENT '全节点名',
  `area_name` varchar(100) NOT NULL COMMENT '区域名称',
  `area_type` char(1) DEFAULT NULL COMMENT '区域类型',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`area_code`),
  KEY `idx_sys_area_pc` (`parent_code`),
  KEY `idx_sys_area_ts` (`tree_sort`),
  KEY `idx_sys_area_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行政区划';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_area`
--

LOCK TABLES `sys_area` WRITE;
/*!40000 ALTER TABLE `sys_area` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_user_developer`
--

DROP TABLE IF EXISTS `paas_user_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_user_developer` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `app_key` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT 'app_key',
  `app_secret` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT 'app_secret',
  `salt` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '随机盐',
  `status` varchar(1) DEFAULT '0' COMMENT '状态',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(500) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_developer_appkey` (`app_key`),
  KEY `idx_developer_userid_status` (`user_id`,`status`),
  KEY `idx_developer_appkey_status` (`app_key`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户开发者授权';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_user_developer`
--

LOCK TABLES `paas_user_developer` WRITE;
/*!40000 ALTER TABLE `paas_user_developer` DISABLE KEYS */;
INSERT INTO `paas_user_developer` VALUES ('1','1105305559767789568','a','b','c','0','2021-02-24 17:52:51','2021-02-24 17:52:53',NULL,'system','system');
/*!40000 ALTER TABLE `paas_user_developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_code` varchar(100) NOT NULL COMMENT '用户编码',
  `login_code` varchar(100) NOT NULL COMMENT '登录账号',
  `user_name` varchar(100) NOT NULL COMMENT '用户昵称',
  `password` varchar(100) NOT NULL COMMENT '登录密码',
  `email` varchar(300) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号码',
  `phone` varchar(100) DEFAULT NULL COMMENT '办公电话',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别',
  `avatar` varchar(1000) DEFAULT NULL COMMENT '头像路径',
  `sign` varchar(200) DEFAULT NULL COMMENT '个性签名',
  `user_type` varchar(16) NOT NULL COMMENT '用户类型',
  `ref_code` varchar(64) DEFAULT NULL COMMENT '用户类型引用编号',
  `ref_name` varchar(100) DEFAULT NULL COMMENT '用户类型引用姓名',
  `last_login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `freeze_date` datetime DEFAULT NULL COMMENT '冻结时间',
  `freeze_cause` varchar(200) DEFAULT NULL COMMENT '冻结原因',
  `user_weight` decimal(8,0) DEFAULT '0' COMMENT '用户权重（降序）',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用 3冻结）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `tenant_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '租户代码',
  `tenant_name` varchar(100) NOT NULL DEFAULT 'tangdao' COMMENT '租户名称',
  PRIMARY KEY (`user_code`),
  KEY `idx_sys_user_lc` (`login_code`),
  KEY `idx_sys_user_email` (`email`),
  KEY `idx_sys_user_mobile` (`mobile`),
  KEY `idx_sys_user_rt` (`user_type`),
  KEY `idx_sys_user_rc` (`ref_code`),
  KEY `idx_sys_user_us` (`user_weight`),
  KEY `idx_sys_user_ud` (`update_date`),
  KEY `idx_sys_user_status` (`status`),
  KEY `idx_sys_user_cc` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('user','user','user','user',NULL,NULL,NULL,NULL,NULL,NULL,'0','1105305559767789568','小明',NULL,NULL,NULL,NULL,0,'0','system','2021-02-24 17:34:21','system','2021-02-24 17:34:30',NULL,'0','tangdao');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_priority_words`
--

DROP TABLE IF EXISTS `paas_priority_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_priority_words` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `content` varchar(64) NOT NULL COMMENT '内容',
  `priority` int(4) NOT NULL DEFAULT '1' COMMENT '优先级',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `indx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优先级词库配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_priority_words`
--

LOCK TABLES `paas_priority_words` WRITE;
/*!40000 ALTER TABLE `paas_priority_words` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_priority_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_user_passage`
--

DROP TABLE IF EXISTS `paas_user_passage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_user_passage` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `type` int(4) NOT NULL COMMENT '类型 1-短信，2-流量，3-语音',
  `passage_group_id` int(11) NOT NULL COMMENT '业务通道组ID，如短信通道组ID，流量通道组ID',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_user_passage_1` (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通道配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_user_passage`
--

LOCK TABLES `paas_user_passage` WRITE;
/*!40000 ALTER TABLE `paas_user_passage` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_user_passage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `user_code` varchar(100) NOT NULL COMMENT '用户编码',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`user_code`,`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_message_deliver`
--

DROP TABLE IF EXISTS `sms_mt_message_deliver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_message_deliver` (
  `id` varchar(64) NOT NULL,
  `msg_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '消息ID',
  `cmcp` int(4) NOT NULL COMMENT '运营商',
  `mobile` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '用户手机号',
  `status_code` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '状态码',
  `deliver_time` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信提供商回复的时间，可为空',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_deliver_unique` (`msg_id`,`mobile`),
  KEY `idx_deliver_status` (`status`),
  KEY `idx_receive_time` (`create_date`),
  KEY `idx_message_deliver_only_msgid` (`msg_id`),
  KEY `idx_message_deliver_only_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行短信回执状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_message_deliver`
--

LOCK TABLES `sms_mt_message_deliver` WRITE;
/*!40000 ALTER TABLE `sms_mt_message_deliver` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_message_deliver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_push_config`
--

DROP TABLE IF EXISTS `paas_push_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_push_config` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `url` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '状态报告地址/上行地址',
  `type` int(4) DEFAULT NULL COMMENT '类型 1:短信状态报告,2:短信上行回执报告,3:流量充值报告,4:语音发送报告',
  `retry_times` int(4) DEFAULT '3' COMMENT '重推次数',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_push_config_1` (`user_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送配置信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_push_config`
--

LOCK TABLES `paas_push_config` WRITE;
/*!40000 ALTER TABLE `paas_push_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `paas_push_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_user_sms_config`
--

DROP TABLE IF EXISTS `paas_user_sms_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_user_sms_config` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT 'appid',
  `sms_words` int(4) NOT NULL DEFAULT '70' COMMENT '每条计费字数',
  `sms_return_rule` int(4) DEFAULT '0' COMMENT '短信返还规则，0:不返还，1：失败自动返还，2：超时未回执返还；',
  `sms_timeout` bigint(20) DEFAULT NULL COMMENT '短信超时时间（毫秒）',
  `message_pass` tinyint(1) DEFAULT '1' COMMENT '短信内容免审核，1：需要，0：不需要',
  `need_template` tinyint(1) DEFAULT '1' COMMENT '是否需要报备模板，1：需要，0：不需要',
  `auto_template` tinyint(1) DEFAULT '0' COMMENT '自动提取短信模板,0-不提取，1-提取',
  `signature_source` int(4) NOT NULL DEFAULT '0' COMMENT '签名途径，0：自维护，1：系统强制',
  `signature_content` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '签名内容',
  `ext_number` varchar(32) DEFAULT NULL COMMENT '扩展号码',
  `submit_interval` int(8) DEFAULT '30' COMMENT '短信提交时间间隔（同一号码）',
  `limit_times` int(8) DEFAULT '10' COMMENT '短信每天提交次数上限（同一号码）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_sms_config_app_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户短信配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_user_sms_config`
--

LOCK TABLES `paas_user_sms_config` WRITE;
/*!40000 ALTER TABLE `paas_user_sms_config` DISABLE KEYS */;
INSERT INTO `paas_user_sms_config` VALUES ('1','1',70,0,10000,1,1,0,1,'强制',NULL,1,20,'0','system','2021-02-24 17:54:25','system','2021-02-24 17:54:27',NULL);
/*!40000 ALTER TABLE `paas_user_sms_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mo_message_receive`
--

DROP TABLE IF EXISTS `sms_mo_message_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mo_message_receive` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `passage_id` int(11) DEFAULT '0' COMMENT '通道标识',
  `msg_id` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信标识',
  `mobile` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户手机号',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '短信内容',
  `destnation_no` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '服务号长号码',
  `need_push` tinyint(1) DEFAULT '0' COMMENT '是否需要推送，0：不需要，1：需要',
  `push_url` varchar(128) DEFAULT NULL COMMENT '推送地址',
  `receive_time` varchar(64) DEFAULT NULL COMMENT '收到信息的时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_mo_msgid` (`msg_id`,`mobile`),
  KEY `idx_mo_msgid_pass` (`passage_id`,`msg_id`,`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上行消息回复';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mo_message_receive`
--

LOCK TABLES `sms_mo_message_receive` WRITE;
/*!40000 ALTER TABLE `sms_mo_message_receive` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mo_message_receive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict_type` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `is_inner` char(1) NOT NULL COMMENT '是否内置字典（1是 0否）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_sys_dict_type_is` (`is_inner`),
  KEY `idx_sys_dict_type_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mt_task`
--

DROP TABLE IF EXISTS `sms_mt_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mt_task` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT 'app_id',
  `sid` bigint(20) NOT NULL COMMENT '消息ID',
  `app_type` tinyint(2) NOT NULL DEFAULT '2' COMMENT '调用类型 1:融合WEB平台,2:开发者平台,3:运营支撑系统',
  `mobile` longtext CHARACTER SET utf8 NOT NULL COMMENT '手机号',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ext_number` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '拓展号码',
  `attach` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '自定义内容',
  `callback` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '回调URL（选填）',
  `fee` int(8) NOT NULL COMMENT '计费条数',
  `return_fee` int(4) DEFAULT '0' COMMENT '返还条数',
  `submit_url` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '程序调用URL',
  `ip` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '提交IP',
  `submit_type` int(4) DEFAULT '0' COMMENT '提交类型：0：批量短信，1：普通点对点，2：模板点对点',
  `process_status` int(4) DEFAULT NULL COMMENT '分包状态,0:正在分包，1：分包完成，待发送，2:分包异常，待处理，3:分包失败，终止',
  `approve_status` int(4) DEFAULT NULL COMMENT '0：待审核，1：自动通过，2：手动通过，3：审核未通过',
  `error_mobiles` text CHARACTER SET utf8 COMMENT '错号手机号码',
  `repeat_mobiles` text COMMENT '重复手机号码',
  `black_mobiles` text COMMENT '黑名单手机号码',
  `final_content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `process_time` datetime DEFAULT NULL COMMENT '分包完成时间',
  `force_actions` varchar(32) CHARACTER SET utf8 DEFAULT '000' COMMENT '异常分包情况下允许的操作，如000,010，第一位:未报备模板，第二位：敏感词，第三位：通道不可用',
  `message_template_id` bigint(20) DEFAULT NULL COMMENT '短信模板ID',
  `forbidden_words` varchar(64) DEFAULT NULL COMMENT '敏感词',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_dst_sid_mobile` (`sid`,`mobile`(255)),
  KEY `idx_create_date` (`create_date`),
  KEY `idx_approve_status` (`approve_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下行短信任务';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mt_task`
--

LOCK TABLES `sms_mt_task` WRITE;
/*!40000 ALTER TABLE `sms_mt_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mt_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_message_deliver_log`
--

DROP TABLE IF EXISTS `sms_message_deliver_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_message_deliver_log` (
  `id` varchar(64) NOT NULL,
  `passage_code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '通道简码',
  `msg_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '消息ID',
  `status_code` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '状态码',
  `deliver_time` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信提供商回复的时间，可为空',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_receive_time` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mt下行短信回执';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_message_deliver_log`
--

LOCK TABLES `sms_message_deliver_log` WRITE;
/*!40000 ALTER TABLE `sms_message_deliver_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_message_deliver_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_mobile_whitelist`
--

DROP TABLE IF EXISTS `sms_mobile_whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_mobile_whitelist` (
  `id` varchar(64) NOT NULL,
  `mobile` varchar(16) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `indx_smsmobile_whlist_mobile` (`user_id`,`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='号码白名单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_mobile_whitelist`
--

LOCK TABLES `sms_mobile_whitelist` WRITE;
/*!40000 ALTER TABLE `sms_mobile_whitelist` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_mobile_whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_passage_control`
--

DROP TABLE IF EXISTS `sms_passage_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_passage_control` (
  `id` varchar(64) NOT NULL,
  `passage_id` int(11) NOT NULL COMMENT '通道ID',
  `cron` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '轮训表达式',
  `parameter_id` int(11) DEFAULT NULL COMMENT '通道参数ID',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道控制';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_passage_control`
--

LOCK TABLES `sms_passage_control` WRITE;
/*!40000 ALTER TABLE `sms_passage_control` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_passage_control` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` varchar(64) NOT NULL COMMENT '字典编码',
  `parent_code` varchar(64) NOT NULL COMMENT '父级编号',
  `parent_codes` varchar(1000) NOT NULL COMMENT '所有父级编号',
  `tree_sort` decimal(10,0) NOT NULL COMMENT '本级排序号（升序）',
  `tree_names` varchar(1000) NOT NULL COMMENT '全节点名',
  `dict_label` varchar(100) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) NOT NULL COMMENT '字典键值',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `is_inner` char(1) NOT NULL COMMENT '是否内置字典（1是 0否）',
  `description` varchar(500) DEFAULT NULL COMMENT '字典描述',
  `css_style` varchar(500) DEFAULT NULL COMMENT 'css样式（如：color:red)',
  `css_class` varchar(500) DEFAULT NULL COMMENT 'css类名（如：red）',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `tenant_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '租户代码',
  `tenant_name` varchar(100) NOT NULL DEFAULT 'tangdao' COMMENT '租户名称',
  PRIMARY KEY (`dict_code`),
  KEY `idx_sys_dict_data_cc` (`tenant_code`),
  KEY `idx_sys_dict_data_dt` (`dict_type`),
  KEY `idx_sys_dict_data_pc` (`parent_code`),
  KEY `idx_sys_dict_data_status` (`status`),
  KEY `idx_sys_dict_data_ts` (`tree_sort`),
  KEY `idx_sys_dict_data_dv` (`dict_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_passage_access`
--

DROP TABLE IF EXISTS `sms_passage_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_passage_access` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户编码',
  `group_id` int(11) NOT NULL COMMENT '通道组id',
  `route_type` int(4) NOT NULL COMMENT '路由类型',
  `cmcp` int(2) NOT NULL COMMENT '运营商',
  `area_code` int(11) DEFAULT '0' COMMENT '省份',
  `passage_id` int(11) NOT NULL COMMENT '通道ID',
  `passage_code` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '通道代码',
  `protocol` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '协议类型',
  `call_type` tinyint(4) NOT NULL COMMENT '1-发送2-下行推送 3-下行自取 4-上行推送 5-上行自取',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `params_definition` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '定义，直接取模板里的值',
  `params` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '具体的参数值，取模板中的key作为KEY，如{"username":"test", "password":"123456"}',
  `result_format` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `success_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '成功码',
  `position` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '返回值的具体位置，json存储',
  `mobile_size` int(10) DEFAULT NULL COMMENT '手机号码分包数',
  `packets_size` int(10) DEFAULT NULL COMMENT '1秒钟允许提交的网络包数量',
  `connection_size` int(10) DEFAULT NULL COMMENT '最大连接数',
  `read_timeout` int(10) DEFAULT NULL COMMENT '超时时间（毫秒）',
  `access_code` varchar(32) DEFAULT NULL COMMENT '接入号码（10690...）',
  `ext_number` int(10) DEFAULT '0' COMMENT '拓展号长度,0表示不允许拓展',
  `sign_mode` tinyint(4) DEFAULT '0' COMMENT '签名模式 0:不处理，1：自动前置，2：自动后置，3：自动去除签名',
  `sms_template_param` int(4) DEFAULT '0' COMMENT '是否需要短信模板参数信息',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_sms_passage_access_url` (`call_type`,`url`),
  KEY `idx_sms_passage_access_appid` (`user_id`,`route_type`,`area_code`,`cmcp`),
  KEY `idx_sms_passage_access_passageid` (`passage_id`),
  KEY `idx_sms_passage_access_appid_calltype` (`user_id`,`call_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道资产';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_passage_access`
--

LOCK TABLES `sms_passage_access` WRITE;
/*!40000 ALTER TABLE `sms_passage_access` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_passage_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_message_deliver`
--

DROP TABLE IF EXISTS `sms_message_deliver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_message_deliver` (
  `id` varchar(64) NOT NULL,
  `deliverStatus` int(11) DEFAULT NULL,
  `statusDes` varchar(500) DEFAULT NULL,
  `deliverTimeStart` bigint(20) DEFAULT NULL,
  `deliverTimeEnd` bigint(20) DEFAULT NULL,
  `httpTryTimes` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回执推送信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_message_deliver`
--

LOCK TABLES `sms_message_deliver` WRITE;
/*!40000 ALTER TABLE `sms_message_deliver` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_message_deliver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_passage`
--

DROP TABLE IF EXISTS `sms_passage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_passage` (
  `id` varchar(64) NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '通道名称',
  `code` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '通道字母编码',
  `cmcp` int(4) NOT NULL COMMENT '运营商',
  `word_number` tinyint(4) NOT NULL COMMENT '计费字数',
  `priority` tinyint(4) DEFAULT NULL COMMENT '优先级',
  `paas_template_id` int(11) NOT NULL COMMENT '通道模板',
  `type` tinyint(4) NOT NULL COMMENT '通道类型 0 公共通道 1 独立通道',
  `exclusive_user_id` int(11) DEFAULT NULL COMMENT '独立通道时绑定的用户',
  `sign_mode` tinyint(4) DEFAULT '0' COMMENT '签名模式 0:不处理，1：自动前置，2：自动后置，3：自动去除签名',
  `access_code` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '10690接入号，需与params表接入号一致',
  `account` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '接入账号(上家提供的用户账号)',
  `pay_type` tinyint(4) DEFAULT '1' COMMENT '付费方式(1预付2后付)',
  `balance` int(10) DEFAULT NULL COMMENT '剩余条数',
  `mobile_size` int(10) DEFAULT NULL COMMENT '手机号码分包数',
  `packets_size` int(10) DEFAULT NULL COMMENT '1秒钟允许提交的网络包数量',
  `connection_size` int(10) DEFAULT NULL COMMENT '最大连接数',
  `read_timeout` int(10) DEFAULT NULL COMMENT '超时时间（毫秒）',
  `ext_number` int(10) DEFAULT '0' COMMENT '拓展号长度,0表示不允许拓展',
  `born_term` int(10) DEFAULT '72' COMMENT '统计落地时限（小时）',
  `sms_template_param` int(4) DEFAULT '0' COMMENT '是否需要短信模板参数信息',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_passage_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通道管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_passage`
--

LOCK TABLES `sms_passage` WRITE;
/*!40000 ALTER TABLE `sms_passage` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_passage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-08 18:11:09
