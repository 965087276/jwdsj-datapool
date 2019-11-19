/*
Navicat MySQL Data Transfer

Source Server         : 10.10.169.121
Source Server Version : 80015
Source Host           : 10.10.169.121:3306
Source Database       : dbpool

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-11-19 11:20:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dict_column
-- ----------------------------
DROP TABLE IF EXISTS `dict_column`;
CREATE TABLE `dict_column` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) unsigned NOT NULL DEFAULT '3' COMMENT '外键，关联dict_database表',
  `table_id` bigint(20) NOT NULL COMMENT '外键，关联dict_table表',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名(冗余字段)',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文表名(冗余字段)',
  `en_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段英文名',
  `ch_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段中文名',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `table_id` (`table_id`,`en_column`) USING BTREE,
  UNIQUE KEY `en_database_2` (`en_database`,`en_table`,`en_column`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=480326 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='字段中英对照信息表';

-- ----------------------------
-- Table structure for dict_database
-- ----------------------------
DROP TABLE IF EXISTS `dict_database`;
CREATE TABLE `dict_database` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名',
  `ch_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文库名',
  `detail` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '库详情',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `en_database` (`en_database`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=270002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='数据库中英对照信息表';

-- ----------------------------
-- Table structure for dict_table
-- ----------------------------
DROP TABLE IF EXISTS `dict_table`;
CREATE TABLE `dict_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) NOT NULL COMMENT '外键,关联dict_database',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名(冗余字段）',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文表名',
  `ch_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文表名',
  `is_add_to_se` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否加入到搜索引擎',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `database_id` (`database_id`,`en_table`) USING BTREE,
  KEY `en_database` (`en_database`,`en_table`)
) ENGINE=InnoDB AUTO_INCREMENT=510019 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='数据表中英对照信息表';

-- ----------------------------
-- Table structure for es_column
-- ----------------------------
DROP TABLE IF EXISTS `es_column`;
CREATE TABLE `es_column` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `index_id` bigint(20) unsigned NOT NULL COMMENT '索引表主键，关联es_index表',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段类型(text、keyword、not_search)',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_id_2` (`index_id`,`name`),
  KEY `index_id` (`index_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=150036 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='索引字段信息表';

-- ----------------------------
-- Table structure for es_index
-- ----------------------------
DROP TABLE IF EXISTS `es_index`;
CREATE TABLE `es_index` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `index_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '索引名',
  `num_shards` tinyint(4) unsigned NOT NULL COMMENT '主分片数',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_name` (`index_name`)
) ENGINE=InnoDB AUTO_INCREMENT=30002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='索引信息表';

-- ----------------------------
-- Table structure for mapping_column
-- ----------------------------
DROP TABLE IF EXISTS `mapping_column`;
CREATE TABLE `mapping_column` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '外键 关联dict_table',
  `column_id` bigint(20) NOT NULL COMMENT '外键 关联dict_column',
  `en_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段英文名（冗余字段）',
  `ch_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段中文名（冗余字段）',
  `es_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'es中的字段名',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段类型（针对es来说，TEXT、KEYWORD、NOT_SEARCH）',
  `is_searched` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否搜索',
  `is_analyzed` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否分词',
  `is_displayed` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否前端展示',
  `boost` double NOT NULL DEFAULT '1' COMMENT '字段权重',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `column_id` (`column_id`) USING BTREE,
  UNIQUE KEY `table_id_2` (`table_id`,`es_column`) USING BTREE,
  UNIQUE KEY `table_id_3` (`table_id`,`en_column`),
  KEY `table_id` (`table_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=420144 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='表字段与索引字段对照信息表';

-- ----------------------------
-- Table structure for mapping_table
-- ----------------------------
DROP TABLE IF EXISTS `mapping_table`;
CREATE TABLE `mapping_table` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) NOT NULL COMMENT '外键，关联dict_database',
  `table_id` bigint(20) unsigned NOT NULL COMMENT '外键，关联dict_table',
  `index_id` bigint(20) unsigned NOT NULL COMMENT '所在索引, 关联es_index',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名(冗余字段)',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文表名（冗余字段）',
  `ch_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文表名（冗余字段）',
  `index_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '索引名（冗余字段）',
  `table_records` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '表记录数',
  `index_records` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '索引中的记录数',
  `update_date` date NOT NULL COMMENT '上次更新日期',
  `update_period` smallint(11) unsigned NOT NULL COMMENT '更新周期',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `table_id` (`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=420006 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='表名与索引名对照信息表 同步信息表';

-- ----------------------------
-- Table structure for se_table
-- ----------------------------
DROP TABLE IF EXISTS `se_table`;
CREATE TABLE `se_table` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) unsigned NOT NULL COMMENT '外键，关联dict_database',
  `table_id` bigint(20) unsigned NOT NULL COMMENT '外键，关联dict_table',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表英文名（冗余字段）',
  `ch_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表中文名（冗余字段）',
  `is_sync` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否同步',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `table_id` (`table_id`) USING BTREE,
  UNIQUE KEY `database_id_2` (`database_id`,`en_table`)
) ENGINE=InnoDB AUTO_INCREMENT=390006 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='搜索引擎的表的集合。包括了已加入到同步队列中的表和未加入到同步队列中的表';

-- ----------------------------
-- Table structure for stat_column
-- ----------------------------
DROP TABLE IF EXISTS `stat_column`;
CREATE TABLE `stat_column` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `database_id` bigint(20) unsigned NOT NULL COMMENT '库id，关联dict_database',
  `table_id` bigint(20) unsigned NOT NULL COMMENT '表id，关联dict_table',
  `column_id` bigint(20) unsigned NOT NULL COMMENT '字段id，关联dict_column',
  `en_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文字段名（冗余字段）',
  `ch_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文字段名',
  `update_date` date NOT NULL DEFAULT '2019-01-01' COMMENT '更新时间',
  `is_defect` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为缺陷字段',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `column_id` (`column_id`) USING BTREE,
  KEY `table_id` (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32989 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for stat_database
-- ----------------------------
DROP TABLE IF EXISTS `stat_database`;
CREATE TABLE `stat_database` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) unsigned NOT NULL COMMENT '库id，关联dict_database',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名（冗余字段）',
  `ch_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文库名（冗余字段）',
  `total_tables` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '表数',
  `total_records` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '记录数',
  `update_date` date NOT NULL DEFAULT '2019-01-01' COMMENT '库最后更新日期',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `database_id` (`database_id`) USING BTREE,
  UNIQUE KEY `en_database` (`en_database`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='数据库数据统计';

-- ----------------------------
-- Table structure for stat_table
-- ----------------------------
DROP TABLE IF EXISTS `stat_table`;
CREATE TABLE `stat_table` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) unsigned NOT NULL COMMENT '库id，关联database_id',
  `table_id` bigint(20) unsigned NOT NULL COMMENT '表id，关联dict_table',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据表英文名（冗余字段）',
  `ch_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据表中文名（冗余字段）',
  `total_records` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '记录数',
  `total_columns` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '字段数',
  `defect_columns` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '缺陷字段数',
  `defect_rate` float unsigned NOT NULL DEFAULT '0' COMMENT '缂洪櫡鐜?',
  `update_date` date NOT NULL DEFAULT '2019-01-01' COMMENT '表最后更新日期',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `table_id` (`table_id`) USING BTREE,
  KEY `database_id` (`database_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2058 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='数据表数据统计';
