-- ============================================================================
-- SmartStock Inventory Management System - Schema Only
-- ============================================================================
-- Project: Capstone Project - Inventory System with QR Integration
-- Database: smartstock_clean
-- Version: 1.0.0 (Build 20251202)
-- Date: December 2, 2025
-- Compatible: MySQL 5.7+, MariaDB 10.2+
-- 
-- SCHEMA ONLY: Empty database structure without sample data
-- Use for: Fresh installation, production deployment, custom data entry
-- ============================================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

DROP DATABASE IF EXISTS smartstock_clean;
CREATE DATABASE smartstock_clean CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartstock_clean;

-- ============================================================================
-- TABLE STRUCTURES
-- ============================================================================

-- Users table
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_role` varchar(20) NOT NULL DEFAULT 'Admin',
  `admin_id` int(11) DEFAULT NULL,
  `default_markup_percent` decimal(5,2) NOT NULL DEFAULT 30.00,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_users_admin` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Stores table
CREATE TABLE `stores` (
  `store_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `store_name` varchar(100) NOT NULL,
  `location` varchar(150) DEFAULT NULL,
  `contact` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`store_id`),
  KEY `fk_stores_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uq_category_user` (`user_id`,`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Units table
CREATE TABLE `units` (
  `unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_name` varchar(50) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`unit_id`),
  UNIQUE KEY `uq_unit_user` (`user_id`,`unit_name`),
  KEY `fk_units_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Products table
CREATE TABLE `products` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `name` varchar(120) NOT NULL,
  `unit_of_measurement` varchar(20) NOT NULL,
  `cost_price` decimal(10,2) NOT NULL DEFAULT 0.00,
  `retail_price` decimal(10,2) NOT NULL DEFAULT 0.00,
  `markup_percent` decimal(5,2) DEFAULT NULL,
  `quantity_in_stock` int(11) NOT NULL DEFAULT 0,
  `quantity_damaged` int(11) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`product_id`,`user_id`),
  KEY `fk_products_user` (`user_id`),
  KEY `fk_products_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sales table
CREATE TABLE `sales` (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT,
  `sale_date` datetime NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(12,2) NOT NULL DEFAULT 0.00,
  `payment_received` decimal(12,2) DEFAULT NULL,
  `change_amount` decimal(12,2) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`sale_id`),
  KEY `fk_sales_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sale items table
CREATE TABLE `sale_items` (
  `sale_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity_sold` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `cost_price` decimal(10,2) NOT NULL DEFAULT 0.00,
  `subtotal` decimal(12,2) NOT NULL,
  PRIMARY KEY (`sale_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Stock log table
CREATE TABLE `stock_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `quantity_changed` int(11) NOT NULL,
  `log_type` varchar(30) NOT NULL,
  `notes` varchar(200) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `log_date` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`log_id`),
  KEY `fk_stock_log_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- FOREIGN KEY CONSTRAINTS
-- ============================================================================

ALTER TABLE `users`
  ADD CONSTRAINT `fk_users_admin` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `stores`
  ADD CONSTRAINT `fk_stores_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `categories`
  ADD CONSTRAINT `fk_categories_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_products_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sales`
  ADD CONSTRAINT `fk_sales_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sale_items`
  ADD CONSTRAINT `fk_sale_items_sale` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `stock_log`
  ADD CONSTRAINT `fk_stock_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `units`
  ADD CONSTRAINT `fk_units_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ============================================================================
-- DATABASE SCHEMA READY
-- ============================================================================
-- 
-- Empty database structure created successfully.
-- All tables are ready for data entry through the SmartStock application.
-- 
-- TABLE SUMMARY:
--   - users: User accounts (Admin/Employee)
--   - stores: Store information
--   - categories: Product categories
--   - products: Product inventory with cost/retail pricing
--   - sales: Sales transaction headers
--   - sale_items: Sales transaction details with historical cost tracking
--   - stock_log: Complete inventory movement history
-- 
-- FEATURES SUPPORTED:
--   ✓ Historical cost tracking (sale_items.cost_price)
--   ✓ Multi-user support with role-based access
--   ✓ Category-based product organization
--   ✓ Stock movement logging (STOCK-IN, SALE, CUSTOMER-RETURN, etc.)
--   ✓ Damaged inventory tracking
--   ✓ Referential integrity with foreign keys
--   ✓ Automatic timestamps
--   ✓ UTF-8 support for international characters
-- 
-- NEXT STEPS:
--   1. Import this schema into MySQL
--   2. Launch SmartStock application
--   3. Create your first user account through signup
--   4. Configure store settings
--   5. Add categories and products
-- ============================================================================

COMMIT;

SELECT 
    'Database schema created successfully!' AS Status,
    '7 tables initialized' AS Tables,
    'No sample data loaded' AS Data,
    'Ready for production use' AS ReadyStatus;
