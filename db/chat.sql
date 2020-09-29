/*
 Navicat Premium Data Transfer

 Source Server         : Bata's Link
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : chat

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 29/09/2020 16:19:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户账号',
  `password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户密码',
  `nickname` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `signature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `avatar` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `background` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景图像地址',
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, '123', '123', 'O泡', '123', 'O泡时间到', 'head1', 'background3');
INSERT INTO `account` VALUES (2, '111', '111', '老司机', NULL, '开车时间到', 'head2', 'background2');
INSERT INTO `account` VALUES (3, '222', '222', '保加利亚妖王', '妖王', '一起捡肥皂', 'head3', 'background4');
INSERT INTO `account` VALUES (9, '42898', '111', '啊啊啊', '欠缺', NULL, 'head6', 'background3');
INSERT INTO `account` VALUES (10, '98315', '111', '嗡嗡嗡', '搜索', NULL, 'head6', 'background3');

-- ----------------------------
-- Table structure for companion
-- ----------------------------
DROP TABLE IF EXISTS `companion`;
CREATE TABLE `companion`  (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `my_account` int(11) NOT NULL COMMENT '我的账号对应eid',
  `friend_account` int(11) NOT NULL COMMENT '朋友账号对应的eid',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `mygroup` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组',
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of companion
-- ----------------------------
INSERT INTO `companion` VALUES (1, 1, 2, '基友', '我的好友');
INSERT INTO `companion` VALUES (2, 2, 1, '好基友', '我的好友');

-- ----------------------------
-- Table structure for grouping
-- ----------------------------
DROP TABLE IF EXISTS `grouping`;
CREATE TABLE `grouping`  (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分组名称',
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grouping
-- ----------------------------
INSERT INTO `grouping` VALUES (1, '1', '我的好友');
INSERT INTO `grouping` VALUES (2, '2', '我的好友');
INSERT INTO `grouping` VALUES (3, '3', '我的好友');
INSERT INTO `grouping` VALUES (5, '9', '我的好友');
INSERT INTO `grouping` VALUES (6, '10', '我的好友');

-- ----------------------------
-- Table structure for offline_message
-- ----------------------------
DROP TABLE IF EXISTS `offline_message`;
CREATE TABLE `offline_message`  (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `from_account_id` int(11) NOT NULL COMMENT '发送消息的账户对应的id',
  `to_account_id` int(11) NOT NULL COMMENT '接收账户对应的id',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送的消息',
  `message_time` timestamp(0) NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of offline_message
-- ----------------------------
INSERT INTO `offline_message` VALUES (1, 2, 1, '你好', '2020-06-04 00:00:00');
INSERT INTO `offline_message` VALUES (8, 1, 2, '你好啊', '2020-06-06 00:30:36');

-- ----------------------------
-- Table structure for online
-- ----------------------------
DROP TABLE IF EXISTS `online`;
CREATE TABLE `online`  (
  `eid` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '在线账号',
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of online
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
