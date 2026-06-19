-- fishfrineds 数据库初始化脚本
-- 字符集: utf8mb4 + utf8mb4_unicode_ci

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE DATABASE IF NOT EXISTS fishfrineds
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE fishfrineds;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(100) DEFAULT '',
  `avatar` VARCHAR(500) DEFAULT '',
  `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT 'USER / ADMIN',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 钓点信息表 (R003)
CREATE TABLE IF NOT EXISTS `fishing_spots` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `location` VARCHAR(500) NOT NULL,
  `location_desc` VARCHAR(1000) DEFAULT '',
  `fish_types` VARCHAR(500) DEFAULT '',
  `best_season` VARCHAR(100) DEFAULT '',
  `best_time` VARCHAR(100) DEFAULT '',
  `facility_desc` VARCHAR(1000) DEFAULT '',
  `fee_info` VARCHAR(500) DEFAULT '',
  `contact_info` VARCHAR(200) DEFAULT '',
  `image_url` VARCHAR(500) DEFAULT '',
  `content` TEXT,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (`created_by`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 鱼获分享表 (R004)
CREATE TABLE IF NOT EXISTS `fish_catches` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `fish_type` VARCHAR(100) NOT NULL,
  `fish_weight` DECIMAL(8, 2) DEFAULT NULL COMMENT '重量(kg)',
  `fish_length` DECIMAL(8, 2) DEFAULT NULL COMMENT '长度(cm)',
  `fishing_spot` VARCHAR(200) DEFAULT '',
  `fishing_method` VARCHAR(100) DEFAULT '',
  `bait_used` VARCHAR(200) DEFAULT '',
  `weather` VARCHAR(100) DEFAULT '',
  `catch_date` DATETIME DEFAULT NULL,
  `image_url` VARCHAR(500) DEFAULT '',
  `content` TEXT,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (`created_by`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 生活分享表 (R005)
CREATE TABLE IF NOT EXISTS `life_shares` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `share_type` VARCHAR(50) DEFAULT '',
  `image_url` VARCHAR(500) DEFAULT '',
  `content` TEXT,
  `view_count` INT DEFAULT 0,
  `like_count` INT DEFAULT 0,
  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (`created_by`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 钓友信息表 (R006)
CREATE TABLE IF NOT EXISTS `angler_profiles` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `bio` TEXT,
  `experience_years` INT DEFAULT 0,
  `speciality` VARCHAR(200) DEFAULT '',
  `contact` VARCHAR(200) DEFAULT '',
  `image_url` VARCHAR(500) DEFAULT '',
  `user_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 钓具销售表 (R007)
CREATE TABLE IF NOT EXISTS `gear_sales` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `description` TEXT,
  `price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  `category` VARCHAR(100) DEFAULT '',
  `condition_status` VARCHAR(50) DEFAULT '全新' COMMENT '全新/二手',
  `contact` VARCHAR(200) DEFAULT '',
  `image_url` VARCHAR(500) DEFAULT '',
  `user_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试用户 (密码为 BCrypt 加密后的 "password123")
INSERT INTO `users` (`username`, `password`, `nickname`, `role`) VALUES
  ('admin', '$2a$10$lh0aqvB1IV7gZaypuMw7weVjmPDut8.eFsbF7.GOQsjf6uqZqg2tO', '管理员', 'ADMIN'),
  ('testuser', '$2a$10$lh0aqvB1IV7gZaypuMw7weVjmPDut8.eFsbF7.GOQsjf6uqZqg2tO', '测试钓友', 'USER');

-- 插入测试钓点数据
INSERT INTO `fishing_spots` (`title`, `location`, `location_desc`, `fish_types`, `best_season`, `created_by`) VALUES
  ('西湖垂钓点', '浙江省杭州市西湖区', '西湖边上的绝佳钓鱼位置，水质清澈，鱼种丰富', '鲫鱼、鲤鱼、草鱼', '春秋两季', 1),
  ('千岛湖野钓区', '浙江省淳安县千岛湖', '千岛湖开放垂钓区域，大物频出', '鲢鳙、青鱼、翘嘴', '全年可钓', 2);

-- 插入测试鱼获数据
INSERT INTO `fish_catches` (`title`, `fish_type`, `fish_weight`, `fish_length`, `fishing_spot`, `fishing_method`, `created_by`) VALUES
  ('今日大鲤鱼', '鲤鱼', 3.50, 45.00, '西湖垂钓点', '台钓', 2),
  ('千岛湖巨物', '鲢鳙', 12.80, 85.00, '千岛湖野钓区', '海竿', 1);

-- 插入测试生活分享
INSERT INTO `life_shares` (`title`, `share_type`, `content`, `view_count`, `like_count`, `created_by`) VALUES
  ('周末钓鱼日记', '钓鱼日记', '今天天气很好，和朋友们一起去钓鱼，收获满满！', 128, 23, 1),
  ('新手入门指南', '经验分享', '分享一些钓鱼入门的小技巧，希望对大家有所帮助。', 256, 45, 2);

-- 验证字符集
SELECT @@character_set_database AS db_charset, @@collation_database AS db_collation;
