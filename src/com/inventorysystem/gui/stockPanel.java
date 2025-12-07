package com.inventorysystem.gui;

import com.inventorysystem.data.StockRepository;
import com.inventorysystem.data.UserRepository;
import com.inventorysystem.model.StockRecord;
import com.inventorysystem.util.CSVExporter;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// stockPanel
public class stockPanel extends JPanel {

    private DefaultTableModel model;
    private JTable stockTable;
    private final int userId;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private DateRangePanel dateRangePanel;

    public stockPanel(userFrame mainFrame) {
        int originalUserId = mainFrame.loggedInUserId;
        String userRole = mainFrame.loggedInUserRole;
        this.userRepository = new UserRepository();
        
        // Employee sees admin's data
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
        
        this.stockRepository = new StockRepository();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        //Initialize Table FIRST so stockTable exists for the search bar
        JScrollPane tableScrollPane = createTablePanel();

        //Then create Top Panel (Search Bar depends on table)
        JPanel topPanel = createTopPanel();

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Reload data when this panel is shown
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadStockSummary();
            }
        });

        loadStockSummary();
    }

    // Title and date filter
    // createTopPanel with Search Bar
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Left: Title
        JLabel titleLabel = new JLabel("STOCKS SUMMARY");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // Right: Date Range + Search
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);

        // Date Filter
        dateRangePanel = new DateRangePanel();
        dateRangePanel.addDateRangeChangeListener(() -> loadStockSummary());
        rightPanel.add(dateRangePanel);
        
        // Export CSV button
        JButton exportButton = new JButton("\u2193 Export CSV"); // ↓ symbol
        exportButton.setFont(UIConstants.BUTTON_FONT);
        exportButton.setFocusPainted(false);
        exportButton.setBackground(new Color(40, 167, 69)); // Clean green
        exportButton.setForeground(Color.WHITE);
        exportButton.setPreferredSize(new Dimension(130, 35));
        exportButton.setBorderPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setToolTipText("Export stock logs to CSV file");
        exportButton.addActionListener(e -> CSVExporter.exportTableToCSV(stockTable, "stock_logs", this));
        rightPanel.add(exportButton);
        
        // Search Field
        JTextField searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Search stocks...");
        searchField.setFont(UIConstants.INPUT_FONT);
        searchField.setPreferredSize(new Dimension(180, 35));
        
        // Filter Logic
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        stockTable.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        rightPanel.add(new JLabel("Search:"));
        rightPanel.add(searchField);
        
        topPanel.add(rightPanel, BorderLayout.EAST);
        return topPanel;
    }

    // Table showing stock movements
    private JScrollPane createTablePanel() {
        String[] columnNames = {"#", "ID", "Product Name", "Category", "Unit", "Stock In", "Stock Out", "Available", "Status"};
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        stockTable = new JTable(model) {
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
        stockTable.setFillsViewportHeight(true);
        stockTable.setFont(UIConstants.TABLE_FONT);
        stockTable.setRowHeight(35);
        stockTable.setShowVerticalLines(false);
        stockTable.setGridColor(UIConstants.BORDER_COLOR);
        stockTable.setIntercellSpacing(new Dimension(0, 0));
        stockTable.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        stockTable.setSelectionForeground(Color.WHITE);
        stockTable.setAutoCreateRowSorter(true);
        
        // Header styling
        javax.swing.table.JTableHeader header = stockTable.getTableHeader();
        header.setFont(UIConstants.TABLE_HEADER_FONT);
        header.setBackground(UIConstants.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(UIConstants.PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
                c.setFont(UIConstants.TABLE_HEADER_FONT);
                ((javax.swing.JLabel) c).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        // Custom cell renderer with striping and status colors
        stockTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
        
        // Custom renderer for row number column (#)
        stockTable.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(String.valueOf(row + 1));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (isSelected) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                    setForeground(UIConstants.TEXT_PRIMARY);
                }
                setFont(new Font("Segoe UI", Font.PLAIN, 10));
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        
        // Set column widths
        stockTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // #
        stockTable.getColumnModel().getColumn(0).setMaxWidth(50);
        stockTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // ID
        stockTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Product Name
        stockTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Category
        stockTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Unit
        stockTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Stock In
        stockTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Stock Out
        stockTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Available
        stockTable.getColumnModel().getColumn(8).setPreferredWidth(110); // Status

        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Enable fast mouse wheel scrolling on entire panel
        scrollPane.addMouseWheelListener(e -> {
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            int scrollAmount = e.getUnitsToScroll() * 5000;
            scrollBar.setValue(scrollBar.getValue() + scrollAmount);
        });
        
        return scrollPane;
    }

    // Get data from database and put in table
    private void loadStockSummary() {
        model.setRowCount(0);
        try {
            java.sql.Date startDate = dateRangePanel.getSqlStartDate();
            java.sql.Date endDate = dateRangePanel.getSqlEndDate();
            List<StockRecord> summary = stockRepository.getStockSummaryWithDateRange(userId, startDate, endDate);
            
            for (StockRecord record : summary) {
                int available = record.endingStock();
                String status;
                
                // Determine status based on realistic stock thresholds
                if (available == 0) {
                    status = "Out of Stock";
                } else if (available <= 30) {
                    status = "Critical";
                } else if (available <= 50) {
                    status = "Low Stock";
                } else if (available <= 100) {
                    status = "Good";
                } else {
                    status = "Overstocked";
                }
                
                model.addRow(new Object[]{
                    null,
                    record.productId(),
                    record.productName(),
                    record.category(),
                    record.unit(),
                    record.stockIn(),
                    record.stockOut(),
                    available,
                    status
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Could not load stock summary: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            com.inventorysystem.util.DebugLogger.error("Error loading stock summary", e);
        }
    }

    // Test method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Stock Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            mockFrame.loggedInUserRole = "Admin";
            frame.add(new stockPanel(mockFrame));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}