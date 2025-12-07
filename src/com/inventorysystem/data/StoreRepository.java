package com.inventorysystem.data;

import com.inventorysystem.model.Store;
import java.sql.*;

// StoreRepository
public class StoreRepository {

    // Get store
    public Store getStoreByUserId(int userId) throws SQLException {
        String sql = "SELECT store_id, store_name, location, contact FROM stores WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Store(
                        rs.getInt("store_id"),
                        userId,
                        rs.getString("store_name"),
                        rs.getString("location"),
                        rs.getString("contact")
                    );
                }
            }
        }
        return null;
    }

    // Add store
    public boolean addStore(int userId, String storeName, String location, String contact) throws SQLException {
        String sql = "INSERT INTO stores (user_id, store_name, location, contact) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, storeName.trim());
            pstmt.setString(3, location != null ? location.trim() : null);
            pstmt.setString(4, contact != null ? contact.trim() : null);

            return pstmt.executeUpdate() > 0;
        }
    }

    // Update store
    public boolean updateStore(int userId, String newName, String newLocation, String newContact) throws SQLException {
        String sql = "UPDATE stores SET store_name = ?, location = ?, contact = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newName.trim());
            pstmt.setString(2, newLocation != null ? newLocation.trim() : null);
            pstmt.setString(3, newContact != null ? newContact.trim() : null);
            pstmt.setInt(4, userId);

            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete store
    public boolean deleteStore(int storeId) throws SQLException {
        String findUserSql = "SELECT user_id FROM stores WHERE store_id = ?";
        String deleteStoreSql = "DELETE FROM stores WHERE store_id = ?";
        
        Connection conn = null;
        boolean storeDeleted = false;
        int userId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(findUserSql)) {
                pstmt.setInt(1, storeId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                    } else {
                        throw new SQLException("Cannot delete. Store not found with ID: " + storeId);
                    }
                }
            }
            
            ProductRepository productRepo = new ProductRepository(userId);
            productRepo.deleteAllProductsForUser(conn);

            try (PreparedStatement pstmt = conn.prepareStatement(deleteStoreSql)) {
                pstmt.setInt(1, storeId);
                int rowsAffected = pstmt.executeUpdate();
                storeDeleted = (rowsAffected > 0);
            }

            conn.commit();
            return storeDeleted;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { 
                    com.inventorysystem.util.DebugLogger.error("Rollback failed in deleteStore", ex);
                }
            }
            throw new SQLException("Error deleting store and associated data: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { /* Ignored */ }
                try { conn.close(); } catch (SQLException e) { /* Ignored */ }
            }
        }
    }
}