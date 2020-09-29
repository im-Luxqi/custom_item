/*
 Navicat Premium Data Transfer

 Source Server         : mysql_local
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : adidas_20201111

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 28/09/2020 17:16:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cus_big_wheel
-- ----------------------------
DROP TABLE IF EXISTS `cus_big_wheel`;
CREATE TABLE `cus_big_wheel`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `context` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详细',
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `fly_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '别名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cus_big_wheel_log
-- ----------------------------
DROP TABLE IF EXISTS `cus_big_wheel_log`;
CREATE TABLE `cus_big_wheel_log`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `buyer_nick` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '混淆昵称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `gateway` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入口',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



INSERT INTO `sys_key_value`(`k`, `type`, `v`) VALUES ('act_end_time', 'type_act_setting', '2020-10-26');
INSERT INTO `sys_key_value`(`k`, `type`, `v`) VALUES ('act_rule', 'type_act_setting', '规则');
INSERT INTO `sys_key_value`(`k`, `type`, `v`) VALUES ('act_start_time', 'type_act_setting', '2020-09-29');

INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t0', '', 0, 'aaabbbccc', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601389580930&di=4607344ea555354e3fe15b8fcc349fe2&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171126%2F391501c7197a419692b0b97985016166.jpeg', '1', '邀请3人50元券', 1, NULL, 100, 0, 100, 'GOODS', 'INVITE');
INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t1', '', 0, 'aaabbbccc', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601389580930&di=4607344ea555354e3fe15b8fcc349fe2&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171126%2F391501c7197a419692b0b97985016166.jpeg', '1', '首次抽奖必中100元券', 1, NULL, 99, 1, 100, 'GOODS', 'FIRSTLUCKY');
INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t2', '', 1, '', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601389376717&di=2417db5dabb15ddb3a9ea2a0be1ce82e&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Ffc1f4134970a304eabba89e6ddc8a786c9175c15.jpg', '1', '变态辣', 3, NULL, 99, 1, 100, 'GOODS', 'POOL');
INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t3', '', 2, '', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2090677192,1127185527&fm=26&gp=0.jpg', '1', '中辣', 2, NULL, 100, 0, 100, 'GOODS', 'POOL');
INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t4', '', 2, '', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601389514517&di=3d4ea09816afb5adb3a83e98031cee8e&imgtype=0&src=http%3A%2F%2Fecju.jx.qnzs.youth.cn%2Fassets%2Ffiles%2F20160927%2F1474934955511504.jpg', '1', '小辣', 1, NULL, 99, 1, 100, 'GOODS', 'POOL');
INSERT INTO `sys_award`(`id`, `award_level`, `award_level_sign`, `ename`, `img`, `lucky_value`, `name`, `pool_level`, `price`, `remain_num`, `send_num`, `total_num`, `type`, `use_way`) VALUES ('t5', '', 2, 'aaabbbccc', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601389580930&di=4607344ea555354e3fe15b8fcc349fe2&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171126%2F391501c7197a419692b0b97985016166.jpeg', '0', '微辣', 1, NULL, 100, 0, 100, 'COUPON', 'POOL');
