package com.inventorysystem.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {

    private static final String DB_URL;

    static {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/config.properties")) {
            if (input == null) throw new RuntimeException("Cannot find config.properties file");
            properties.load(input);
            DB_URL = properties.getProperty("db.url");
            
            // Initialize Tables automatically on startup
            initializeDatabase();
            
        } catch (IOException | SQLException ex) {
            throw new RuntimeException("Failed to load database configuration", ex);
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        // Enable foreign keys for every connection
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    // CREATE TABLES IF THEY DON'T EXIST
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Enable Foreign Keys (SQLite specific)
            stmt.execute("PRAGMA foreign_keys = ON;");

            // 1. Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "user_role TEXT NOT NULL DEFAULT 'Admin', " +
                    "admin_id INTEGER, " +
                    "default_markup_percent REAL NOT NULL DEFAULT 30.00, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(admin_id) REFERENCES users(user_id))");

            // 2. Categories Table
            stmt.execute("CREATE TABLE IF NOT EXISTS categories (" +
                    "category_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "category_name TEXT NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "UNIQUE(user_id, category_name), " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            // 2.5. Stores Table
            stmt.execute("CREATE TABLE IF NOT EXISTS stores (" +
                    "store_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "store_name TEXT NOT NULL, " +
                    "location TEXT, " +
                    "contact TEXT, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            // 3. Products Table
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "category_id INTEGER NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "unit_of_measurement TEXT NOT NULL, " +
                    "cost_price REAL NOT NULL DEFAULT 0.00, " +
                    "retail_price REAL NOT NULL DEFAULT 0.00, " +
                    "markup_percent REAL, " +
                    "quantity_in_stock INTEGER NOT NULL DEFAULT 0, " +
                    "quantity_damaged INTEGER NOT NULL DEFAULT 0, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(category_id) REFERENCES categories(category_id) ON DELETE CASCADE)");

            // 4. Sales Table
            stmt.execute("CREATE TABLE IF NOT EXISTS sales (" +
                    "sale_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sale_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "total_amount REAL NOT NULL DEFAULT 0.00, " +
                    "user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            // 5. Sale Items Table
            stmt.execute("CREATE TABLE IF NOT EXISTS sale_items (" +
                    "sale_id INTEGER NOT NULL, " +
                    "product_id INTEGER NOT NULL, " +
                    "quantity_sold INTEGER NOT NULL, " +
                    "unit_price REAL NOT NULL, " +
                    "cost_price REAL NOT NULL DEFAULT 0.00, " +
                    "subtotal REAL NOT NULL, " +
                    "PRIMARY KEY (sale_id, product_id), " +
                    "FOREIGN KEY(sale_id) REFERENCES sales(sale_id) ON DELETE CASCADE)");

            // 6. Stock Log Table
            stmt.execute("CREATE TABLE IF NOT EXISTS stock_log (" +
                    "log_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_id INTEGER NOT NULL, " +
                    "quantity_changed INTEGER NOT NULL, " +
                    "log_type TEXT NOT NULL, " +
                    "notes TEXT, " +
                    "user_id INTEGER NOT NULL, " +
                    "log_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");

            // 7. Units Table
            stmt.execute("CREATE TABLE IF NOT EXISTS units (" +
                    "unit_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "unit_name TEXT NOT NULL, " +
                    "user_id INTEGER NOT NULL, " +
                    "UNIQUE(user_id, unit_name), " +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
        }
    }
}