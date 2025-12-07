package com.inventorysystem.data;

import com.inventorysystem.model.TransactionRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// RecordsRepository
public class RecordsRepository {

    // Get transaction history - ONLY from stock_log table
    public List<TransactionRecord> getTransactionHistory(int userId) throws SQLException {
        List<TransactionRecord> history = new ArrayList<>();

        // Get user's default markup
        UserRepository userRepo = new UserRepository();
        double markupPercent = userRepo.getDefaultMarkup(userId);

        String sql =
            "SELECT sl.log_date AS transaction_date, " +
            "COALESCE(p.name, SUBSTRING_INDEX(sl.notes, 'Product deleted: ', -1), 'Unknown Product') AS product_name, " +
            "COALESCE(p.unit_of_measurement, '-') AS unit, " +
            "sl.log_type AS transaction_type, " +
            "sl.quantity_changed AS quantity, " +
            "COALESCE(p.cost_price, 0) AS unitPrice, " +
            "COALESCE(p.retail_price, 0) AS retailPrice, " +
            "COALESCE(p.cost_price, 0) AS costPrice, " +
            "(COALESCE(p.cost_price, 0) * ABS(sl.quantity_changed)) AS total, " +
            "sl.notes AS notes " +
            "FROM stock_log sl " +
            "LEFT JOIN products p ON sl.product_id = p.product_id AND sl.user_id = p.user_id " +
            "WHERE sl.user_id = ? " +
            "AND sl.log_type IN ('STOCK-IN', 'STOCK-OUT', 'STOCK-REMOVAL', 'REJECT', 'REFUND', 'CUSTOMER-RETURN', 'DISPOSE', 'DELETE') " +
            "ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double retailPrice = rs.getDouble("retailPrice");
                    double costPrice = rs.getDouble("costPrice");
                    
                    if (retailPrice <= 0.0 && costPrice > 0.0) {
                        retailPrice = costPrice * (1 + markupPercent / 100.0);
                        retailPrice = Math.round(retailPrice * 100.0) / 100.0;
                    }
                    
                    history.add(new TransactionRecord(
                        rs.getTimestamp("transaction_date"),
                        rs.getString("product_name"),
                        rs.getString("unit"),
                        rs.getString("transaction_type"),
                        rs.getInt("quantity"), 
                        rs.getDouble("unitPrice"),
                        retailPrice,
                        rs.getDouble("total"),
                        rs.getString("notes")
                    ));
                }
            }
        }
        return history;
    }

    // Get transaction history with date range - ONLY from stock_log table
    public List<TransactionRecord> getTransactionHistoryWithDateRange(int userId, java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        List<TransactionRecord> history = new ArrayList<>();

        UserRepository userRepo = new UserRepository();
        double markupPercent = userRepo.getDefaultMarkup(userId);

        String sql =
            "SELECT sl.log_date AS transaction_date, " +
            "COALESCE(p.name, SUBSTRING_INDEX(sl.notes, 'Product deleted: ', -1), 'Unknown Product') AS product_name, " +
            "COALESCE(p.unit_of_measurement, '-') AS unit, " +
            "sl.log_type AS transaction_type, " +
            "sl.quantity_changed AS quantity, " +
            "COALESCE(p.cost_price, 0) AS unitPrice, " +
            "COALESCE(p.retail_price, 0) AS retailPrice, " +
            "COALESCE(p.cost_price, 0) AS costPrice, " +
            "(COALESCE(p.cost_price, 0) * ABS(sl.quantity_changed)) AS total, " +
            "sl.notes AS notes " +
            "FROM stock_log sl " +
            "LEFT JOIN products p ON sl.product_id = p.product_id AND sl.user_id = p.user_id " +
            "WHERE sl.user_id = ? AND sl.log_date BETWEEN ? AND ? " +
            "AND sl.log_type IN ('STOCK-IN', 'STOCK-OUT', 'STOCK-REMOVAL', 'REJECT', 'REFUND', 'CUSTOMER-RETURN', 'DISPOSE', 'DELETE') " +
            "ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setTimestamp(2, new java.sql.Timestamp(startDate.getTime()));
            pstmt.setTimestamp(3, new java.sql.Timestamp(endDate.getTime()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double retailPrice = rs.getDouble("retailPrice");
                    double costPrice = rs.getDouble("costPrice");
                    
                    // Calculate retail price using markup if not set
                    if (retailPrice <= 0.0 && costPrice > 0.0) {
                        retailPrice = costPrice * (1 + markupPercent / 100.0);
                        retailPrice = Math.round(retailPrice * 100.0) / 100.0;
                    }
                    
                    history.add(new TransactionRecord(
                        rs.getTimestamp("transaction_date"),
                        rs.getString("product_name"),
                        rs.getString("unit"),
                        rs.getString("transaction_type"),
                        rs.getInt("quantity"), 
                        rs.getDouble("unitPrice"),
                        retailPrice,
                        rs.getDouble("total"),
                        rs.getString("notes")
                    ));
                }
            }
        }
        return history;
    }
}