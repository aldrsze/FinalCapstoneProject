-- ============================================================================
-- SmartStock Inventory Management System - SQLite Database Schema
-- ============================================================================
-- Project: Capstone Project - Inventory System with QR Integration
-- Database: smartstock.db (SQLite)
-- Version: 3.0.0 (Converted January 2026)
-- Compatible: SQLite 3.x
-- 
-- COMPLETE SQLite DATABASE SCHEMA
-- This file is for reference only - tables are auto-created by DatabaseConnection.java
-- ============================================================================

PRAGMA foreign_keys = ON;

-- ============================================================================
-- TABLE STRUCTURES
-- ============================================================================

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE COLLATE NOCASE,
    password TEXT NOT NULL,
    user_role TEXT NOT NULL DEFAULT 'Admin',
    admin_id INTEGER,
    default_markup_percent REAL NOT NULL DEFAULT 30.00,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Stores table
CREATE TABLE IF NOT EXISTS stores (
    store_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    store_name TEXT NOT NULL,
    location TEXT,
    contact TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, category_name),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Units of Measurement table
CREATE TABLE IF NOT EXISTS units (
    unit_id INTEGER PRIMARY KEY AUTOINCREMENT,
    unit_name TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, unit_name),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    product_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    unit_of_measurement TEXT NOT NULL,
    cost_price REAL NOT NULL DEFAULT 0.00,
    retail_price REAL NOT NULL DEFAULT 0.00,
    markup_percent REAL,
    quantity_in_stock INTEGER NOT NULL DEFAULT 0,
    quantity_damaged INTEGER NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, user_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY(category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- Sales table
CREATE TABLE IF NOT EXISTS sales (
    sale_id INTEGER PRIMARY KEY AUTOINCREMENT,
    sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount REAL NOT NULL DEFAULT 0.00,
    payment_received REAL,
    change_amount REAL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Sale Items table
CREATE TABLE IF NOT EXISTS sale_items (
    sale_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity_sold INTEGER NOT NULL,
    unit_price REAL NOT NULL,
    cost_price REAL NOT NULL DEFAULT 0.00,
    subtotal REAL NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY(sale_id) REFERENCES sales(sale_id) ON DELETE CASCADE
);

-- Stock Log table
CREATE TABLE IF NOT EXISTS stock_log (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER NOT NULL,
    quantity_changed INTEGER NOT NULL,
    log_type TEXT NOT NULL,
    notes TEXT,
    user_id INTEGER NOT NULL,
    log_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================================================
-- INDEXES FOR PERFORMANCE
-- ============================================================================

CREATE INDEX IF NOT EXISTS idx_products_user ON products(user_id);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_sales_user ON sales(user_id);
CREATE INDEX IF NOT EXISTS idx_sales_date ON sales(sale_date);
CREATE INDEX IF NOT EXISTS idx_stock_log_product ON stock_log(product_id);
CREATE INDEX IF NOT EXISTS idx_stock_log_user ON stock_log(user_id);
CREATE INDEX IF NOT EXISTS idx_stock_log_date ON stock_log(log_date);
CREATE INDEX IF NOT EXISTS idx_categories_user ON categories(user_id);
CREATE INDEX IF NOT EXISTS idx_units_user ON units(user_id);

-- ============================================================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================================================

-- Default admin user (password: admin123)
-- INSERT OR IGNORE INTO users (username, password, user_role, default_markup_percent) 
-- VALUES ('admin', 'admin123', 'Admin', 30.00);

-- Default categories for user_id = 1
-- INSERT OR IGNORE INTO categories (category_name, user_id) VALUES 
-- ('Beverages', 1),
-- ('Snacks', 1),
-- ('Dairy', 1),
-- ('Canned Goods', 1),
-- ('Frozen Foods', 1);

-- Default units for user_id = 1
-- INSERT OR IGNORE INTO units (unit_name, user_id) VALUES 
-- ('piece', 1),
-- ('milliliter', 1),
-- ('liter', 1),
-- ('gram', 1),
-- ('kilogram', 1),
-- ('per pack', 1),
-- ('slice', 1),
-- ('scoop', 1);

-- ============================================================================
-- NOTES
-- ============================================================================
-- 1. Tables are automatically created by DatabaseConnection.java on first run
-- 2. Foreign keys are enforced (PRAGMA foreign_keys = ON)
-- 3. SQLite uses INTEGER PRIMARY KEY for auto-increment (not AUTO_INCREMENT)
-- 4. TEXT type is used instead of VARCHAR
-- 5. REAL type is used instead of DECIMAL
-- 6. DATETIME uses CURRENT_TIMESTAMP for default values
-- 7. Composite primary keys are supported in products table
-- 8. Case-insensitive username comparison using COLLATE NOCASE
-- ============================================================================
