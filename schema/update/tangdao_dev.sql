/*
 Navicat Premium Data Transfer

 Source Server         : 华为公有云-48
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 121.37.180.48:3306
 Source Schema         : tangdao_dev

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 07/07/2020 22:18:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
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
  PRIMARY KEY (`id`) USING BTREE,
  KEY `log_created_IDX` (`created`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='审计日志表';

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
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

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` VALUES ('1', '0', '0,', '系统设置', '/system', NULL, 90, '1', '1', '0', NULL, NULL, NULL, 'broken_image', NULL, '0');
INSERT INTO `menu` VALUES ('10', '8', '0,1,8', '测试2', '/test/test2', NULL, 2, '1', '1', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('11', '0', '0,', '商城', '/shop', NULL, 30, '1', '1', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('12', '0', '0,', '内容', '/cms', NULL, 50, '1', '1', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('13', '0', '0,', '财务', '/account', NULL, 80, '1', '1', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('14', '1', '0,1,', '配置管理', '/system/config', NULL, 5, '1', '1', '0', NULL, NULL, NULL, 'developer_board', NULL, '0');
INSERT INTO `menu` VALUES ('15', '3', '0,1,2,3,', '用户新增', NULL, 'admin:users:POST', 2, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('16', '3', '0,1,2,3,', '用户修改密码', NULL, 'admin:users:password:modify:POST', 2, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('17', '3', '0,1,2,3,', '用户删除', NULL, 'admin:users:delete:POST', 2, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('18', '7', '0,1,2,7', '角色新增', NULL, 'admin:roles:POST', 3, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('19', '1', '0,1,', '监控信息', '/system/metrics', NULL, 10, '1', '1', '0', NULL, NULL, NULL, 'developer_board', NULL, '0');
INSERT INTO `menu` VALUES ('2', '1', '0,1,', '管理员', '/system/admin', NULL, 1, '1', '1', '0', NULL, NULL, NULL, 'group', 'New', '0');
INSERT INTO `menu` VALUES ('3', '2', '0,1,2,', '用户列表', NULL, 'admin:users:GET', 0, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('4', '3', '0,1,2,3,', '用户详情', NULL, 'admin:users:detail:GET', 1, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('6', '3', '0,1,2,3,', '用户编辑', NULL, 'admin:users:update:POST', 2, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('7', '2', '0,1,2,', '角色列表', NULL, 'admin:roles:GET', 3, '2', '0', '0', NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `menu` VALUES ('8', '1', '0,1,', '测试', '/test', NULL, 2, '1', '1', '0', NULL, NULL, NULL, 'developer_board', NULL, '0');
INSERT INTO `menu` VALUES ('9', '8', '0,1,8', '测试1', '/test/test1', NULL, 2, '1', '1', '0', NULL, NULL, NULL, NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for policy
-- ----------------------------
DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `policy_name` varchar(128) NOT NULL COMMENT '策略名称',
  `policy_name_cn` varchar(256) DEFAULT NULL COMMENT '策略名称',
  `policy_type` char(1) NOT NULL DEFAULT '0' COMMENT '类型，1-系统策略，2-自定义策略',
  `content` varchar(5000) DEFAULT NULL COMMENT '策略内容',
  `version` varchar(128) NOT NULL COMMENT '策略内容版本号',
  `status` varchar(32) NOT NULL DEFAULT '0' COMMENT '状态',
  `remark` varchar(256) DEFAULT NULL,
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略列表';

-- ----------------------------
-- Records of policy
-- ----------------------------
BEGIN;
INSERT INTO `policy` VALUES ('1', 'cn.test', '自定义策略', '1', '[{\"permissions\":[\"admin:users:*:GET\",\"admin:users:GET\",\"admin:roles:*:GET\"],\"effect\":\"1\"},{\"permissions\":[\"admin:roles:GET\"],\"effect\":\"-1\"}]', '1', '0', NULL, NULL, NULL);
INSERT INTO `policy` VALUES ('2', 'common', '通用策略', '1', '[{\"permissions\":[\"admin:login:GET\",\"admin:check_token:GET\",\"admin:authority:GET\"],\"effect\":\"1\"}]', '1', '0', NULL, NULL, NULL);
INSERT INTO `policy` VALUES ('3', 'admin', '管理员策略', '1', '[{\"permissions\":[\"admin:*\"],\"effect\":\"1\"}]', '1', '0', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
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

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '系统管理员', '系统管理员', NULL, '客户方使用的系统管理员，用于一些常用的基础数据配置。', '0', '2020-06-05 11:28:00', NULL);
INSERT INTO `role` VALUES ('2', '财务', '财务', NULL, '', '0', '2020-06-05 11:28:02', NULL);
INSERT INTO `role` VALUES ('3', '商铺管理员', '商铺管理员', NULL, '', '0', '2020-06-05 11:28:04', NULL);
INSERT INTO `role` VALUES ('4', '普通员工', '普通员工', NULL, '', '0', '2020-06-05 11:28:06', NULL);
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `menu_id` varchar(128) NOT NULL COMMENT '菜单编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单关联表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` VALUES ('1275331506385993730', '1275272223367663618', '8', '2020-06-23 15:35:00');
INSERT INTO `role_menu` VALUES ('1275349476869009410', '2', '3', '2020-06-23 16:46:25');
INSERT INTO `role_menu` VALUES ('1275349476961284098', '2', '2', '2020-06-23 16:46:25');
INSERT INTO `role_menu` VALUES ('1275349477061947393', '2', '1', '2020-06-23 16:46:25');
INSERT INTO `role_menu` VALUES ('1280429208132247554', '1', '2', '2020-07-07 17:11:27');
INSERT INTO `role_menu` VALUES ('1280429208199356418', '1', '19', '2020-07-07 17:11:27');
INSERT INTO `role_menu` VALUES ('1280429208270659585', '1', '1', '2020-07-07 17:11:27');
INSERT INTO `role_menu` VALUES ('1280429208346157057', '1', '13', '2020-07-07 17:11:27');
INSERT INTO `role_menu` VALUES ('1280429208417460225', '1', '12', '2020-07-07 17:11:27');
INSERT INTO `role_menu` VALUES ('1280485933074452482', '4', '12', '2020-07-07 20:56:51');
COMMIT;

-- ----------------------------
-- Table structure for role_policy
-- ----------------------------
DROP TABLE IF EXISTS `role_policy`;
CREATE TABLE `role_policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `policy_id` varchar(128) NOT NULL COMMENT '策略编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与策略关联表';

-- ----------------------------
-- Records of role_policy
-- ----------------------------
BEGIN;
INSERT INTO `role_policy` VALUES ('1', '1', '3', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
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

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1270957117494263809', 'ruyang', NULL, NULL, NULL, '$2a$10$Lg9cGpDFMKRYgDobEl5Rtu1whonVDb2.o9lI11sP03kyZFsbWPIRW', NULL, NULL, '1', NULL, NULL, NULL, '0', '2020-06-11 13:52:45', NULL, NULL, '2020-07-07 21:29:15', '127.0.0.1', NULL);
INSERT INTO `user` VALUES ('1271323640947875841', 'test01', NULL, NULL, NULL, '$2a$10$Nmqihlm5gN47304lJRoh2euk4EMEVA0wEyG5bF.eVJOFtTcvyNqT2', NULL, NULL, NULL, NULL, NULL, NULL, '0', '2020-06-12 14:09:11', NULL, NULL, '2020-06-12 16:52:34', '172.28.223.3', NULL);
INSERT INTO `user` VALUES ('1271327694386176001', 'test02', NULL, NULL, NULL, '$2a$10$W8PxMFzVPFOZWNiYWvsnG.uyH2C4590c2.Rrn1fSZP5NPsu0HBuTq', NULL, NULL, '2', NULL, NULL, NULL, '2', '2020-06-12 14:25:17', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES ('1271377165513981954', 'test03', '小王', '王小二', NULL, '$2a$10$xgTi9VfoiNrLTFV7LCFD4.W.iRnIxm9f1cMiAfw2zDWGpejPidjYa', 'wangxiaoer@quasar.cn', '13800000001', '1', '天大地大，我最大！', NULL, '', '0', '2020-06-12 17:41:52', NULL, '网页注册', '2020-07-03 17:57:39', '127.0.0.1', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_policy
-- ----------------------------
DROP TABLE IF EXISTS `user_policy`;
CREATE TABLE `user_policy` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `user_id` varchar(128) NOT NULL COMMENT '用户编码',
  `policy_id` varchar(128) NOT NULL COMMENT '策略编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与策略关联表';

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `user_id` varchar(128) NOT NULL COMMENT '用户编码',
  `role_id` varchar(128) NOT NULL COMMENT '角色编码',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关联表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES ('1270620369442619393', '1270620369220321281', '3', '2020-06-10 15:34:38');
INSERT INTO `user_role` VALUES ('1270620369509728257', '1270620369220321281', '4', '2020-06-10 15:34:38');
INSERT INTO `user_role` VALUES ('1270620529618894850', '1270620529488871426', '2', '2020-06-10 15:35:16');
INSERT INTO `user_role` VALUES ('1270957117783670785', '1270957117494263809', '1', '2020-06-11 13:52:45');
INSERT INTO `user_role` VALUES ('1274963683113467905', '1271323640947875841', '4', '2020-06-22 15:13:24');
INSERT INTO `user_role` VALUES ('1274963683184771073', '1271323640947875841', '2', '2020-06-22 15:13:24');
INSERT INTO `user_role` VALUES ('1274963714285535234', '1271327694386176001', '2', '2020-06-22 15:13:32');
INSERT INTO `user_role` VALUES ('1275620202024923138', '1271377165513981954', '3', '2020-06-24 10:42:11');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
