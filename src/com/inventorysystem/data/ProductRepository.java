package com.inventorysystem.data;

import com.inventorysystem.model.Category;
import com.inventorysystem.model.Product;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Product repo
public class ProductRepository {

    private final int userId;

    public ProductRepository(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }
        this.userId = userId;

        try {
            ensureQuantityDamagedColumn();
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error ensuring quantity_damaged column exists", e);
        }
    }

    // Add quantity_damaged column if missing
    private void ensureQuantityDamagedColumn() throws SQLException {
        String checkColumnSql = "SHOW COLUMNS FROM products LIKE 'quantity_damaged'";
        String addColumnSql = "ALTER TABLE products ADD COLUMN quantity_damaged INT(11) NOT NULL DEFAULT 0 AFTER quantity_in_stock";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean columnExists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkColumnSql);
                 ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    columnExists = true;
                }
            }
            
            if (!columnExists) {
                try (PreparedStatement addColStmt = conn.prepareStatement(addColumnSql)) {
                    addColStmt.executeUpdate();
                }
            }
        }
    }

    // Get all products for user
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        
        String sql = "SELECT p.product_id, p.name, c.category_name, p.unit_of_measurement, p.cost_price, p.retail_price, p.quantity_in_stock, " +
                     "COALESCE(p.quantity_damaged, 0) as quantity_damaged " +
                     "FROM products p " +
                     "JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.user_id = ? " +
                     "ORDER BY p.product_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double costPrice = rs.getDouble("cost_price");
                    int stock = rs.getInt("quantity_in_stock");
                    double retailPrice = rs.getObject("retail_price") == null ? 0.0 : rs.getDouble("retail_price");
                    double totalCost = costPrice * stock;

                    products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category_name"),
                        rs.getString("unit_of_measurement"),
                        costPrice,
                        retailPrice,
                        stock,
                        totalCost
                    ));
                }
            }
        }
        return products;
    }

    // Get retail price
    public double getProductRetailPrice(String productId) throws SQLException, NumberFormatException {
        int prodId = Integer.parseInt(productId);

        String sql = "SELECT retail_price FROM products WHERE product_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, prodId);
            pstmt.setInt(2, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double retailPrice = rs.getDouble("retail_price");
                    return rs.wasNull() ? 0.0 : retailPrice;
                } else {
                    return 0.0;
                }
            }
        }
    }

    // Get next product ID
    public int getNextProductId() throws SQLException {
        String sql = "SELECT MAX(product_id) FROM products WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt(1);
                    return maxId + 1;
                } else {
                    return 1;
                }
            }
        }
    }

    // Find product by name and category
    private int findProductByNameAndCategory(String name, int categoryId) throws SQLException {
        String sql = "SELECT product_id FROM products WHERE name = ? AND category_id = ? AND user_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, categoryId);
            pstmt.setInt(3, this.userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("product_id");
                }
            }
        }
        return -1; // Not found
    }

    // Update product markup
    public void updateProductMarkup(int productId, Double markupPercent) throws SQLException {
        String sql = "UPDATE products SET markup_percent = ? WHERE product_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (markupPercent != null) {
                pstmt.setDouble(1, markupPercent);
            } else {
                pstmt.setNull(1, java.sql.Types.DECIMAL);
            }
            pstmt.setInt(2, productId);
            pstmt.setInt(3, this.userId);
            
            pstmt.executeUpdate();
        }
    }

    // Clear retail price
    public void clearRetailPrice(int productId) throws SQLException {
        String sql = "UPDATE products SET retail_price = 0 WHERE product_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            pstmt.setInt(2, this.userId);
            
            pstmt.executeUpdate();
        }
    }

    // Get product markup
    public Double getProductMarkup(int productId) throws SQLException {
        String sql = "SELECT markup_percent FROM products WHERE product_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            pstmt.setInt(2, this.userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Object markup = rs.getObject("markup_percent");
                    return markup != null ? rs.getDouble("markup_percent") : null;
                }
            }
        }
        return null;
    }

    // Add or update product from QR code
    public int upsertProductFromQR(JSONObject data) throws SQLException, JSONException {
        if (data == null) {
            throw new IllegalArgumentException("JSON data cannot be null.");
        }

        String name = data.getString("name");
        int categoryId = data.getInt("category_id");
        double costPrice = data.getDouble("cost_price");
        int stockToAdd = data.getInt("stock");
        String unit = data.getString("unit");
        
        // Determine product ID: check for existing product with same name + category
        int productId;
        if (data.has("id")) {
            productId = data.getInt("id"); // Old QR codes with explicit ID
        } else {
            // New QR codes without ID - check if product already exists
            productId = findProductByNameAndCategory(name, categoryId);
            if (productId == -1) {
                // Product doesn't exist - auto-generate new ID
                productId = getNextProductId();
            }
            // If productId > 0, it means product exists - will add to existing
        }
        
        String upsertSql = "INSERT INTO products (product_id, name, unit_of_measurement, cost_price, quantity_in_stock, category_id, user_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE " +
                           "name = VALUES(name), unit_of_measurement = VALUES(unit_of_measurement), cost_price = VALUES(cost_price), " +
                           "quantity_in_stock = quantity_in_stock + VALUES(quantity_in_stock), category_id = VALUES(category_id)";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'STOCK-IN', 'From QR Scan', ?)";

        if (costPrice < 0 || stockToAdd < 0) {
             throw new IllegalArgumentException("Cost price and stock quantity from QR cannot be negative.");
        }

        Connection conn = null;
        int affectedRows = 0;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement upsertPstmt = conn.prepareStatement(upsertSql)) {
                upsertPstmt.setInt(1, productId);
                upsertPstmt.setString(2, name);
                upsertPstmt.setString(3, unit);
                upsertPstmt.setDouble(4, costPrice);
                upsertPstmt.setInt(5, stockToAdd);
                upsertPstmt.setInt(6, categoryId);
                upsertPstmt.setInt(7, this.userId);
                affectedRows = upsertPstmt.executeUpdate();
            }

            if (affectedRows > 0) {
                try (PreparedStatement logPstmt = conn.prepareStatement(logSql)) {
                    logPstmt.setInt(1, productId);
                    logPstmt.setInt(2, +stockToAdd);
                    logPstmt.setInt(3, this.userId);
                    logPstmt.executeUpdate();
                }
            } else {
                throw new SQLException("Upsert operation affected 0 rows for product ID: " + productId);
            }

            conn.commit();
            return productId; // Return the actual product ID used (auto-generated or from QR) 

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { 
                    com.inventorysystem.util.DebugLogger.error("Rollback failed in QR database transaction", ex);
                }
            }
            throw new SQLException("Error during QR database transaction: " + e.getMessage(), e);
        } finally {
             if (conn != null) {
                 try { conn.setAutoCommit(true); conn.close(); } 
                 catch (SQLException e) { /* Ignored */ }
             }
        }
    }

    // Add or update product manually
    public int manualUpsertProduct(int id, String name, int categoryId, String unit, double costPrice, 
                                      Double retailPrice, Double markupPercent, int newStock) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (newStock < 0 || costPrice < 0) {
             throw new IllegalArgumentException("Cost price and stock quantity cannot be negative.");
        }

        boolean isNewProduct = (id == 0);
        
        String selectSql = "SELECT quantity_in_stock, retail_price FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
        String getNextIdSql = "SELECT COALESCE(MAX(product_id), 0) + 1 as next_id FROM products WHERE user_id = ?";
        String insertSql = "INSERT INTO products (product_id, name, unit_of_measurement, cost_price, retail_price, markup_percent, quantity_in_stock, category_id, user_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE products SET name = ?, unit_of_measurement = ?, cost_price = ?, retail_price = ?, markup_percent = ?, " +
                           "quantity_in_stock = ?, category_id = ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        int resultProductId = 0;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            int oldStock = 0;
            double existingRetailPrice = 0.0;
            boolean exists = false;
            
            if (!isNewProduct) {
                try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                    selectPstmt.setInt(1, id);
                    selectPstmt.setInt(2, this.userId);
                    try (ResultSet rs = selectPstmt.executeQuery()) {
                        if (rs.next()) {
                            oldStock = rs.getInt("quantity_in_stock");
                            existingRetailPrice = rs.getDouble("retail_price");
                            exists = true;
                        }
                    }
                }
            }

            // If retailPrice is null, preserve existing value or use 0
            double finalRetailPrice = 0.0;
            if (retailPrice != null) {
                finalRetailPrice = retailPrice;
            } else if (exists) {
                finalRetailPrice = existingRetailPrice; // Preserve existing
            }

            int rowsAffected = 0;
            
            if (isNewProduct) {
                // Get next available product_id for this user
                int nextProductId = 1;
                try (PreparedStatement getIdPstmt = conn.prepareStatement(getNextIdSql)) {
                    getIdPstmt.setInt(1, this.userId);
                    try (ResultSet rs = getIdPstmt.executeQuery()) {
                        if (rs.next()) {
                            nextProductId = rs.getInt("next_id");
                        }
                    }
                }
                
                resultProductId = nextProductId;
                
                // Insert new product with manual ID
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setInt(1, nextProductId);
                    insertPstmt.setString(2, name.trim());
                    insertPstmt.setString(3, unit);
                    insertPstmt.setDouble(4, costPrice);
                    insertPstmt.setDouble(5, finalRetailPrice);
                    
                    if (markupPercent != null) {
                        insertPstmt.setDouble(6, markupPercent);
                    } else {
                        insertPstmt.setNull(6, java.sql.Types.DECIMAL);
                    }
                    
                    insertPstmt.setInt(7, newStock);
                    insertPstmt.setInt(8, categoryId);
                    insertPstmt.setInt(9, this.userId);
                    rowsAffected = insertPstmt.executeUpdate();
                }
            } else {
                // Update existing product
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                    updatePstmt.setString(1, name.trim());
                    updatePstmt.setString(2, unit);
                    updatePstmt.setDouble(3, costPrice);
                    updatePstmt.setDouble(4, finalRetailPrice);
                    
                    if (markupPercent != null) {
                        updatePstmt.setDouble(5, markupPercent);
                    } else {
                        updatePstmt.setNull(5, java.sql.Types.DECIMAL);
                    }
                    
                    updatePstmt.setInt(6, newStock);
                    updatePstmt.setInt(7, categoryId);
                    updatePstmt.setInt(8, id);
                    updatePstmt.setInt(9, this.userId);
                    rowsAffected = updatePstmt.executeUpdate();
                }
                resultProductId = id;
            }

            if (rowsAffected > 0) {
                int quantityChange = newStock - oldStock;
                String logType = null;
                String notes = null;

                if (isNewProduct) {
                    logType = "STOCK-IN";
                    notes = "Manual product added";
                    quantityChange = newStock;
                } else if (quantityChange > 0) {
                    logType = "STOCK-IN";
                    notes = "Manual stock increase";
                } else if (quantityChange < 0) {
                    logType = "STOCK-REMOVAL";
                    notes = "Manual stock decrease";
                }

                if (logType != null) {
                    try (PreparedStatement logPstmt = conn.prepareStatement(logSql)) {
                        logPstmt.setInt(1, resultProductId);
                        logPstmt.setInt(2, quantityChange);
                        logPstmt.setString(3, logType);
                        logPstmt.setString(4, notes);
                        logPstmt.setInt(5, this.userId);
                        logPstmt.executeUpdate();
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { 
                    com.inventorysystem.util.DebugLogger.error("Rollback failed", ex);
                }
            }
            throw e;
        } finally {
             if (conn != null) {
                 try { conn.setAutoCommit(true); conn.close(); } 
                 catch (SQLException e) { /* Ignored */ }
             }
        }
        return resultProductId;
    }

    /**
     * Sells a product: Records financial sale AND reduces stock.
     * Now inserts into 'sales' and 'sale_items' tables so Dashboard works.
     */
    public void sellProduct(String productId, int quantityToSell) throws SQLException, NumberFormatException {
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Positive quantity required.");
        }

        int prodId = Integer.parseInt(productId);

        // 1. Fetch product details to calculate price and profit
        String productSql = "SELECT cost_price, retail_price, markup_percent, quantity_in_stock " +
                            "FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
        
        // 2. Fetch User's Default Markup (fallback if product has no specific price)
        String userMarkupSql = "SELECT default_markup_percent FROM users WHERE user_id = ?";

        // SQLs for inserting records
        String insertSaleSql = "INSERT INTO sales (user_id, sale_date, total_amount) VALUES (?, NOW(), ?)";
        String insertItemSql = "INSERT INTO sale_items (sale_id, product_id, quantity_sold, unit_price, cost_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String updateProductSql = "UPDATE products SET quantity_in_stock = ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'SALE', ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // --- Step A: Get Product Data ---
            double costPrice = 0.0;
            double retailPrice = 0.0;
            Double markupPercent = null;
            int currentStock = 0;
            
            try (PreparedStatement stmt = conn.prepareStatement(productSql)) {
                stmt.setInt(1, prodId);
                stmt.setInt(2, this.userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Product not found.");
                    costPrice = rs.getDouble("cost_price");
                    retailPrice = rs.getDouble("retail_price");
                    markupPercent = (Double) rs.getObject("markup_percent");
                    currentStock = rs.getInt("quantity_in_stock");
                }
            }

            if (currentStock < quantityToSell) {
                throw new SQLException("Insufficient stock. Available: " + currentStock);
            }

            // --- Step B: Calculate Final Selling Price ---
            double finalUnitPrice = retailPrice;
            
            // If no fixed retail price, calculate using markup
            if (finalUnitPrice <= 0.0) {
                double effectiveMarkup = 0.0;
                
                if (markupPercent != null) {
                    effectiveMarkup = markupPercent;
                } else {
                    // Fetch default markup
                    try (PreparedStatement stmt = conn.prepareStatement(userMarkupSql)) {
                        stmt.setInt(1, this.userId);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) effectiveMarkup = rs.getDouble("default_markup_percent");
                        }
                    }
                }
                finalUnitPrice = costPrice * (1 + effectiveMarkup / 100.0);
            }
            
            // Round to 2 decimals
            finalUnitPrice = Math.round(finalUnitPrice * 100.0) / 100.0;
            double totalSaleAmount = finalUnitPrice * quantityToSell;

            // --- Step C: Insert into SALES table ---
            int saleId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertSaleSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, this.userId);
                stmt.setDouble(2, totalSaleAmount);
                stmt.executeUpdate();
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        saleId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating sale failed, no ID obtained.");
                    }
                }
            }

            // --- Step D: Insert into SALE_ITEMS table ---
            try (PreparedStatement stmt = conn.prepareStatement(insertItemSql)) {
                stmt.setInt(1, saleId);
                stmt.setInt(2, prodId);
                stmt.setInt(3, quantityToSell);
                stmt.setDouble(4, finalUnitPrice);
                stmt.setDouble(5, costPrice);
                stmt.setDouble(6, totalSaleAmount);
                stmt.executeUpdate();
            }

            // --- Step E: Update Stock ---
            try (PreparedStatement stmt = conn.prepareStatement(updateProductSql)) {
                stmt.setInt(1, currentStock - quantityToSell);
                stmt.setInt(2, prodId);
                stmt.setInt(3, this.userId);
                stmt.executeUpdate();
            }

            // --- Step F: Log as SALE (for Stock Panel) ---
            try (PreparedStatement stmt = conn.prepareStatement(logSql)) {
                stmt.setInt(1, prodId);
                stmt.setInt(2, -quantityToSell); // Negative for removal
                stmt.setString(3, "Sold " + quantityToSell + " @ " + String.format("₱%,.2f", finalUnitPrice));
                stmt.setInt(4, this.userId);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { com.inventorysystem.util.DebugLogger.error("Rollback failed", ex); }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* Ignored */ }
            }
        }
    }

    // Remove stock
    public void removeStock(int productId, int quantityToRemove, String reason) throws SQLException {
        if (quantityToRemove <= 0) {
             throw new IllegalArgumentException("Quantity to remove must be positive.");
        }
        
        String checkStockSql = "SELECT quantity_in_stock FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
        String updateSql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'STOCK-REMOVAL', ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check current stock
            int currentStock;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
                checkStmt.setInt(1, productId);
                checkStmt.setInt(2, this.userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Product ID " + productId + " not found for this user.");
                    }
                    currentStock = rs.getInt("quantity_in_stock");
                }
            }

            // 2. Validate stock availability
            if (currentStock < quantityToRemove) {
                throw new SQLException("Not enough stock to remove. Available: " + currentStock + ", Requested: " + quantityToRemove);
            }

            // 3. Update product stock
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantityToRemove);
                updateStmt.setInt(2, productId);
                updateStmt.setInt(3, this.userId);
                updateStmt.executeUpdate();
            }

            // 4. Log the stock removal
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, productId);
                logStmt.setInt(2, -quantityToRemove);
                logStmt.setString(3, reason != null && !reason.isEmpty() ? reason : "Manual stock removal");
                logStmt.setInt(4, this.userId); // Set user ID
                logStmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try (Connection c = conn) { c.rollback(); } catch (SQLException ex) { com.inventorysystem.util.DebugLogger.error("Rollback failed", ex); }
            }
            throw e;
        } finally {
             if (conn != null) {
                 try { conn.setAutoCommit(true); } catch (SQLException e) { /* Ignored */ }
                 try { conn.close(); } catch (SQLException e) { /* Ignored */ }
             }
        }
    }

    // Reject damaged product
    public void rejectProduct(int productId, int quantityToReject, String reason) throws SQLException {
        if (quantityToReject <= 0) {
            throw new IllegalArgumentException("Quantity to reject must be positive.");
        }
        
        String findProductSql = "SELECT quantity_in_stock FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
        String updateSql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ?, quantity_damaged = quantity_damaged + ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'REJECT', ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check current stock
            int currentStock;
            try (PreparedStatement findPstmt = conn.prepareStatement(findProductSql)) {
                findPstmt.setInt(1, productId);
                findPstmt.setInt(2, this.userId);
                try (ResultSet rs = findPstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Product ID " + productId + " not found for this user.");
                    }
                    currentStock = rs.getInt("quantity_in_stock");
                }
            }

            // Validate stock availability
            if (currentStock < quantityToReject) {
                throw new SQLException("Not enough stock to reject. Available: " + currentStock + ", Requested: " + quantityToReject);
            }

            // Update: reduce sellable stock and increase damaged quantity
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantityToReject);
                updateStmt.setInt(2, quantityToReject);
                updateStmt.setInt(3, productId);
                updateStmt.setInt(4, this.userId);
                updateStmt.executeUpdate();
            }

            // Log rejection
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, productId);
                logStmt.setInt(2, -quantityToReject);
                logStmt.setString(3, reason + " [Moved to damaged inventory - NOT FOR SALE]");
                logStmt.setInt(4, this.userId);
                logStmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try (Connection c = conn) { c.rollback(); } catch (SQLException ex) { com.inventorysystem.util.DebugLogger.error("Rollback failed", ex); }
            }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { /* Ignored */ }
                try { conn.close(); } catch (SQLException e) { /* Ignored */ }
            }
        }
    }

    // Process customer return
    public void customerReturn(int productId, int quantityToReturn, String reason) throws SQLException {
        if (quantityToReturn <= 0) throw new IllegalArgumentException("Quantity to return must be positive.");
        
        // 1. Fetch price details to calculate refund amount
        String productSql = "SELECT cost_price, retail_price, markup_percent FROM products WHERE product_id = ? AND user_id = ?";
        String userMarkupSql = "SELECT default_markup_percent FROM users WHERE user_id = ?";
        
        // 2. SQLs for updates
        String updateSql = "UPDATE products SET quantity_in_stock = quantity_in_stock + ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'CUSTOMER-RETURN', ?, ?)";
        // FIX: Insert negative records to reverse the sale in Dashboard
        String insertSaleSql = "INSERT INTO sales (user_id, sale_date, total_amount) VALUES (?, NOW(), ?)";
        String insertItemSql = "INSERT INTO sale_items (sale_id, product_id, quantity_sold, unit_price, cost_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // --- Step A: Calculate Refund Price ---
            double costPrice = 0.0;
            double retailPrice = 0.0;
            Double markupPercent = null;
            
            try (PreparedStatement stmt = conn.prepareStatement(productSql)) {
                stmt.setInt(1, productId);
                stmt.setInt(2, this.userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Product ID " + productId + " not found.");
                    costPrice = rs.getDouble("cost_price");
                    retailPrice = rs.getDouble("retail_price");
                    markupPercent = (Double) rs.getObject("markup_percent");
                }
            }

            double finalRefundPrice = retailPrice;
            if (finalRefundPrice <= 0.0) {
                double effectiveMarkup = 0.0;
                if (markupPercent != null) {
                    effectiveMarkup = markupPercent;
                } else {
                    try (PreparedStatement stmt = conn.prepareStatement(userMarkupSql)) {
                        stmt.setInt(1, this.userId);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) effectiveMarkup = rs.getDouble("default_markup_percent");
                        }
                    }
                }
                finalRefundPrice = costPrice * (1 + effectiveMarkup / 100.0);
            }
            finalRefundPrice = Math.round(finalRefundPrice * 100.0) / 100.0;
            double totalRefundAmount = finalRefundPrice * quantityToReturn;

            // --- Step B: Update Stock ---
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantityToReturn);
                updateStmt.setInt(2, productId);
                updateStmt.setInt(3, this.userId);
                updateStmt.executeUpdate();
            }

            // --- Step C: Log Transaction ---
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, productId);
                logStmt.setInt(2, +quantityToReturn);
                logStmt.setString(3, reason + " [Added back to stock]");
                logStmt.setInt(4, this.userId);
                logStmt.executeUpdate();
            }

            // --- Step D: Record Financial Reversal (Negative Sale) ---
            int saleId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertSaleSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, this.userId);
                stmt.setDouble(2, -totalRefundAmount); // Negative Amount
                stmt.executeUpdate();
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) saleId = generatedKeys.getInt(1);
                }
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(insertItemSql)) {
                stmt.setInt(1, saleId);
                stmt.setInt(2, productId);
                stmt.setInt(3, -quantityToReturn); // Negative Quantity
                stmt.setDouble(4, finalRefundPrice);
                stmt.setDouble(5, costPrice);
                stmt.setDouble(6, -totalRefundAmount); // Negative Subtotal
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { }
            }
        }
    }

    // Refund to supplier
    public void refundProduct(int productId, int quantityToRefund, String reason) throws SQLException {
        if (quantityToRefund <= 0) {
            throw new IllegalArgumentException("Quantity to refund must be positive.");
        }
        
        String findProductSql = "SELECT quantity_in_stock, cost_price FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
        String updateSql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ? WHERE product_id = ? AND user_id = ?";
        String logSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'REFUND', ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Check stock
            int currentStock;
            double costPrice;
            try (PreparedStatement findPstmt = conn.prepareStatement(findProductSql)) {
                findPstmt.setInt(1, productId);
                findPstmt.setInt(2, this.userId);
                try (ResultSet rs = findPstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Product ID " + productId + " not found for this user.");
                    }
                    currentStock = rs.getInt("quantity_in_stock");
                    costPrice = rs.getDouble("cost_price");
                }
            }

            // Validate stock
            if (currentStock < quantityToRefund) {
                throw new SQLException("Not enough stock to refund. Available: " + currentStock + ", Requested: " + quantityToRefund);
            }

            // Update stock
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantityToRefund);
                updateStmt.setInt(2, productId);
                updateStmt.setInt(3, this.userId);
                updateStmt.executeUpdate();
            }

            // Log refund
            String refundNote = reason + " [Refund Amount: ₱" + String.format("%,.2f", costPrice * quantityToRefund) + "]";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, productId);
                logStmt.setInt(2, -quantityToRefund);
                logStmt.setString(3, refundNote);
                logStmt.setInt(4, this.userId);
                logStmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try (Connection c = conn) { c.rollback(); } catch (SQLException ex) { com.inventorysystem.util.DebugLogger.error("Rollback failed", ex); }
            }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { /* Ignored */ }
                try { conn.close(); } catch (SQLException e) { /* Ignored */ }
            }
        }
    }

    /**
     * Deletes a product and its associated stock logs.
     * No longer needs to handle sales/sale_items since those aren't created.
     */
    public boolean deleteProduct(int productId) throws SQLException {
        // Get product details for logging
        String getProductSql = "SELECT name, quantity_in_stock FROM products WHERE product_id = ? AND user_id = ?";
        
        // Log the deletion
        String logDeletionSql = "INSERT INTO stock_log (product_id, quantity_changed, log_type, notes, user_id) VALUES (?, ?, 'DELETE', ?, ?)";
        
        // Delete associated stock logs (except the deletion log we just created)
        String deleteStockLogSql = "DELETE FROM stock_log WHERE product_id = ? AND user_id = ? AND log_type != 'DELETE'";
        
        // Delete the product itself
        String deleteProductSql = "DELETE FROM products WHERE product_id = ? AND user_id = ?";

        Connection conn = null;
        int productRowsAffected = 0;
        String productName = null;
        int currentStock = 0;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get product details for logging
            try (PreparedStatement pstmt = conn.prepareStatement(getProductSql)) {
                pstmt.setInt(1, productId);
                pstmt.setInt(2, this.userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        productName = rs.getString("name");
                        currentStock = rs.getInt("quantity_in_stock");
                    } else {
                        conn.rollback();
                        return false; // Product not found
                    }
                }
            }
            
            // Log the deletion with product name in notes
            try (PreparedStatement pstmt = conn.prepareStatement(logDeletionSql)) {
                pstmt.setInt(1, productId);
                pstmt.setInt(2, -currentStock); // Negative to show stock removed
                pstmt.setString(3, "Product deleted: " + productName);
                pstmt.setInt(4, this.userId);
                pstmt.executeUpdate();
            }

            // Delete associated stock logs (except deletion log)
            try (PreparedStatement pstmt = conn.prepareStatement(deleteStockLogSql)) {
                pstmt.setInt(1, productId);
                pstmt.setInt(2, this.userId);
                pstmt.executeUpdate();
            }

            // Delete the product itself
            try (PreparedStatement pstmt = conn.prepareStatement(deleteProductSql)) {
                pstmt.setInt(1, productId);
                pstmt.setInt(2, this.userId);
                productRowsAffected = pstmt.executeUpdate();
            }

            conn.commit();
            return productRowsAffected > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try { 
                    conn.rollback(); 
                } catch (SQLException ex) { 
                    com.inventorysystem.util.DebugLogger.error("Rollback failed", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                } catch (SQLException e) {
                    // Ignored
                }
                try { 
                    conn.close(); 
                } catch (SQLException e) {
                    // Ignored
                }
            }
        }
    }


    // Get all categories
    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM categories WHERE user_id = ? ORDER BY category_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, this.userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                    ));
                }
            }
        }
        return categories;
    }

    // Add new category
    public void addNewCategory(String categoryName) throws SQLException {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }

        String sql = "INSERT INTO categories (category_name, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName.trim());
            pstmt.setInt(2, this.userId);
            pstmt.executeUpdate();
        }
    }

    // Delete all products
    public void deleteAllProductsForUser(Connection conn) throws SQLException {
        List<Integer> productIds = new ArrayList<>();
        String selectSql = "SELECT product_id FROM products WHERE user_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setInt(1, this.userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) productIds.add(rs.getInt("product_id"));
            }
        }

        if (productIds.isEmpty()) return;

        StringBuilder inClause = new StringBuilder("(");
        for (int i = 0; i < productIds.size(); i++) {
            inClause.append("?");
            if (i < productIds.size() - 1) inClause.append(",");
        }
        inClause.append(")");

        String delItems = "DELETE FROM sale_items WHERE product_id IN " + inClause;
        String delLogs = "DELETE FROM stock_log WHERE product_id IN " + inClause;
        String delProds = "DELETE FROM products WHERE product_id IN " + inClause + " AND user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(delItems)) {
            for (int i = 0; i < productIds.size(); i++) pstmt.setInt(i + 1, productIds.get(i));
            pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(delLogs)) {
            for (int i = 0; i < productIds.size(); i++) pstmt.setInt(i + 1, productIds.get(i));
            pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt = conn.prepareStatement(delProds)) {
            for (int i = 0; i < productIds.size(); i++) pstmt.setInt(i + 1, productIds.get(i));
            pstmt.setInt(productIds.size() + 1, this.userId);
            pstmt.executeUpdate();
        }
    }

    // Process return
    public void processReturn(int productId, int quantity, String reason, String notes) throws SQLException {
        
        if ("CUSTOMER-RETURN".equalsIgnoreCase(reason)) {
            customerReturn(productId, quantity, notes);
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String logType;
            int stockChange = 0;
            boolean updateDamaged = false;

            switch (reason.toUpperCase()) {
                // Customer return above
                case "REJECT":
                    logType = "REJECT";
                    stockChange = -quantity;
                    updateDamaged = true;
                    break;
                case "REFUND":
                    logType = "REFUND";
                    stockChange = -quantity;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid return reason: " + reason);
            }
            
            // Check stock for reduce
            if (stockChange < 0 || updateDamaged) {
                String checkStockSql = "SELECT quantity_in_stock FROM products WHERE product_id = ? AND user_id = ? FOR UPDATE";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
                    checkStmt.setInt(1, productId);
                    checkStmt.setInt(2, this.userId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Product ID " + productId + " not found.");
                        int currentStock = rs.getInt("quantity_in_stock");
                        if (currentStock < quantity) throw new SQLException("Not enough stock available.");
                    }
                }
            }

            // Update stock
            if (stockChange != 0) {
                String updateStockSql = "UPDATE products SET quantity_in_stock = quantity_in_stock + ? WHERE product_id = ? AND user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                    pstmt.setInt(1, stockChange);
                    pstmt.setInt(2, productId);
                    pstmt.setInt(3, this.userId);
                    pstmt.executeUpdate();
                }
            }

            // Update damaged
            if (updateDamaged) {
                String updateDamagedSql = "UPDATE products SET quantity_damaged = quantity_damaged + ? WHERE product_id = ? AND user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateDamagedSql)) {
                    pstmt.setInt(1, quantity);
                    pstmt.setInt(2, productId);
                    pstmt.setInt(3, this.userId);
                    pstmt.executeUpdate();
                }
            }

            // Log
            String finalNotes = notes;
            if (logType.equals("REJECT")) finalNotes = (notes != null ? notes + " - " : "") + "[Moved to damaged]";
            else if (logType.equals("REFUND")) finalNotes = (notes != null ? notes + " - " : "") + "[Returned to supplier]";
            
            String logSql = "INSERT INTO stock_log (product_id, user_id, quantity_changed, log_type, notes, log_date) VALUES (?, ?, ?, ?, ?, NOW())";
            try (PreparedStatement pstmt = conn.prepareStatement(logSql)) {
                pstmt.setInt(1, productId);
                pstmt.setInt(2, this.userId);
                pstmt.setInt(3, stockChange);
                pstmt.setString(4, logType);
                pstmt.setString(5, finalNotes);
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { }
            throw e;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { }
            }
        }
    }

    // Get category name by ID
    public String getCategoryNameById(int categoryId) throws SQLException {
        String sql = "SELECT category_name FROM categories WHERE category_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, this.userId); // Crucial: Must match the current user
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("category_name");
                }
            }
        }
        return null; // Not found for this user
    }

    // Get product category name
    public String getProductCategoryName(int productId) throws SQLException {
        String sql = "SELECT c.category_name FROM products p " +
                     "JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.product_id = ? AND p.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, this.userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("category_name");
                }
            }
        }
        return "Unknown";
    }
}


