/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : studio

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2016-06-06 16:49:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blacklist
-- ----------------------------
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist` (
  `player_id` int(11) NOT NULL,
  `black_id` int(11) NOT NULL,
  UNIQUE KEY `p2p` (`player_id`,`black_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of blacklist
-- ----------------------------
INSERT INTO `blacklist` VALUES ('7', '8');
INSERT INTO `blacklist` VALUES ('7', '9');
INSERT INTO `blacklist` VALUES ('37', '21');

-- ----------------------------
-- Table structure for coin_hist
-- ----------------------------
DROP TABLE IF EXISTS `coin_hist`;
CREATE TABLE `coin_hist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `pay_coin` int(11) NOT NULL DEFAULT '0',
  `free_coin` int(11) NOT NULL DEFAULT '0',
  `ex_date` int(10) unsigned DEFAULT NULL,
  `log_date` int(10) unsigned DEFAULT NULL,
  `track_id` bigint(20) unsigned DEFAULT NULL,
  `title` text,
  PRIMARY KEY (`id`),
  KEY `i1` (`user_id`,`log_date`),
  KEY `i2` (`log_date`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of coin_hist
-- ----------------------------
INSERT INTO `coin_hist` VALUES ('108', '49', '1000', '0', null, '1461310801', '1700000049461310686', null);

-- ----------------------------
-- Table structure for follower
-- ----------------------------
DROP TABLE IF EXISTS `follower`;
CREATE TABLE `follower` (
  `player_id` int(11) NOT NULL,
  `follower_id` int(11) NOT NULL,
  UNIQUE KEY `p2p` (`player_id`,`follower_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of follower
-- ----------------------------
INSERT INTO `follower` VALUES ('7', '12');
INSERT INTO `follower` VALUES ('16', '9');
INSERT INTO `follower` VALUES ('21', '33');
INSERT INTO `follower` VALUES ('24', '20');
INSERT INTO `follower` VALUES ('25', '27');
INSERT INTO `follower` VALUES ('25', '28');
INSERT INTO `follower` VALUES ('25', '31');
INSERT INTO `follower` VALUES ('26', '27');
INSERT INTO `follower` VALUES ('27', '25');
INSERT INTO `follower` VALUES ('27', '26');
INSERT INTO `follower` VALUES ('27', '28');
INSERT INTO `follower` VALUES ('28', '26');
INSERT INTO `follower` VALUES ('28', '27');
INSERT INTO `follower` VALUES ('31', '32');
INSERT INTO `follower` VALUES ('32', '31');
INSERT INTO `follower` VALUES ('33', '21');
INSERT INTO `follower` VALUES ('36', '21');
INSERT INTO `follower` VALUES ('36', '33');
INSERT INTO `follower` VALUES ('36', '37');
INSERT INTO `follower` VALUES ('36', '38');
INSERT INTO `follower` VALUES ('37', '33');
INSERT INTO `follower` VALUES ('38', '21');

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` varchar(16) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '',
  `price` int(11) NOT NULL,
  `point` int(11) NOT NULL,
  `disabled` tinyint(4) NOT NULL DEFAULT '0',
  `created_on` int(11) NOT NULL,
  `updated_on` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES ('gift001', 'çŽ«ç‘°èŠ±', '1', '1', '0', '1460520500', '1460520500');

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` bigint(20) unsigned NOT NULL,
  `uuid` varbinary(16) NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `created_on` int(10) unsigned NOT NULL DEFAULT '0',
  `updated_on` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`created_on`),
  KEY `payment_on_uuid` (`uuid`),
  KEY `payment_on_user_id` (`user_id`),
  KEY `payment_on_updated_on` (`updated_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES ('1700000049461310686', 0xB7A09A9AB5A94CE68FD4C65445545831, '49', '10', '1461310686', '1461310801');

-- ----------------------------
-- Table structure for payment_escrow
-- ----------------------------
DROP TABLE IF EXISTS `payment_escrow`;
CREATE TABLE `payment_escrow` (
  `payment_id` bigint(20) unsigned NOT NULL COMMENT 'payment.id',
  `row_id` int(10) unsigned NOT NULL COMMENT 'game_coin.coin_hist.row_id or game_coin.vc_hist.row_id',
  `paybacked_row_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'game_coin.coin_hist.row_id or game_coin.vc_hist.row_id',
  `status` enum('NEW','AUTHORIZED','OPEN','CLOSED','CANCELED','ERROR') NOT NULL DEFAULT 'NEW',
  `is_paybacked` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `price` int(10) unsigned NOT NULL DEFAULT '0',
  `comment` varchar(255) DEFAULT '',
  `created_on` int(10) unsigned NOT NULL DEFAULT '0',
  `updated_on` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`payment_id`,`created_on`),
  KEY `status_and_created_on` (`status`,`created_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment_escrow
-- ----------------------------
INSERT INTO `payment_escrow` VALUES ('1700000049461310686', '108', '108', 'CLOSED', '1', '1000', '', '1461310000', '1461310801');

-- ----------------------------
-- Table structure for payment_item
-- ----------------------------
DROP TABLE IF EXISTS `payment_item`;
CREATE TABLE `payment_item` (
  `payment_id` bigint(20) unsigned NOT NULL COMMENT 'game_payment.payment.id',
  `item_id` varchar(64) NOT NULL,
  `unit_price` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT '',
  `created_on` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`payment_id`,`item_id`,`created_on`),
  KEY `payment_item_on_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment_item
-- ----------------------------
INSERT INTO `payment_item` VALUES ('1700000049461310686', 'gift001', '1', '1000', '', '', '1461310686');

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick` varchar(100) NOT NULL,
  `city` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `intro` varchar(255) DEFAULT NULL,
  `alias_id` varchar(100) CHARACTER SET utf8 NOT NULL,
  `alias_type` int(11) NOT NULL,
  `created_on` int(11) NOT NULL,
  `updated_on` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_INX` (`alias_id`,`alias_type`),
  KEY `nick` (`nick`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COMMENT='ç›´æ’­ç”¨æˆ·è¡¨';

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES ('49', '???200', null, '1', 'http://avatar.com/1.jpg', null, 'open221873113312256523251', '1', '1461310441', '1461310441');
INSERT INTO `player` VALUES ('50', '???300', null, '1', 'http://avatar.com/1.jpg', null, 'open2218731113312256523251', '1', '1461310516', '1461310516');

-- ----------------------------
-- Table structure for player_bank_statistic
-- ----------------------------
DROP TABLE IF EXISTS `player_bank_statistic`;
CREATE TABLE `player_bank_statistic` (
  `player_id` int(11) NOT NULL,
  `coin_in` int(11) NOT NULL,
  `coin_out` int(11) NOT NULL,
  `point_in` int(11) NOT NULL,
  `point_out` int(11) NOT NULL,
  `updated_on` int(11) NOT NULL,
  UNIQUE KEY `pid` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_bank_statistic
-- ----------------------------
INSERT INTO `player_bank_statistic` VALUES ('49', '0', '0', '0', '0', '1461310801');
INSERT INTO `player_bank_statistic` VALUES ('50', '0', '0', '1000', '0', '1461310686');

-- ----------------------------
-- Table structure for player_coin
-- ----------------------------
DROP TABLE IF EXISTS `player_coin`;
CREATE TABLE `player_coin` (
  `player_id` int(11) NOT NULL,
  `coin` int(11) NOT NULL,
  `updated_on` int(11) NOT NULL,
  UNIQUE KEY `pid` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of player_coin
-- ----------------------------
INSERT INTO `player_coin` VALUES ('49', '10000', '1461310441');
INSERT INTO `player_coin` VALUES ('50', '10000', '1461310516');

-- ----------------------------
-- Table structure for player_point
-- ----------------------------
DROP TABLE IF EXISTS `player_point`;
CREATE TABLE `player_point` (
  `player_id` int(11) NOT NULL,
  `point` int(11) NOT NULL,
  `updated_on` int(11) NOT NULL,
  UNIQUE KEY `pid` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of player_point
-- ----------------------------
INSERT INTO `player_point` VALUES ('49', '10000', '1461310441');
INSERT INTO `player_point` VALUES ('50', '11000', '1461310516');
