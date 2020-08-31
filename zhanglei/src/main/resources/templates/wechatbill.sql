/*
Navicat MySQL Data Transfer

Source Server         : local(root,root)
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : wechatbill

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-03-18 14:54:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for change_detail
-- ----------------------------
DROP TABLE IF EXISTS `change_detail`;
CREATE TABLE `change_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wechat_id` varchar(100) NOT NULL COMMENT '微信账号',
  `order_id` varchar(200) NOT NULL COMMENT '订单号',
  `money` decimal(20,2) DEFAULT NULL COMMENT '交易金额',
  `balance` decimal(20,2) DEFAULT NULL COMMENT '账户余额',
  `balance_source` varchar(100) DEFAULT NULL COMMENT '支出;收入',
  `transaction_type` int(255) DEFAULT NULL COMMENT '交易类型',
  `transaction_time` datetime DEFAULT NULL COMMENT '交易时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `change_union` (`order_id`,`balance_source`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='微信零钱明细表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wechat_id` varchar(100) DEFAULT NULL COMMENT '微信账号',
  `export_key` varchar(255) DEFAULT NULL COMMENT '导出key',
  `userroll_encryption` varchar(255) DEFAULT NULL COMMENT '加密key',
  `userroll_pass_ticket` varchar(255) DEFAULT NULL COMMENT '通过凭证key',
  `balanceuserroll_encryption` varchar(255) DEFAULT NULL COMMENT '余额加密key',
  `expire` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_index` (`wechat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='微信账号key';
