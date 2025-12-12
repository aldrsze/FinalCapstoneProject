package com.inventorysystem.gui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Custom table renderer
public class CustomTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Alignment
        setHorizontalAlignment(JLabel.CENTER);

        // Fonts and Colors
        if (column == 0) { // Row numbers
            setFont(UIConstants.TABLE_SMALL_FONT);
            if (!isSelected) setForeground(UIConstants.TEXT_SECONDARY);
        } else {
            setFont(UIConstants.TABLE_FONT);
            setForeground(isSelected ? UIConstants.WHITE : UIConstants.TEXT_PRIMARY);
        }

        // Row background
        if (isSelected) {
            c.setBackground(UIConstants.PRIMARY_LIGHT);
        } else {
            c.setBackground(row % 2 == 0 ? UIConstants.WHITE : UIConstants.BACKGROUND_COLOR);
        }

        // Padding
        ((JLabel) c).setBorder(new EmptyBorder(0, 10, 0, 10));

        // Stock Status & Transaction Type Color Coding
        if (value != null) {
            String s = value.toString();
            if (s.equals("Out of Stock")) {
                c.setForeground(UIConstants.DANGER_COLOR); // Red - Critical
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Critical")) {
                c.setForeground(UIConstants.WARNING_DEEP); // Deep Orange - Urgent
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Low Stock")) {
                c.setForeground(UIConstants.WARNING_COLOR); // Orange - Warning
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("Good")) {
                c.setForeground(UIConstants.SUCCESS_GREEN); // Green - Healthy
            } else if (s.equals("Overstocked")) {
                c.setForeground(UIConstants.INFO_BLUE); // Blue - Info
            }
            // Transaction Type Color Coding (for records panel)
            else if (s.equals("STOCK-OUT") || s.equals("SALE")) {
                c.setForeground(UIConstants.DANGER_COLOR); // Red - Outgoing
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("STOCK-IN")) {
                c.setForeground(UIConstants.SUCCESS_GREEN); // Green - Incoming
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("CUSTOMER-RETURN")) {
                c.setForeground(UIConstants.INFO_BLUE); // Blue - Return
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("STOCK-REMOVAL") || s.equals("REJECT")) {
                c.setForeground(UIConstants.WARNING_DEEP); // Deep Orange - Removal
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("REFUND") || s.equals("DISPOSE")) {
                c.setForeground(UIConstants.PURPLE_COLOR); // Purple - Special
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (s.equals("DELETE")) {
                c.setForeground(UIConstants.DANGER_CRITICAL); // Bright Red - Critical
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            }
        }

        return c;
    }
}