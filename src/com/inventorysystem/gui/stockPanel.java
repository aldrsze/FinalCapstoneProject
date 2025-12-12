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

// Stock panel
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
        
        // Show admin data
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
        setBorder(new EmptyBorder(UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING, UIConstants.PANEL_PADDING));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Init table first
        JScrollPane tableScrollPane = createTablePanel();

        // Create top panel
        JPanel topPanel = createTopPanel();

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Reload data
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadStockSummary();
            }
        });

        loadStockSummary();
    }

    // Title/date filter
    // Top panel/search
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Left: title
        JLabel titleLabel = new JLabel("STOCKS SUMMARY");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // Right: date/search
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Date filter
        dateRangePanel = new DateRangePanel();
        dateRangePanel.addDateRangeChangeListener(() -> loadStockSummary());
        rightPanel.add(dateRangePanel);
        
        // Export CSV
        JButton exportButton = new JButton("\u2193 Export CSV"); // ↓ symbol
        exportButton.setFont(UIConstants.BUTTON_FONT);
        exportButton.setFocusPainted(false);
        exportButton.setBackground(UIConstants.EXPORT_BTN_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.setPreferredSize(new Dimension(130, 35));
        exportButton.setBorderPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setToolTipText("Export stock logs to CSV file");
        exportButton.addActionListener(e -> CSVExporter.exportTableToCSV(stockTable, "stock_logs", this));
        rightPanel.add(exportButton);
        
        // Search field
        JTextField searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Search stocks...");
        searchField.setFont(UIConstants.INPUT_FONT);
        searchField.setPreferredSize(new Dimension(180, 35));
        
        // Filter logic
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

    // Table: stock moves
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
        stockTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        stockTable.setShowVerticalLines(false);
        stockTable.setGridColor(UIConstants.BORDER_COLOR);
        stockTable.setIntercellSpacing(new Dimension(0, 0));
        stockTable.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        stockTable.setSelectionForeground(Color.WHITE);
        stockTable.setAutoCreateRowSorter(true);
        
        // Header
        javax.swing.table.JTableHeader header = stockTable.getTableHeader();
        header.setFont(UIConstants.TABLE_HEADER_FONT);
        header.setBackground(UIConstants.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), UIConstants.TABLE_HEADER_HEIGHT));
        
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
        
        // Custom renderer
        stockTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
        
        // Renderer: # col
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
                setFont(UIConstants.TABLE_SMALL_FONT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        
        // Col widths
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
        scrollPane.getViewport().setBackground(UIConstants.FORM_COLOR);
        
        // Fast scroll
        scrollPane.addMouseWheelListener(e -> {
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            int scrollAmount = e.getUnitsToScroll() * 5000;
            scrollBar.setValue(scrollBar.getValue() + scrollAmount);
        });
        
        return scrollPane;
    }

    // Get data
    private void loadStockSummary() {
        model.setRowCount(0);
        try {
            java.sql.Date startDate = dateRangePanel.getSqlStartDate();
            java.sql.Date endDate = dateRangePanel.getSqlEndDate();
            List<StockRecord> summary = stockRepository.getStockSummaryWithDateRange(userId, startDate, endDate);
            
            for (StockRecord record : summary) {
                int available = record.endingStock();
                String status;
                
                // Status logic
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