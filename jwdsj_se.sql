/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3307
 Source Schema         : jwdsj_se

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 02/04/2019 11:54:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dict_column
-- ----------------------------
DROP TABLE IF EXISTS `dict_column`;
CREATE TABLE `dict_column`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '外键，关联dict_table表',
  `en_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段英文名',
  `ch_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段中文名',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_id`(`table_id`, `en_column`) USING BTREE,
  INDEX `table_id_2`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段中英对照信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dict_database
-- ----------------------------
DROP TABLE IF EXISTS `dict_database`;
CREATE TABLE `dict_database`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `en_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文库名',
  `ch_database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文库名',
  `detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '库详情',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `en_database`(`en_database`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库中英对照信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dict_table
-- ----------------------------
DROP TABLE IF EXISTS `dict_table`;
CREATE TABLE `dict_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) NOT NULL COMMENT '外键,关联dict_database',
  `en_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '英文表名',
  `ch_table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中文表名',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `database_id`(`database_id`, `en_table`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据表中英对照信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for es_column
-- ----------------------------
DROP TABLE IF EXISTS `es_column`;
CREATE TABLE `es_column`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `index_id` bigint(20) UNSIGNED NOT NULL COMMENT '索引表主键，关联es_index表',
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名',
  `column_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段类型(text、keyword、not_search)',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '索引字段信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for es_index
-- ----------------------------
DROP TABLE IF EXISTS `es_index`;
CREATE TABLE `es_index`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `index_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '索引名',
  `num_shards` tinyint(4) UNSIGNED NOT NULL COMMENT '主分片数',
  `total_docs` bigint(20) UNSIGNED NOT NULL COMMENT '记录数',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '索引信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for mapping_column
-- ----------------------------
DROP TABLE IF EXISTS `mapping_column`;
CREATE TABLE `mapping_column`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '外键 关联info_table',
  `en_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段英文名',
  `ch_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表字段中文名',
  `es_column` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'es中的字段名',
  `is_searched` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否搜索',
  `is_analyzed` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否分词',
  `is_displayed` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否前端展示',
  `boost` double NOT NULL DEFAULT 1 COMMENT '字段权重',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_id_2`(`table_id`, `en_column`) USING BTREE,
  INDEX `table_id`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表字段与索引字段对照信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for mapping_table
-- ----------------------------
DROP TABLE IF EXISTS `mapping_table`;
CREATE TABLE `mapping_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '外键，表id',
  `index_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所在索引',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_id`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表名与索引名对照信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for stat_database
-- ----------------------------
DROP TABLE IF EXISTS `stat_database`;
CREATE TABLE `stat_database`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) UNSIGNED NOT NULL COMMENT '库id，关联dict_database',
  `total_tables` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '表数',
  `total_records` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录数',
  `update_date` date NOT NULL COMMENT '库最后更新日期',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `database_id`(`database_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库数据统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for stat_table
-- ----------------------------
DROP TABLE IF EXISTS `stat_table`;
CREATE TABLE `stat_table`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_id` bigint(20) UNSIGNED NOT NULL COMMENT '库id，关联database_id',
  `table_id` bigint(20) UNSIGNED NOT NULL COMMENT '表id，关联dict_table',
  `total_records` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '记录数',
  `update_date` date NOT NULL COMMENT '表最后更新日期',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_id`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据表数据统计' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sync_table
-- ----------------------------
DROP TABLE IF EXISTS `sync_table`;
CREATE TABLE `sync_table`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `table_id` bigint(20) UNSIGNED NOT NULL COMMENT '需同步的表，关联info_table',
  `table_records` bigint(20) UNSIGNED NOT NULL COMMENT '表记录条数（当数据库中该表的记录数与这里不一致时，进行数据同步操作）',
  `index_records` bigint(20) UNSIGNED NOT NULL COMMENT '索引中该表的记录条数，该值应与table_records相同',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '需要同步的表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
