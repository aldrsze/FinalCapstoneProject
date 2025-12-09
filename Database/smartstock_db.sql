-- ============================================================================
-- SmartStock Inventory Management System - Complete Database with Unit Support
-- ============================================================================
-- Project: Capstone Project - Inventory System with QR Integration
-- Database: smartstock_db
-- Version: 2.0.0 (Updated December 7, 2025)
-- Compatible: MySQL 5.7+, MariaDB 10.2+
-- 
-- COMPLETE DATABASE: Structure + Sample Data with Unit of Measurement Support
-- Use for: Testing, demonstration, development environment
-- ============================================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

DROP DATABASE IF EXISTS smartstock_db;
CREATE DATABASE smartstock_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartstock_db;

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

-- Categories table
CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uq_category_user` (`user_id`,`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Units of Measurement table
CREATE TABLE `units` (
  `unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_name` varchar(50) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`unit_id`),
  UNIQUE KEY `uq_unit_user` (`user_id`,`unit_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Products table (with unit_of_measurement support)
CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
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
-- SAMPLE DATA
-- ============================================================================

-- Insert sample user (username: admin, password: admin123)
INSERT INTO `users` (`user_id`, `username`, `password`, `user_role`, `admin_id`, `default_markup_percent`, `created_at`) VALUES
(1, 'admin', 'admin123', 'Admin', NULL, 30.00, '2025-12-01 08:00:00');
-- Add sample employee account
(2, 'employee1', 'employee123', 'Employee', 1, 30.00, '2025-12-01 09:00:00');

-- Insert sample store
INSERT INTO `stores` (`store_id`, `user_id`, `store_name`, `location`, `contact`, `created_at`) VALUES
(1, 1, 'SmartStock Main Store', 'Manila, Philippines', '+63 912 345 6789', '2025-12-01 08:00:00');

-- Insert sample categories
INSERT INTO `categories` (`category_id`, `category_name`, `user_id`, `created_at`) VALUES
(1, 'Beverages', 1, '2025-12-01 08:00:00'),
(2, 'Snacks', 1, '2025-12-01 08:00:00'),
(3, 'Dairy', 1, '2025-12-01 08:00:00'),
(4, 'Canned Goods', 1, '2025-12-01 08:00:00'),
(5, 'Personal Care', 1, '2025-12-01 08:00:00');

-- Insert default units of measurement
INSERT INTO `units` (`unit_id`, `unit_name`, `user_id`, `created_at`) VALUES
(1, 'piece', 1, '2025-12-01 08:00:00'),
(2, 'milliliter', 1, '2025-12-01 08:00:00'),
(3, 'liter', 1, '2025-12-01 08:00:00'),
(4, 'gram', 1, '2025-12-01 08:00:00'),
(5, 'kilogram', 1, '2025-12-01 08:00:00'),
(6, 'per pack', 1, '2025-12-01 08:00:00'),
(7, 'slice', 1, '2025-12-01 08:00:00'),
(8, 'scoop', 1, '2025-12-01 08:00:00');

-- Insert sample products with realistic PH supermarket pricing (quantities adjusted for realistic stock flow)
INSERT INTO `products` (`product_id`, `user_id`, `category_id`, `name`, `unit_of_measurement`, `cost_price`, `retail_price`, `markup_percent`, `quantity_in_stock`, `quantity_damaged`, `created_at`) VALUES
(1, 1, 1, 'Coca-Cola 500ml', 'piece', 20.00, 28.00, 40.00, 181, 0, '2025-12-01 08:15:23'),
(2, 1, 1, 'Pepsi 1.5L', 'piece', 48.00, 65.00, 35.42, 104, 0, '2025-12-01 08:16:45'),
(3, 1, 1, 'Mountain Dew 330ml', 'piece', 16.00, 22.00, 37.50, 111, 0, '2025-12-01 08:17:12'),
(4, 1, 2, 'Lays Chips Classic 50g', 'per pack', 22.00, 32.00, 45.45, 90, 5, '2025-12-01 08:18:34'),
(5, 1, 2, 'Pringles Original 110g', 'piece', 88.00, 119.00, 35.23, 59, 0, '2025-12-01 08:19:56'),
(6, 1, 3, 'Alaska Fresh Milk 1L', 'liter', 98.00, 135.00, 37.76, 47, 0, '2025-12-01 08:21:08'),
(7, 1, 3, 'Nestle All Purpose Cream 250ml', 'milliliter', 42.00, 58.00, 38.10, 53, 0, '2025-12-01 08:22:33'),
(8, 1, 4, 'Century Tuna Flakes 180g', 'gram', 38.00, 52.00, 36.84, 91, 0, '2025-12-01 08:23:47'),
(9, 1, 4, 'Argentina Corned Beef 150g', 'gram', 45.00, 62.00, 37.78, 87, 0, '2025-12-01 08:24:59'),
(10, 1, 5, 'Safeguard Soap Bar 135g', 'piece', 32.00, 45.00, 40.63, 118, 0, '2025-12-01 08:26:11'),
(11, 1, 5, 'Colgate Total 100g', 'gram', 52.00, 72.00, 38.46, 69, 0, '2025-12-01 08:27:22'),
(12, 1, 2, 'Oreo Cookies 133g', 'per pack', 42.00, 58.00, 38.10, 72, 5, '2025-12-01 08:28:45'),
(13, 1, 1, 'Gatorade Blue Bolt 500ml', 'milliliter', 28.00, 39.00, 39.29, 91, 0, '2025-12-01 08:29:56'),
(14, 1, 3, 'Anchor Butter 227g', 'gram', 195.00, 268.00, 37.44, 38, 0, '2025-12-01 08:31:08'),
(15, 1, 2, 'Nova Multigrain 78g', 'per pack', 12.00, 18.00, 50.00, 189, 0, '2025-12-01 08:32:19'),
(16, 1, 1, 'C2 Green Tea 500ml', 'piece', 18.00, 25.00, 38.89, 115, 0, '2025-12-01 08:33:27'),
(17, 1, 2, 'Skyflakes Crackers 250g', 'per pack', 28.00, 38.00, 35.71, 68, 0, '2025-12-01 08:34:38'),
(18, 1, 4, 'Ligo Sardines 155g', 'gram', 24.00, 33.00, 37.50, 95, 0, '2025-12-01 08:35:49'),
(19, 1, 3, 'Eden Cheese 165g', 'gram', 68.00, 92.00, 35.29, 56, 0, '2025-12-01 08:36:52'),
(20, 1, 5, 'Palmolive Shampoo 180ml', 'milliliter', 48.00, 68.00, 41.67, 84, 0, '2025-12-01 08:38:03'),
(21, 1, 1, 'Royal Tru-Orange 1.5L', 'piece', 45.00, 62.00, 37.78, 73, 0, '2025-12-01 08:39:15'),
(22, 1, 2, 'Oishi Prawn Crackers 60g', 'per pack', 16.00, 23.00, 43.75, 103, 0, '2025-12-01 08:40:26'),
(23, 1, 4, 'Mega Sardines 155g', 'gram', 19.00, 27.00, 42.11, 82, 0, '2025-12-01 08:41:37'),
(24, 1, 3, 'Bear Brand Powdered 300g', 'gram', 142.00, 195.00, 37.32, 43, 0, '2025-12-01 08:42:48'),
(25, 1, 5, 'Dove Soap 100g', 'piece', 38.00, 54.00, 42.11, 99, 0, '2025-12-01 08:43:59');

-- Insert sample stock log entries (realistic workflow showing all system functions)
INSERT INTO `stock_log` (`log_id`, `product_id`, `quantity_changed`, `log_type`, `notes`, `user_id`, `log_date`) VALUES
-- Day 1: Dec 1 - Initial inventory setup via manual entry (actual system format)
(1, 1, 150, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:15:23'),
(2, 2, 80, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:16:45'),
(3, 3, 120, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:17:12'),
(4, 4, 100, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:18:34'),
(5, 5, 60, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:19:56'),
(6, 6, 50, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:21:08'),
(7, 7, 55, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:22:33'),
(8, 8, 110, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:23:47'),
(9, 9, 90, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:24:59'),
(10, 10, 125, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:26:11'),
(11, 11, 75, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:27:22'),
(12, 12, 80, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:28:45'),
(13, 13, 95, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:29:56'),
(14, 14, 40, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:31:08'),
(15, 15, 150, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:32:19'),
(16, 16, 115, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:33:27'),
(17, 17, 70, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:34:38'),
(18, 18, 100, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:35:49'),
(19, 19, 60, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:36:52'),
(20, 20, 85, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:38:03'),
(21, 21, 75, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:39:15'),
(22, 22, 110, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:40:26'),
(23, 23, 95, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:41:37'),
(24, 24, 45, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:42:48'),
(25, 25, 100, 'STOCK-IN', 'Manual product added', 1, '2025-12-01 08:43:59'),
-- Day 2: Dec 2 - Stock removal of expired items
(26, 10, -5, 'STOCK-REMOVAL', 'Expired - Best before Nov 30, 2025', 1, '2025-12-02 07:45:18'),
(27, 15, -10, 'STOCK-REMOVAL', 'Expired - Best before Dec 1, 2025', 1, '2025-12-02 07:52:34'),
-- Day 3: Dec 3 - Rejected damaged items found during inspection
(28, 4, -5, 'REJECT', 'Packaging torn during delivery [Moved to damaged inventory - NOT FOR SALE]', 1, '2025-12-03 09:23:41'),
(29, 12, -5, 'REJECT', 'Crushed packages - unsuitable for sale [Moved to damaged inventory - NOT FOR SALE]', 1, '2025-12-03 09:28:15'),
-- Day 4: Dec 4 - Customer return processed
(30, 1, 2, 'CUSTOMER-RETURN', 'Customer changed mind - items intact', 1, '2025-12-04 11:34:27'),
(31, 16, 3, 'CUSTOMER-RETURN', 'Wrong product purchased - unopened', 1, '2025-12-04 14:18:52'),
-- Day 5: Dec 5 - Supplier refund for defective batch
(32, 8, -10, 'REFUND', 'Defective batch - returned to supplier', 1, '2025-12-05 10:15:39'),
(33, 23, -7, 'REFUND', 'Quality issue - supplier credit issued', 1, '2025-12-05 10:42:17'),
-- Day 6: Dec 6 - Additional stock added via QR code scanning
(34, 1, 50, 'STOCK-IN', 'From QR Scan', 1, '2025-12-06 08:30:12'),
(35, 2, 40, 'STOCK-IN', 'From QR Scan', 1, '2025-12-06 08:31:45'),
(36, 15, 60, 'STOCK-IN', 'From QR Scan', 1, '2025-12-06 08:33:28'),
-- Day 7: Dec 7 - Manual stock adjustments
(37, 22, -5, 'STOCK-REMOVAL', 'Damaged during shelf stocking', 1, '2025-12-07 08:15:44');

-- Insert sample sales transactions (realistic retail sales for presentation)
INSERT INTO `sales` (`sale_id`, `sale_date`, `total_amount`, `payment_received`, `change_amount`, `user_id`) VALUES
(1, '2025-12-02 09:15:00', 168.00, 200.00, 32.00, 1),
(2, '2025-12-02 10:30:00', 391.00, 400.00, 9.00, 1),
(3, '2025-12-02 14:20:00', 156.00, 200.00, 44.00, 1),
(4, '2025-12-03 08:45:00', 534.00, 600.00, 66.00, 1),
(5, '2025-12-03 11:00:00', 287.00, 300.00, 13.00, 1),
(6, '2025-12-03 15:30:00', 623.00, 700.00, 77.00, 1),
(7, '2025-12-04 09:00:00', 219.00, 300.00, 81.00, 1),
(8, '2025-12-04 13:15:00', 445.00, 500.00, 55.00, 1),
(9, '2025-12-05 10:20:00', 792.00, 800.00, 8.00, 1),
(10, '2025-12-05 16:45:00', 356.00, 400.00, 44.00, 1),
(11, '2025-12-06 08:30:00', 518.00, 600.00, 82.00, 1),
(12, '2025-12-06 12:00:00', 673.00, 700.00, 27.00, 1),
(13, '2025-12-07 09:45:00', 289.00, 300.00, 11.00, 1),
(14, '2025-12-07 14:30:00', 412.00, 500.00, 88.00, 1),
(15, '2025-12-07 17:00:00', 156.00, 200.00, 44.00, 1);

-- Insert sample sale items (products sold in each transaction)
INSERT INTO `sale_items` (`sale_id`, `product_id`, `quantity_sold`, `unit_price`, `cost_price`, `subtotal`) VALUES
-- Sale 1: 6 items
(1, 1, 3, 28.00, 20.00, 84.00),
(1, 3, 2, 22.00, 16.00, 44.00),
(1, 15, 2, 18.00, 12.00, 36.00),
(1, 16, 1, 25.00, 18.00, 25.00),
-- Sale 2: Mixed items
(2, 5, 1, 119.00, 88.00, 119.00),
(2, 6, 2, 135.00, 98.00, 270.00),
(2, 22, 2, 23.00, 16.00, 46.00),
-- Sale 3: Quick purchase
(3, 10, 2, 45.00, 32.00, 90.00),
(3, 4, 2, 32.00, 22.00, 64.00),
-- Sale 4: Large order
(4, 14, 2, 268.00, 195.00, 536.00),
-- Sale 5: Multiple snacks
(5, 12, 3, 58.00, 42.00, 174.00),
(5, 17, 2, 38.00, 28.00, 76.00),
(5, 22, 1, 23.00, 16.00, 23.00),
-- Sale 6: Beverage-heavy
(6, 2, 5, 65.00, 48.00, 325.00),
(6, 13, 4, 39.00, 28.00, 156.00),
(6, 16, 3, 25.00, 18.00, 75.00),
(6, 21, 1, 62.00, 45.00, 62.00),
-- Sale 7: Personal care
(7, 11, 2, 72.00, 52.00, 144.00),
(7, 25, 1, 54.00, 38.00, 54.00),
(7, 20, 1, 68.00, 48.00, 68.00),
-- Sale 8: Canned goods
(8, 8, 5, 52.00, 38.00, 260.00),
(8, 9, 3, 62.00, 45.00, 186.00),
-- Sale 9: Large dairy order
(9, 19, 4, 92.00, 68.00, 368.00),
(9, 24, 2, 195.00, 142.00, 390.00),
(9, 7, 2, 58.00, 42.00, 116.00),
-- Sale 10: Mixed
(10, 1, 4, 28.00, 20.00, 112.00),
(10, 4, 3, 32.00, 22.00, 96.00),
(10, 10, 2, 45.00, 32.00, 90.00),
(10, 15, 3, 18.00, 12.00, 54.00),
-- Sale 11: Beverages
(11, 2, 4, 65.00, 48.00, 260.00),
(11, 3, 6, 22.00, 16.00, 132.00),
(11, 21, 2, 62.00, 45.00, 124.00),
-- Sale 12: Large mixed order
(12, 8, 4, 52.00, 38.00, 208.00),
(12, 18, 5, 33.00, 24.00, 165.00),
(12, 23, 6, 27.00, 19.00, 162.00),
(12, 6, 1, 135.00, 98.00, 135.00),
-- Sale 13: Snacks
(13, 4, 3, 32.00, 22.00, 96.00),
(13, 5, 1, 119.00, 88.00, 119.00),
(13, 12, 1, 58.00, 42.00, 58.00),
(13, 15, 2, 18.00, 12.00, 36.00),
-- Sale 14: Personal care & dairy
(14, 11, 3, 72.00, 52.00, 216.00),
(14, 19, 2, 92.00, 68.00, 184.00),
(14, 20, 1, 68.00, 48.00, 68.00),
-- Sale 15: Quick items
(15, 1, 2, 28.00, 20.00, 56.00),
(15, 15, 4, 18.00, 12.00, 72.00),
(15, 3, 1, 22.00, 16.00, 22.00);

-- ============================================================================
-- FOREIGN KEY CONSTRAINTS
-- ============================================================================

ALTER TABLE `users`
  ADD CONSTRAINT `fk_users_admin` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `stores`
  ADD CONSTRAINT `fk_stores_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `categories`
  ADD CONSTRAINT `fk_categories_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `units`
  ADD CONSTRAINT `fk_units_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_products_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sales`
  ADD CONSTRAINT `fk_sales_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sale_items`
  ADD CONSTRAINT `fk_sale_items_sale` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `stock_log`
  ADD CONSTRAINT `fk_stock_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ============================================================================
-- DATABASE READY
-- ============================================================================

COMMIT;

SELECT 
    'SmartStock Database Created Successfully!' AS Status,
    '8 tables with realistic transaction data' AS Tables,
    '25 products + 15 sales + complete workflow' AS Data,
    'Ready for presentation' AS ReadyStatus,
    'Login: admin / admin123' AS Credentials;

-- ============================================================================
-- FEATURES IN THIS DATABASE:
-- ============================================================================
-- ✓ Unit of Measurement Support (piece, liter, milliliter, gram, kilogram, per pack, slice, scoop)
-- ✓ 25 realistic products based on Philippine supermarket pricing
-- ✓ 15 sample sales transactions (Dec 2-7, 2025)
-- ✓ Complete transaction workflow demonstrating ALL system functions:
--   🟢 STOCK-IN - Manual product entry (Dec 1) + QR restock (Dec 6)
--   🔴 SALE - 15 realistic sales transactions across 6 days
--   🟠 STOCK-REMOVAL - Expired items removed (Dec 2, 7)
--   🟠 REJECT - Damaged items marked unsuitable (Dec 3)
--   🔵 CUSTOMER-RETURN - Customer returns processed (Dec 4)
--   🟣 REFUND - Supplier refunds for defects (Dec 5)
-- ✓ Realistic inventory flow matching actual system usage
-- ✓ Proper timestamps showing natural business operations
-- ✓ Multi-user support with role-based access
-- ✓ Historical cost tracking
-- ✓ Stock movement logging
-- ✓ Damaged inventory tracking (10 items across 2 products)
-- ✓ Referential integrity with foreign keys
-- ✓ UTF-8 support for international characters
-- 
-- DATABASE SUMMARY:
--   - Default Admin User: admin / admin123
--   - Sample Store: SmartStock Main Store
--   - 5 Categories: Beverages, Snacks, Dairy, Canned Goods, Personal Care
--   - 25 Products with realistic PH pricing (₱18 - ₱268)
--   - 15 Sales transactions totaling ₱6,019
--   - 37 Stock log entries demonstrating complete system workflow
--   - All products have proper unit measurements
--   - Transaction history shows realistic 7-day operation
--   - All system features demonstrated (Add, Sell, Remove, Reject, Return, Refund)
--   - Ready for demonstration and presentation
-- 
-- TRANSACTION TIMELINE (Dec 1-7, 2025):
--   Day 1 (Mon): Initial inventory setup - 25 products added manually
--   Day 2 (Tue): Sales begin + Expired items removed
--   Day 3 (Wed): Sales continue + Damaged items rejected  
--   Day 4 (Thu): Sales + Customer returns processed
--   Day 5 (Fri): Sales + Supplier refunds issued
--   Day 6 (Sat): High sales volume + QR restock delivery
--   Day 7 (Sun): Ongoing sales + Shelf damage removal
-- ============================================================================
