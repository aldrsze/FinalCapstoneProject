package com.inventorysystem.data;

import com.inventorysystem.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// UserRepository
public class UserRepository {

    // Login
    public User login(String username, String password) throws SQLException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, username, user_role FROM users WHERE BINARY username = ? AND BINARY password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("user_role")
                    );
                }
            }
        }
        return null;
    }

    // Signup
    public boolean signup(String username, String password, String role) throws SQLException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty.");
        }

        String checkSql = "SELECT COUNT(*) FROM users WHERE BINARY username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Username already exists", "23000", 1062);
                }
            }
        }

        String sql = "INSERT INTO users (username, password, user_role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the newly created user ID
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newUserId = rs.getInt(1);
                        // Add default units for new user
                        addDefaultUnitsForNewUser(newUserId);
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    // Add default units for newly registered users
    private void addDefaultUnitsForNewUser(int userId) {
        String[] defaultUnits = {"piece", "milliliter", "liter", "gram", "kilogram", "per pack", "slice", "scoop"};
        String sql = "INSERT INTO units (unit_name, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (String unit : defaultUnits) {
                pstmt.setString(1, unit);
                pstmt.setInt(2, userId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            // Log error but don't fail user registration
            com.inventorysystem.util.DebugLogger.error("Failed to add default units for user " + userId, e);
        }
    }

    // Get default markup
    public double getDefaultMarkup(int userId) throws SQLException {
        String sql = "SELECT default_markup_percent FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("default_markup_percent");
                }
            }
        }
        return 0.0;
    }

    // Update default markup
    public void updateDefaultMarkup(int userId, double newMarkupPercent) throws SQLException {
        String sql = "UPDATE users SET default_markup_percent = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newMarkupPercent);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    // Get admin ID for employee
    public int getAdminIdForEmployee(int employeeId) throws SQLException {
        String sql = "SELECT admin_id FROM users WHERE user_id = ? AND user_role = 'Employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("admin_id");
                }
            }
        }
        return -1;
    }

    // Add employee
    public boolean addEmployeeUnderAdmin(int adminId, String username, String password) throws SQLException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty.");
        }

        String checkSql = "SELECT COUNT(*) FROM users WHERE BINARY username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Username already exists", "23000", 1062);
                }
            }
        }

        String sql = "INSERT INTO users (username, password, user_role, admin_id) VALUES (?, ?, 'Employee', ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, adminId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get employees by admin
    public List<String> getEmployeesByAdmin(int adminId) throws SQLException {
        List<String> employees = new ArrayList<>();
        String sql = "SELECT username FROM users WHERE admin_id = ? AND user_role = 'Employee' ORDER BY username";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adminId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(rs.getString("username"));
                }
            }
        }
        return employees;
    }

    // Remove employee
    public boolean removeEmployee(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ? AND user_role = 'Employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get employee password (for editing purposes)
    public String getEmployeePassword(String username) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ? AND user_role = 'Employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        }
        return null;
    }

    // Verify current password
    public boolean verifyPassword(int userId, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ? AND BINARY password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Update username
    public void updateUsername(int userId, String newUsername) throws SQLException {
        // Check if username already exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE BINARY username = ? AND user_id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, newUsername);
            checkStmt.setInt(2, userId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Username already exists", "23000", 1062);
                }
            }
        }

        String sql = "UPDATE users SET username = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    // Update password
    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    // Update employee username only
    public boolean updateEmployeeUsername(String oldUsername, String newUsername) throws SQLException {
        // Check if new username already exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE BINARY username = ? AND username != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, newUsername);
            checkStmt.setString(2, oldUsername);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Username already exists", "23000", 1062);
                }
            }
        }

        String sql = "UPDATE users SET username = ? WHERE username = ? AND user_role = 'Employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update employee username and password
    public boolean updateEmployeeCredentials(String oldUsername, String newUsername, String newPassword) throws SQLException {
        // Check if new username already exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE BINARY username = ? AND username != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, newUsername);
            checkStmt.setString(2, oldUsername);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Username already exists", "23000", 1062);
                }
            }
        }

        String sql = "UPDATE users SET username = ?, password = ? WHERE username = ? AND user_role = 'Employee'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, oldUsername);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete user account and all associated data
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            // Return true if account was deleted
            return rowsAffected > 0;
        }
    }
}