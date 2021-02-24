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
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
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
-- Table structure for table `td_host_whitelist`
--

DROP TABLE IF EXISTS `td_host_whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_host_whitelist` (
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
-- Dumping data for table `td_host_whitelist`
--

LOCK TABLES `td_host_whitelist` WRITE;
/*!40000 ALTER TABLE `td_host_whitelist` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_host_whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_api_failed_record`
--

DROP TABLE IF EXISTS `td_sms_api_failed_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_api_failed_record` (
  `id` varchar(64) NOT NULL,
  `app_id` varchar(64) DEFAULT NULL COMMENT 'app_id',
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
-- Dumping data for table `td_sms_api_failed_record`
--

LOCK TABLES `td_sms_api_failed_record` WRITE;
/*!40000 ALTER TABLE `td_sms_api_failed_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_api_failed_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_forbidden_words`
--

DROP TABLE IF EXISTS `td_sms_forbidden_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_forbidden_words` (
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
-- Dumping data for table `td_sms_forbidden_words`
--

LOCK TABLES `td_sms_forbidden_words` WRITE;
/*!40000 ALTER TABLE `td_sms_forbidden_words` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_forbidden_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_mobile_blacklist`
--

DROP TABLE IF EXISTS `td_sms_mobile_blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_mobile_blacklist` (
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
-- Dumping data for table `td_sms_mobile_blacklist`
--

LOCK TABLES `td_sms_mobile_blacklist` WRITE;
/*!40000 ALTER TABLE `td_sms_mobile_blacklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_mobile_blacklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_mobile_whitelist`
--

DROP TABLE IF EXISTS `td_sms_mobile_whitelist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_mobile_whitelist` (
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
-- Dumping data for table `td_sms_mobile_whitelist`
--

LOCK TABLES `td_sms_mobile_whitelist` WRITE;
/*!40000 ALTER TABLE `td_sms_mobile_whitelist` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_mobile_whitelist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_mt_task`
--

DROP TABLE IF EXISTS `td_sms_mt_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_mt_task` (
  `id` varchar(64) NOT NULL,
  `app_id` varchar(64) NOT NULL COMMENT 'app_id',
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
-- Dumping data for table `td_sms_mt_task`
--

LOCK TABLES `td_sms_mt_task` WRITE;
/*!40000 ALTER TABLE `td_sms_mt_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_mt_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_sms_mt_task_packets`
--

DROP TABLE IF EXISTS `td_sms_mt_task_packets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_sms_mt_task_packets` (
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
-- Dumping data for table `td_sms_mt_task_packets`
--

LOCK TABLES `td_sms_mt_task_packets` WRITE;
/*!40000 ALTER TABLE `td_sms_mt_task_packets` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_sms_mt_task_packets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_user`
--

DROP TABLE IF EXISTS `td_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_user` (
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
-- Dumping data for table `td_user`
--

LOCK TABLES `td_user` WRITE;
/*!40000 ALTER TABLE `td_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_user_balance`
--

DROP TABLE IF EXISTS `td_user_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_user_balance` (
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
-- Dumping data for table `td_user_balance`
--

LOCK TABLES `td_user_balance` WRITE;
/*!40000 ALTER TABLE `td_user_balance` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_user_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_user_developer`
--

DROP TABLE IF EXISTS `td_user_developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_user_developer` (
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
-- Dumping data for table `td_user_developer`
--

LOCK TABLES `td_user_developer` WRITE;
/*!40000 ALTER TABLE `td_user_developer` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_user_developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `td_user_sms_config`
--

DROP TABLE IF EXISTS `td_user_sms_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `td_user_sms_config` (
  `id` varchar(64) NOT NULL,
  `app_id` varchar(64) NOT NULL COMMENT 'appid',
  `sms_words` int(4) NOT NULL DEFAULT '70' COMMENT '每条计费字数',
  `sms_return_rule` int(4) DEFAULT '0' COMMENT '短信返还规则，0:不返还，1：失败自动返还，2：超时未回执返还；',
  `sms_timeout` bigint(20) DEFAULT NULL COMMENT '短信超时时间（毫秒）',
  `message_pass` tinyint(1) DEFAULT '1' COMMENT '短信内容免审核，1：需要审核，0：不需要',
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
  UNIQUE KEY `idx_user_sms_config_app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户短信配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `td_user_sms_config`
--

LOCK TABLES `td_user_sms_config` WRITE;
/*!40000 ALTER TABLE `td_user_sms_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `td_user_sms_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tangdao'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-24 17:31:54
