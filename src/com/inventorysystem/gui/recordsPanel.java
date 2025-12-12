package com.inventorysystem.gui;

import com.inventorysystem.data.RecordsRepository;
import com.inventorysystem.data.UserRepository;
import com.inventorysystem.model.TransactionRecord;
import com.inventorysystem.util.CSVExporter;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// Records panel
public class recordsPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private JTable recordsTable; // For search bar
    private final RecordsRepository recordsRepository;
    private final UserRepository userRepository;
    private final int userId;
    private DateRangePanel dateRangePanel;
    private List<String> rowNotes; // For tooltips

    public recordsPanel(userFrame mainFrame) {
        int originalUserId = mainFrame.loggedInUserId;
        String userRole = mainFrame.loggedInUserRole;
        this.userRepository = new UserRepository();
        
        // Use admin user_id
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
        
        this.recordsRepository = new RecordsRepository();
        this.rowNotes = new ArrayList<>();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)));

        // Init model first
        tableModel = new DefaultTableModel(new String[]{
            "#", "Date", "Product", "Unit", "Type", "Qty", "Cost/Unit", "Total Cost", "Retail/Unit", "Total Retail"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Init table second
        JScrollPane tableScrollPane = createTablePanel();
        add(tableScrollPane, BorderLayout.CENTER);

        // --- 3. Initialize Top Panel THIRD (Search Bar needs recordsTable) ---
        add(createTopPanel(), BorderLayout.NORTH);

        // Load data when panel is shown
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadData();
            }
        });

        loadData();
    }

    // Title, Date Filter, and Search Bar
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Left: Title
        JLabel titleLabel = new JLabel("TRANSACTION HISTORY");
        titleLabel.setFont(UIConstants.TITLE_FONT.deriveFont(Font.BOLD, 20f));
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 20));
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Right: Date Range + Search
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Date Filter
        dateRangePanel = new DateRangePanel();
        dateRangePanel.addDateRangeChangeListener(() -> loadData());
        rightPanel.add(dateRangePanel);
        
        // Export CSV button
        JButton exportButton = new JButton("\u2193 Export CSV"); // ↓ symbol
        exportButton.setFont(UIConstants.BUTTON_FONT);
        exportButton.setFocusPainted(false);
        exportButton.setBackground(UIConstants.EXPORT_BTN_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.setPreferredSize(new Dimension(130, 35));
        exportButton.setBorderPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setToolTipText("Export transaction records to CSV file");
        exportButton.addActionListener(e -> CSVExporter.exportTableToCSV(recordsTable, "transaction_records", this));
        rightPanel.add(exportButton);

        // Search Field
        JTextField searchField = new JTextField(15);
        searchField.setFont(UIConstants.INPUT_FONT);
        searchField.setPreferredSize(new Dimension(180, 35));
        
        // Filter Logic - recordsTable MUST be initialized before this
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        recordsTable.setRowSorter(sorter);
        
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

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // Transaction table
    private JScrollPane createTablePanel() {
        // Assign to class variable 'recordsTable' (NOT a local variable)
        recordsTable = new JTable(tableModel) {
            @Override
            public String getToolTipText(java.awt.event.MouseEvent event) {
                int row = rowAtPoint(event.getPoint());
                int col = columnAtPoint(event.getPoint());
                
                if (row >= 0 && col >= 0) {
                    // Get the cell value
                    Object value = getValueAt(row, col);
                    if (value != null) {
                        String text = value.toString();
                        
                        // Get cell bounds to check if text is truncated
                        java.awt.Rectangle cellRect = getCellRect(row, col, false);
                        java.awt.FontMetrics fm = getFontMetrics(getFont());
                        int textWidth = fm.stringWidth(text);
                        
                        StringBuilder tooltip = new StringBuilder("<html>");
                        
                        // Show cell content if truncated
                        if (textWidth > cellRect.width - 20) {
                            tooltip.append("<b>").append(getColumnName(col)).append(":</b> ").append(text);
                        }
                        
                        // Add notes if available
                        if (row < rowNotes.size()) {
                            int modelRow = convertRowIndexToModel(row);
                            String notes = rowNotes.get(modelRow);
                            if (notes != null && !notes.trim().isEmpty()) {
                                if (tooltip.length() > 6) tooltip.append("<br><br>");
                                tooltip.append("<b>Notes:</b> ").append(notes);
                            }
                        }
                        
                        tooltip.append("</html>");
                        return tooltip.length() > 13 ? tooltip.toString() : null;
                    }
                }
                return null;
            }
        };
        
        recordsTable.setAutoCreateRowSorter(true);
        recordsTable.setFont(UIConstants.TABLE_FONT);
        recordsTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        recordsTable.setShowVerticalLines(false);
        recordsTable.setGridColor(UIConstants.BORDER_COLOR);
        recordsTable.setIntercellSpacing(new Dimension(0, 0));
        recordsTable.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        recordsTable.setSelectionForeground(Color.WHITE);
        
        // Header styling
        javax.swing.table.JTableHeader header = recordsTable.getTableHeader();
        header.setFont(UIConstants.TABLE_HEADER_FONT);
        header.setBackground(UIConstants.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), UIConstants.TABLE_HEADER_HEIGHT));
        header.setOpaque(true);
        
        // Force header colors with custom renderer
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(UIConstants.PRIMARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(UIConstants.TABLE_HEADER_FONT);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
                label.setOpaque(true);
                return label;
            }
        });
        
        // Custom cell renderer with striping
        recordsTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
        
        // Custom renderer for row number column (#)
        recordsTable.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        
        recordsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        recordsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        
        // Add specific renderer for Type column (index 4) to ensure colors show
        recordsTable.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                // Apply transaction type colors using UIConstants
                if (value != null && !isSelected) {
                    String type = value.toString();
                    if (type.equals("STOCK-OUT") || type.equals("SALE")) {
                        setForeground(UIConstants.DANGER_COLOR);     // Red - Outgoing
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("STOCK-IN")) {
                        setForeground(UIConstants.SUCCESS_GREEN);     // Green - Incoming
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("CUSTOMER-RETURN")) {
                        setForeground(UIConstants.INFO_BLUE);    // Blue - Return
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("STOCK-REMOVAL")) {
                        setForeground(UIConstants.WARNING_DEEP);     // Deep Orange - Removal
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("REJECT")) {
                        setForeground(UIConstants.WARNING_COLOR);     // Orange - Damaged
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("REFUND")) {
                        setForeground(UIConstants.PURPLE_COLOR);    // Purple - Refund
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("DISPOSE")) {
                        setForeground(UIConstants.BROWN_COLOR);     // Brown - Disposal
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else if (type.equals("DELETE")) {
                        setForeground(UIConstants.DANGER_CRITICAL);     // Bright Red - Critical
                        setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                    } else {
                        setForeground(UIConstants.TEXT_PRIMARY);
                        setFont(UIConstants.TABLE_FONT);
                    }
                } else if (isSelected) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(Color.WHITE);
                    setFont(UIConstants.TABLE_FONT.deriveFont(Font.BOLD, 12f));
                } else {
                    setForeground(UIConstants.TEXT_PRIMARY);
                    setFont(UIConstants.TABLE_FONT);
                }
                // Row striping
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(recordsTable);
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
    
    // Load transactions from database
    public void loadData() {
        tableModel.setRowCount(0);
        
        new Thread(() -> {
            try {
                java.sql.Date startDate = dateRangePanel.getSqlStartDate();
                java.sql.Date endDate = dateRangePanel.getSqlEndDate();
                List<TransactionRecord> history = recordsRepository.getTransactionHistoryWithDateRange(
                    userId, startDate, endDate);
                
                SwingUtilities.invokeLater(() -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    
                    rowNotes.clear(); // Clear previous notes
                    
                    for (TransactionRecord record : history) {
                        String formattedDate = record.transactionDate() != null 
                            ? dateFormat.format(record.transactionDate()) 
                            : "-";
                        
                        // Calculate totals
                        int qty = Math.abs(record.quantity()); // Use absolute value for display
                        double costPerUnit = record.unitPrice();
                        double retailPerUnit = record.retailPrice();
                        double totalCost = costPerUnit * qty;
                        double totalRetail = retailPerUnit * qty;
                        
                        tableModel.addRow(new Object[]{
                            null, // # column - rendered dynamically
                            formattedDate,
                            record.productName(),
                            record.unit(),
                            record.transactionType(),
                            record.quantity(), // Show original quantity with sign
                            String.format("₱%,.2f", costPerUnit),
                            String.format("₱%,.2f", totalCost),
                            retailPerUnit == 0.0 ? "-" : String.format("₱%,.2f", retailPerUnit),
                            retailPerUnit == 0.0 ? "-" : String.format("₱%,.2f", totalRetail)
                        });
                        
                        // Store notes for tooltip
                        rowNotes.add(record.notes());
                    }
                });
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(this, 
                        "Error loading transaction history: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    // Test method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Records Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            mockFrame.loggedInUserRole = "Admin";
            frame.add(new recordsPanel(mockFrame));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}