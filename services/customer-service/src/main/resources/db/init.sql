CREATE DATABASE IF NOT EXISTS customer_schema;
USE customer_schema;

CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(7) NOT NULL,
  `name` varchar(255) NOT NULL,
  `legal_id` varchar(255) NOT NULL,
  `type` enum('RETAIL','CORPORATE','INVESTMENT') NOT NULL,
  `address` varchar(255) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`)
);


CREATE TABLE `account_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(7) DEFAULT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_customer_id` (`customer_id`),
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE
);
