/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50636
Source Host           : localhost:3306
Source Database       : alauwahios

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-06-11 22:28:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `baidu_tieba`
-- ----------------------------
DROP TABLE IF EXISTS `baidu_tieba`;
CREATE TABLE `baidu_tieba` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tiebaKw` varchar(200) NOT NULL DEFAULT '' COMMENT '贴吧kw',
  `tiebaName` varchar(500) DEFAULT '' COMMENT '贴吧文字',
  `tiebaLink` varchar(500) DEFAULT '' COMMENT '贴吧链接',
  `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
  `createTime` datetime NOT NULL COMMENT '抓取时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
  `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
  `hot` int(11) NOT NULL DEFAULT '1' COMMENT '热度',
  `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
  `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tiebakw_idx` (`tiebaKw`) USING BTREE,
  KEY `createTime_idx` (`createTime`) USING BTREE,
  KEY `updateTime_idx` (`updateTime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69549 DEFAULT CHARSET=utf8 COMMENT='贴吧表';

-- ----------------------------
-- Table structure for `baidu_tiezi`
-- ----------------------------
DROP TABLE IF EXISTS `baidu_tiezi`;
CREATE TABLE `baidu_tiezi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `tieziId` bigint(22) NOT NULL DEFAULT '0' COMMENT '帖子ID',
  `tieziName` varchar(500) DEFAULT '' COMMENT '帖子文字',
  `tieziLink` varchar(500) DEFAULT '' COMMENT '帖子链接',
  `tiebaName` varchar(500) DEFAULT '' COMMENT '贴吧文字',
  `tiebaLink` varchar(500) DEFAULT '' COMMENT '贴吧链接',
  `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
  `postTime` datetime DEFAULT NULL COMMENT '发布时间',
  `createTime` datetime NOT NULL COMMENT '抓取时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
  `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
  `hot` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
  `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
  `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tieziid_idx` (`tieziId`) USING BTREE,
  KEY `createTime_idx` (`createTime`) USING BTREE,
  KEY `updateTime_idx` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='贴子表';


-- ----------------------------
-- Table structure for `baidu_wangpan`
-- ----------------------------
DROP TABLE IF EXISTS `baidu_wangpan`;
CREATE TABLE `baidu_wangpan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `panShortLink` varchar(50) NOT NULL DEFAULT '' COMMENT '网盘短链接',
  `panLink` varchar(200) DEFAULT '' COMMENT '网盘链接',
  `replyName` varchar(500) DEFAULT '' COMMENT '回复文字',
  `replyLink` varchar(500) DEFAULT '' COMMENT '回复链接',
  `tiebaName` varchar(500) DEFAULT '' COMMENT '贴吧文字',
  `tiebaLink` varchar(500) DEFAULT '' COMMENT '贴吧链接',
  `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
  `postTime` datetime DEFAULT NULL COMMENT '发布时间',
  `createTime` datetime NOT NULL COMMENT '抓取时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
  `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
  `hot` int(11) NOT NULL DEFAULT '1' COMMENT '热度',
  `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
  `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `panShortlink_idx` (`panShortLink`) USING BTREE,
  KEY `createTime_idx` (`createTime`) USING BTREE,
  KEY `updateTime_idx` (`updateTime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71588 DEFAULT CHARSET=utf8 COMMENT='贴吧链接表';

-- ----------------------------
-- Table structure for `baidu_yun`
-- ----------------------------
DROP TABLE IF EXISTS `baidu_yun`;
CREATE TABLE `baidu_yun` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `panShortLink` varchar(50) NOT NULL DEFAULT '' COMMENT '网盘短链接',
  `panLink` varchar(200) DEFAULT '' COMMENT '网盘链接',
  `shortLink` varchar(200) DEFAULT '' COMMENT '短链接',
  `createTime` datetime NOT NULL COMMENT '抓取时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类别',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1 可用 0 不可用。',
  `star` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '人工排序，默认为0',
  `hot` int(11) NOT NULL DEFAULT '1' COMMENT '热度',
  `visits` int(11) NOT NULL DEFAULT '0' COMMENT '访问次数',
  `remark` varchar(500) DEFAULT '' COMMENT '备用字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `panShortlink_idx` (`panShortLink`) USING BTREE,
  KEY `createTime_idx` (`createTime`) USING BTREE,
  KEY `updateTime_idx` (`updateTime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81846 DEFAULT CHARSET=utf8 COMMENT='baiduyun.xyz表';

DROP TABLE IF EXISTS `request_cnt`;
CREATE TABLE `request_cnt` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `serverIp` varchar(40) DEFAULT '' COMMENT 'IP，默认本地',
  `urlPattern` varchar(100) DEFAULT '' COMMENT 'url匹配',
  `totalRequestCnt` bigint(11) NOT NULL DEFAULT '0' COMMENT '总请求数',
  `overtimeRequestCnt` bigint(11) NOT NULL DEFAULT '0' COMMENT '超时请求数',
  `respTime` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '响应时间',
  `matchedRequestCnt` bigint(11) NOT NULL DEFAULT '0' COMMENT 'url匹配请求数',
  `overtimeMatchedRequestCnt` bigint(11) NOT NULL DEFAULT '0' COMMENT 'url匹配超时请求数',
  `createTime` datetime NOT NULL COMMENT '当前时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=656 DEFAULT CHARSET=utf8 COMMENT='web请求计数表';