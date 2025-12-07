package com.inventorysystem.gui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// CustomTableRenderer
public class CustomTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Alignment
        setHorizontalAlignment(JLabel.CENTER);
        
        // Fonts
        if (column == 0) { // Row numbers
            setFont(new Font("Segoe UI", Font.PLAIN, 10));
            if (!isSelected) setForeground(Color.GRAY);
        } else {
            setFont(UIConstants.TABLE_FONT);
            setForeground(isSelected ? Color.WHITE : UIConstants.TEXT_PRIMARY);
        }

        // Colors
        if (isSelected) {
            c.setBackground(UIConstants.PRIMARY_LIGHT);
        } else {
            c.setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.BACKGROUND_COLOR);
        }
        
        // Padding
        ((JLabel) c).setBorder(new EmptyBorder(0, 10, 0, 10));
        
        // Stock Status Color Coding (realistic retail thresholds)
        if (value != null) {
            String s = value.toString();
            if (s.equals("Out of Stock")) {
                c.setForeground(new Color(220, 53, 69));    // Red - Critical
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Critical")) {
                c.setForeground(new Color(255, 87, 34));     // Deep Orange - Urgent
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Low Stock")) {
                c.setForeground(new Color(255, 152, 0));     // Orange - Warning
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Good")) {
                c.setForeground(new Color(76, 175, 80));     // Green - Healthy
            } else if (s.equals("Overstocked")) {
                c.setForeground(new Color(33, 150, 243));    // Blue - Info
            }
            // Transaction Type Color Coding (for records panel)
            else if (s.equals("STOCK-OUT") || s.equals("SALE")) {
                c.setForeground(new Color(220, 53, 69));     // Red - Outgoing
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("STOCK-IN")) {
                c.setForeground(new Color(76, 175, 80));     // Green - Incoming
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("CUSTOMER-RETURN")) {
                c.setForeground(new Color(33, 150, 243));    // Blue - Return
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("STOCK-REMOVAL") || s.equals("REJECT")) {
                c.setForeground(new Color(255, 87, 34));     // Deep Orange - Removal
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("REFUND") || s.equals("DISPOSE")) {
                c.setForeground(new Color(156, 39, 176));    // Purple - Special
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("DELETE")) {
                c.setForeground(new Color(244, 67, 54));     // Bright Red - Critical
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            }
        }

        return c;
    }
}