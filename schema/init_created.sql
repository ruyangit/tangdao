/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.56.5
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 192.168.56.5:3306
 Source Schema         : tangdao_base

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 25/02/2020 15:07:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_action
-- ----------------------------
DROP TABLE IF EXISTS `sys_action`;
CREATE TABLE `sys_action` (
  `action_id` varchar(64) NOT NULL COMMENT '编号',
  `action_code` varchar(64) DEFAULT NULL,
  `action_name` varchar(64) DEFAULT NULL,
  `service_id` varchar(64) DEFAULT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作表';

-- ----------------------------
-- Table structure for sys_assertion
-- ----------------------------
DROP TABLE IF EXISTS `sys_assertion`;
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

-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `domain_id` varchar(64) NOT NULL COMMENT '编号',
  `domain_type` char(1) DEFAULT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `domain_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='域管理';

-- ----------------------------
-- Table structure for sys_policy
-- ----------------------------
DROP TABLE IF EXISTS `sys_policy`;
CREATE TABLE `sys_policy` (
  `policy_id` varchar(64) NOT NULL COMMENT '编号',
  `policy_name` varchar(64) DEFAULT NULL,
  `policy_type` char(1) NOT NULL DEFAULT '0' COMMENT '策略类型',
  `domain_id` varchar(64) DEFAULT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='策略表';

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `resource_id` varchar(64) NOT NULL COMMENT '编号',
  `resource_code` varchar(64) DEFAULT NULL,
  `resource_name` varchar(64) DEFAULT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` varchar(64) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `domain_id` varchar(64) NOT NULL COMMENT '编号',
  `role_code` varchar(1000) DEFAULT NULL COMMENT '权限标识',
  `role_type` char(1) DEFAULT NULL COMMENT '角色类型',
  `self_serve` char(1) DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_service`;
CREATE TABLE `sys_service` (
  `service_id` varchar(64) NOT NULL COMMENT '编号',
  `service_code` varchar(64) DEFAULT NULL,
  `service_name` varchar(64) DEFAULT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2停用）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `domain_id` varchar(64) DEFAULT NULL,
  `service_endpoint` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
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
  `domain_id` varchar(64) NOT NULL COMMENT '编号',
  `user_type` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('admin', 'admin', '$2a$10$SBQgz0AC1VjHlEPIorlWNuB/eorm3PO50zpPhCDs1qnwmvq4IlzDO', '系统管理员', 'ruyangit@163.com', '15888888888', '', NULL, '/images/default.jpg', NULL, '172.28.220.60', '2019-05-29 17:56:23', NULL, NULL, '0', 'system', '2018-09-26 17:49:23', 'system', '2019-12-23 13:51:03', '客户方使用的系统管理员，用于一些常用的基础数据配置。', NULL, '', NULL);
INSERT INTO `sys_user` VALUES ('user', 'user', '$2a$10$D7kzh.bqcmrKHWc/5.NoEeE4IKQJEudZgWeAy1kfFrP2J9xwCSGbO', '超级管理员', 'admin@aliyeye.com', '13800000000', '', '2', NULL, '', '192.168.113.1', '2020-01-20 09:11:50', NULL, NULL, '0', 'system', '2018-09-26 17:49:23', 'system', '2020-01-20 09:11:50', '开发者使用的最高级别管理员，主要用于开发和调试。', NULL, '', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_policy
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_policy`;
CREATE TABLE `sys_user_policy` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `policy_id` varchar(64) NOT NULL COMMENT '策略编码',
  PRIMARY KEY (`user_id`,`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与策略关联表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(100) NOT NULL COMMENT '用户编码',
  `role_id` varchar(64) NOT NULL COMMENT '角色编码',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关联表';

SET FOREIGN_KEY_CHECKS = 1;
