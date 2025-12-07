package com.inventorysystem.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitsRepository {
    
    private final int userId;
    
    public UnitsRepository(int userId) {
        this.userId = userId;
    }
    
    /**
     * Get all units for the current user
     */
    public List<String> getAllUnits() throws SQLException {
        List<String> units = new ArrayList<>();
        String sql = "SELECT unit_name FROM units WHERE user_id = ? ORDER BY unit_name ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    units.add(rs.getString("unit_name"));
                }
            }
        }
        
        return units;
    }
    
    /**
     * Add a new unit of measurement
     */
    public boolean addUnit(String unitName) throws SQLException {
        if (unitName == null || unitName.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }
        
        String sql = "INSERT INTO units (unit_name, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, unitName.trim().toLowerCase());
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Check for duplicate entry
            if (e.getMessage().contains("Duplicate entry")) {
                throw new SQLException("Unit '" + unitName + "' already exists");
            }
            throw e;
        }
    }
    
    /**
     * Delete a unit (only if not used by any products)
     */
    public boolean deleteUnit(String unitName) throws SQLException {
        // Check if unit is in use
        String checkSql = "SELECT COUNT(*) FROM products WHERE unit_of_measurement = ? AND user_id = ?";
        String deleteSql = "DELETE FROM units WHERE unit_name = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if unit is being used
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, unitName);
                checkStmt.setInt(2, userId);
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Cannot delete unit - it is being used by " + rs.getInt(1) + " product(s)");
                    }
                }
            }
            
            // Delete the unit
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, unitName);
                deleteStmt.setInt(2, userId);
                
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
        }
    }
}
