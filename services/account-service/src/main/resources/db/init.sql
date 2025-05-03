CREATE DATABASE IF NOT EXISTS account_schema;
USE account_schema;

CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` varchar(10) NOT NULL,
  `customer_id` varchar(7) NOT NULL,
  `type` enum('SALARY','SAVING','INVESTMENT') NOT NULL,
  `status` enum('ACTIVE','INACTIVE','CLOSED') NOT NULL,
  `balance` decimal(15,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_account` (`account_id`),
  KEY `idx_customer_id` (`customer_id`)
);

CREATE TABLE `customer_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

