package com.inventorysystem.gui;

import com.inventorysystem.data.DashboardRepository;
import com.inventorysystem.data.UserRepository;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

// Dashboard panel
public class dashboardPanel extends JPanel {

    private JLabel productsValue;
    private JLabel totalStockValue;
    private JLabel outOfStockValue;
    private JLabel totalCostValue;
    private JLabel totalIncomeValue;
    private JLabel totalProfitValue;
    
    // Table models
    private DefaultTableModel bestSellersModel;
    private DefaultTableModel stockAlertModel;

    private final int userId;
    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private DateRangePanel dateRangePanel;

    public dashboardPanel(userFrame mainFrame) {
        int originalUserId = mainFrame.loggedInUserId;
        String userRole = mainFrame.loggedInUserRole;
        this.userRepository = new UserRepository();
        
        // For employees, use their admin's user_id to see admin's data
        int effectiveUserId = originalUserId;
        if (userRole.equalsIgnoreCase("Employee")) {
            try {
                int adminId = userRepository.getAdminIdForEmployee(originalUserId);
                if (adminId > 0) {
                    effectiveUserId = adminId;
                }
            } catch (SQLException e) {
                com.inventorysystem.util.DebugLogger.error("Error getting admin ID for employee", e);
            }
        }
        this.userId = effectiveUserId;
        
        this.dashboardRepository = new DashboardRepository();

        setLayout(new BorderLayout(15, 15));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top panel with title and date range
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        dateRangePanel = new DateRangePanel();
        dateRangePanel.addDateRangeChangeListener(() -> refreshData());
        topPanel.add(dateRangePanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // Main content with scrolling
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Compact stats grid (smaller cards)
        JPanel statsGridPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        statsGridPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        statsGridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        productsValue = createCompactValueLabel("0");
        totalStockValue = createCompactValueLabel("0");
        outOfStockValue = createCompactValueLabel("0");
        totalCostValue = createCompactValueLabel("₱0");
        totalIncomeValue = createCompactValueLabel("₱0");
        totalProfitValue = createCompactValueLabel("₱0");

        statsGridPanel.add(createCompactStatCard("Products", productsValue, UIConstants.PRIMARY_COLOR));
        statsGridPanel.add(createCompactStatCard("Total Items", totalStockValue, UIConstants.PRIMARY_COLOR));
        statsGridPanel.add(createCompactStatCard("Out of Stock", outOfStockValue, UIConstants.DANGER_COLOR));
        statsGridPanel.add(createCompactStatCard("Inventory Value", totalCostValue, UIConstants.TEXT_SECONDARY));
        statsGridPanel.add(createCompactStatCard("Total Sales", totalIncomeValue, UIConstants.SUCCESS_COLOR));
        statsGridPanel.add(createCompactStatCard("Net Profit", totalProfitValue, UIConstants.WARNING_COLOR));
        
        mainContent.add(statsGridPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Best Sellers section
        mainContent.add(createBestSellersSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Stock Alert section
        mainContent.add(createStockAlertSection());
        
        // Remove scrolling from main dashboard
        mainContent.setBorder(null);
        add(mainContent, BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
            }
        });

        refreshData();
    }

    private JLabel createCompactValueLabel(String initialText) {
        JLabel label = new JLabel(initialText, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(UIConstants.TEXT_PRIMARY);
        return label;
    }

    private JPanel createCompactStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(15, 10, 15, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBestSellersSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Best Sellers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(22, 160, 133));
        section.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"#", "Product", "Sales Amount", "COGS", "Margin", "Qty Sold"};
        bestSellersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(bestSellersModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent event) {
                int row = rowAtPoint(event.getPoint());
                int col = columnAtPoint(event.getPoint());
                if (row >= 0 && col >= 0) {
                    Object value = getValueAt(row, col);
                    if (value != null) {
                        String text = value.toString();
                        java.awt.Rectangle cellRect = getCellRect(row, col, false);
                        java.awt.FontMetrics fm = getFontMetrics(getFont());
                        int textWidth = fm.stringWidth(text);
                        if (textWidth > cellRect.width - 20) {
                            return "<html><b>" + getColumnName(col) + ":</b> " + text + "</html>";
                        }
                    }
                }
                return null;
            }
        };
        table.setFont(UIConstants.TABLE_FONT);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setGridColor(new Color(0, 0, 0, 0));
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Header styling
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setFont(UIConstants.TABLE_HEADER_FONT);
        header.setBackground(UIConstants.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Custom header renderer
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(UIConstants.PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
                c.setFont(UIConstants.TABLE_HEADER_FONT);
                c.setHorizontalAlignment(JLabel.CENTER);
                c.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        // Custom cell renderer with striping
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Smaller font for # column
                if (column == 0) {
                    setFont(new Font("Segoe UI", Font.PLAIN, 10));
                } else {
                    setFont(UIConstants.TABLE_FONT);
                }
                
                // Alignment: # centered
                if (column == 0) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else if (column == 1) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }
                
                // Row striping
                if (isSelected) {
                    c.setBackground(UIConstants.PRIMARY_LIGHT);
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(UIConstants.BACKGROUND_COLOR);
                    }
                    c.setForeground(UIConstants.TEXT_PRIMARY);
                }
                
                // Padding
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                }
                
                return c;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Custom renderer for row number column (#) in Best Sellers table
        table.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setText(String.valueOf(row + 1));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (isSelected) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.BACKGROUND_COLOR);
                    setForeground(UIConstants.TEXT_PRIMARY);
                }
                setFont(new Font("Segoe UI", Font.PLAIN, 10));
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return this;
            }
        });
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Product
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Sales Amount
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // COGS
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Margin
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Qty Sold
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        
        // Set exact height for 5 rows only
        int tableHeight = 40 + (5 * 35); // header + 5 rows
        scrollPane.setPreferredSize(new Dimension(0, tableHeight));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, tableHeight));
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        // Constrain section height: title(25) + top_padding(15) + table(215) + bottom_padding(15) = 270
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        
        return section;
    }
    
    private JPanel createStockAlertSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Stock Alert");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(22, 160, 133));
        section.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"#", "Description", "Quantity", "Status"};
        stockAlertModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(stockAlertModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent event) {
                int row = rowAtPoint(event.getPoint());
                int col = columnAtPoint(event.getPoint());
                if (row >= 0 && col >= 0) {
                    Object value = getValueAt(row, col);
                    if (value != null) {
                        String text = value.toString();
                        java.awt.Rectangle cellRect = getCellRect(row, col, false);
                        java.awt.FontMetrics fm = getFontMetrics(getFont());
                        int textWidth = fm.stringWidth(text);
                        if (textWidth > cellRect.width - 20) {
                            return "<html><b>" + getColumnName(col) + ":</b> " + text + "</html>";
                        }
                    }
                }
                return null;
            }
        };
        table.setFont(UIConstants.TABLE_FONT);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setGridColor(new Color(0, 0, 0, 0));
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Header styling
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setFont(UIConstants.TABLE_HEADER_FONT);
        header.setBackground(UIConstants.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Custom header renderer
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(UIConstants.PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
                c.setFont(UIConstants.TABLE_HEADER_FONT);
                c.setHorizontalAlignment(JLabel.CENTER);
                c.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        // Custom cell renderer with striping
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Font & Alignment Logic
                if (column == 0) {
                    setFont(new Font("Segoe UI", Font.PLAIN, 10));
                } else if (column == 3) { // Status Column - Bold
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    setFont(UIConstants.TABLE_FONT);
                }
                
                // Alignment
                if (column == 1) { // Description
                    setHorizontalAlignment(JLabel.LEFT);
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }
                
                // Color Logic
                if (isSelected) {
                    c.setBackground(UIConstants.PRIMARY_LIGHT);
                    c.setForeground(Color.WHITE);
                } else {
                    // Background Striping
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(UIConstants.BACKGROUND_COLOR);
                    }
                    
                    // --- NEW: Custom Text Colors for Status (Column 3) ---
                    if (column == 3) {
                        String status = value != null ? value.toString() : "";
                        if (status.equals("Out of Stock")) {
                            c.setForeground(new Color(220, 53, 69)); // Red - Critical
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else if (status.equals("Critical")) {
                            c.setForeground(new Color(255, 87, 34)); // Deep Orange - Urgent
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else if (status.equals("Low Stock")) {
                            c.setForeground(new Color(255, 152, 0)); // Orange - Warning
                            setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else if (status.equals("Good")) {
                            c.setForeground(new Color(76, 175, 80)); // Green - Healthy
                        } else if (status.equals("Overstocked")) {
                            c.setForeground(new Color(33, 150, 243)); // Blue - Info
                        } else {
                            c.setForeground(UIConstants.TEXT_PRIMARY);
                        }
                    } else {
                        c.setForeground(UIConstants.TEXT_PRIMARY);
                    }
                }
                
                // Padding
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                }
                
                return c;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Custom renderer for row number column (#) in Stock Alert table
        table.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setText(String.valueOf(row + 1));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (isSelected) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.BACKGROUND_COLOR);
                    setForeground(UIConstants.TEXT_PRIMARY);
                }
                setFont(new Font("Segoe UI", Font.PLAIN, 10));
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return this;
            }
        });
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(280); // Description
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Quantity
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Status
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        // Set height to fill remaining space down to bottom of visible area
        int stockAlertTableHeight = 340;
        scrollPane.setPreferredSize(new Dimension(0, stockAlertTableHeight));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, stockAlertTableHeight));
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        // Constrain section to prevent expansion
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, stockAlertTableHeight + 80));
        
        return section;
    }

    
    private void refreshData() {
        updateProducts();
        updateTotalStock();
        updateOutOfStock();
        updateInventoryValue();
        updateTotalSalesWithDateRange();
        updateNetProfitWithDateRange();
        updateBestSellers();
        updateStockAlerts();
    }

    private void updateProducts() {
        try {
            int count = dashboardRepository.getTotalProducts(userId);
            productsValue.setText(String.valueOf(count));
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error updating products", e);
            productsValue.setText("Error");
        }
    }

    private void updateTotalStock() {
        try {
            int totalStock = dashboardRepository.getTotalStock(userId);
            totalStockValue.setText(String.valueOf(totalStock));
        } catch (SQLException e) {
             com.inventorysystem.util.DebugLogger.error("Error updating total stock", e);
            totalStockValue.setText("Error");
        }
    }

    private void updateOutOfStock() {
        try {
            int outOfStock = dashboardRepository.getOutOfStockCount(userId);
            outOfStockValue.setText(String.valueOf(outOfStock));
        } catch (SQLException e) {
             com.inventorysystem.util.DebugLogger.error("Error updating out of stock count", e);
            outOfStockValue.setText("Error");
        }
    }

    private void updateInventoryValue() {
        try {
            double cost = dashboardRepository.getTotalInventoryCost(userId);
            totalCostValue.setText("₱" + String.format("%,.0f", cost));
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error updating inventory value", e);
            totalCostValue.setText("Error");
        }
    }

    private void updateTotalSalesWithDateRange() {
        try {
            java.sql.Date startDate = dateRangePanel.getSqlStartDate();
            java.sql.Date endDate = dateRangePanel.getSqlEndDate();
            double income = dashboardRepository.getTotalIncome(userId, startDate, endDate);
            totalIncomeValue.setText("₱" + String.format("%,.0f", income));
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error updating total sales", e);
            totalIncomeValue.setText("Error");
        }
    }

    private void updateNetProfitWithDateRange() {
        try {
            java.sql.Date startDate = dateRangePanel.getSqlStartDate();
            java.sql.Date endDate = dateRangePanel.getSqlEndDate();
            double profit = dashboardRepository.getTotalProfit(userId, startDate, endDate);
            totalProfitValue.setText("₱" + String.format("%,.0f", profit));
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error updating net profit", e);
            totalProfitValue.setText("Error");
        }
    }
    
    private void updateBestSellers() {
    try {
        bestSellersModel.setRowCount(0);
        
        // Get dates from the panel
        java.sql.Date startDate = dateRangePanel.getSqlStartDate();
        java.sql.Date endDate = dateRangePanel.getSqlEndDate();
        
        // Call the date-aware repository method
        java.util.List<Object[]> bestSellers = dashboardRepository.getBestSellers(userId, startDate, endDate, 5);
        
        if (bestSellers.isEmpty()) {
            // Placeholder if no sales found
            bestSellersModel.addRow(new Object[]{null, "-", "No sales in this period", "-", "-", "-"});
        } else {
            for (Object[] row : bestSellers) {
                // Add null placeholder for # column at index 0
                Object[] rowWithNumber = new Object[row.length + 1];
                rowWithNumber[0] = null; // # column - rendered dynamically
                System.arraycopy(row, 0, rowWithNumber, 1, row.length);
                bestSellersModel.addRow(rowWithNumber);
            }
        }
    } catch (SQLException e) {
        // IMPROVED ERROR HANDLING: Logs error and shows error in table
        com.inventorysystem.util.DebugLogger.error("Error updating best sellers", e);
        bestSellersModel.setRowCount(0);
        bestSellersModel.addRow(new Object[]{null, "!", "Error: " + e.getMessage(), "", "", ""});
    }
}
    
    private void updateStockAlerts() {
        try {
            stockAlertModel.setRowCount(0);
            java.util.List<Object[]> alerts = dashboardRepository.getStockAlerts(userId);
            for (Object[] row : alerts) {
                // Add null placeholder for # column at index 0
                Object[] rowWithNumber = new Object[row.length + 1];
                rowWithNumber[0] = null; // # column - rendered dynamically
                System.arraycopy(row, 0, rowWithNumber, 1, row.length);
                stockAlertModel.addRow(rowWithNumber);
            }
        } catch (SQLException e) {
            com.inventorysystem.util.DebugLogger.error("Error updating stock alerts", e);
            stockAlertModel.setRowCount(0);
            stockAlertModel.addRow(new Object[]{null, "", "Error loading data", ""});
        }
    }

    // Test method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dashboard Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 3;
            mockFrame.loggedInUserRole = "Admin";
            frame.add(new dashboardPanel(mockFrame));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

