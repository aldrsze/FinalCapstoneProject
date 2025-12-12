package com.inventorysystem.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;

public class HelpDialog extends JDialog {
    
    public HelpDialog(JFrame parent) {
        super(parent, "Help Guide", true);
        setLayout(new BorderLayout());
        setSize(900, 700);
        setLocationRelativeTo(parent);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("SmartStock Help & User Guide", SwingConstants.CENTER);
        titleLabel.setFont(UIConstants.TITLE_FONT.deriveFont(24f));
        titleLabel.setForeground(UIConstants.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Scroll panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        // Add sections
        addSection(mainPanel, "Getting Started", createGettingStartedContent());
        addSection(mainPanel, "Product Management", createProductsContent());
        addSection(mainPanel, "Sales & Transactions", createSalesContent());
        addSection(mainPanel, "QR Code Features", createQRContent());
        addSection(mainPanel, "Dashboard & Analytics", createDashboardContent());
        addSection(mainPanel, "Reports & Export", createReportsContent());
        addSection(mainPanel, "Store Settings", createSettingsContent());
        addSection(mainPanel, "Employee Management", createEmployeeContent());
        addSection(mainPanel, "System Information", createSystemInfoContent());
        addSection(mainPanel, "Troubleshooting", createTroubleshootingContent());
        
        // Scrollpane for content
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        footerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(UIConstants.BUTTON_FONT);
        closeBtn.setForeground(UIConstants.WHITE);
        closeBtn.setBackground(UIConstants.GREY_COLOR);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setOpaque(true);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.addActionListener(e -> dispose());
        
        footerPanel.add(closeBtn);
        add(footerPanel, BorderLayout.SOUTH);
        
        // Ensure scroll starts at top when dialog opens
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
    
    private void addSection(JPanel parent, String title, JPanel content) {
        // Section wrapper with clean card design
        JPanel sectionWrapper = new JPanel();
        sectionWrapper.setLayout(new BoxLayout(sectionWrapper, BoxLayout.Y_AXIS));
        sectionWrapper.setBackground(UIConstants.WHITE);
        sectionWrapper.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 15, 10, 15),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
                new EmptyBorder(20, 25, 20, 25)
            )
        ));
        sectionWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        // Section header
        JLabel headerLabel = new JLabel(title, SwingConstants.CENTER);
        headerLabel.setFont(UIConstants.TITLE_FONT.deriveFont(20f));
        headerLabel.setForeground(UIConstants.PRIMARY_COLOR);
        headerLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sectionWrapper.add(headerLabel);
        
        // Section content
        content.setBackground(UIConstants.WHITE);
        content.setBorder(null);
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        sectionWrapper.add(content);
        
        parent.add(sectionWrapper);
    }
    
    private JPanel createGettingStartedContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Welcome!");
        addText(panel, "SmartStock helps you manage your store inventory easily.");
        
        addSubtitle(panel, "Main Tabs");
        addText(panel, "• Dashboard - See sales and alerts\n• Products - Manage your items\n• Stock - Check inventory levels\n• Records - View transaction history\n• About - System information");
        
        addSubtitle(panel, "User Roles");
        addText(panel, "Admin - Can do everything (add, edit, delete, manage users)\n\nEmployee - Can only view products and records");
        
        addSubtitle(panel, "Quick Tips");
        addText(panel, "1. Check Dashboard for daily summary\n2. Add products in Products tab\n3. Use QR codes for faster sales\n4. Export CSV for reports\n5. Watch for low stock alerts");
        
        return panel;
    }
    
    private JPanel createProductsContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Add Product");
        addText(panel, "1. Click '+ Add Product' button\n2. Enter product name\n3. Choose category\n4. Enter cost price\n5. Set markup % (retail price auto-calculates)\n6. Enter stock quantity\n7. Click Save");
        
        addSubtitle(panel, "Edit Product");
        addText(panel, "1. Click on product\n2. Click Edit button\n3. Change details\n4. Click Save");
        
        addSubtitle(panel, "Delete Product");
        addText(panel, "Single: Select product → Click Delete → Confirm\n\nBulk: Actions → Bulk Delete → Select items → Confirm\n\nWarning: Cannot be undone!");
        
        addSubtitle(panel, "Stock Status Colors");
        addText(panel, "[RED] Out of Stock - Reorder now\n[ORANGE] Critical (1-30) - Very low\n[YELLOW] Low (31-50) - Running low\n[GREEN] Good (51-100) - Healthy\n[BLUE] Overstocked (101+) - Too much");
        
        addSubtitle(panel, "Units");
        addText(panel, "Piece, Liter, Milliliter, Gram, Kilogram, Per Pack, Slice, Scoop");
        
        return panel;
    }
    
    private JPanel createSalesContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Sell Products");
        addText(panel, "Products tab:\n1. Select product\n2. Click Sell button\n3. Enter quantity\n4. Confirm\n\nDashboard:\n1. Click Process Sale\n2. Choose product\n3. Enter quantity\n4. Confirm");
        
        addSubtitle(panel, "Returns & Refunds");
        addText(panel, "1. Select product\n2. Click Return/Refund button\n3. Choose type:\n\n[BLUE] Customer Return - Add back to stock\n[ORANGE] Reject/Damaged - Remove from stock\n[PURPLE] Refund to Supplier - Return defective item\n\n4. Enter quantity and reason\n5. Confirm");
        
        addSubtitle(panel, "Transaction Types");
        addText(panel, "[GREEN] STOCK-IN - Added inventory\n[ORANGE] STOCK-OUT - Removed from inventory (includes sales, removals, rejects)\n[BLUE] CUSTOMER-RETURN - Returned item\n[PURPLE] REFUND - Supplier refund");
        
        return panel;
    }
    
    private JPanel createQRContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Generate QR Code");
        addText(panel, "1. Click Generate QR\n2. Select product\n3. Choose or add unit of measurement (use '+' to add new)\n4. Click Generate\n5. Save as PNG");
        
        addSubtitle(panel, "Scan QR Code");
        addText(panel, "Camera:\n1. Go to Scanning tab\n2. Choose mode (Add/Sell/Delete)\n3. Click Start Scanner\n4. Show QR to camera\n\nImage file:\n1. Click Actions\n2. Scan QR from Image\n3. Choose file\n\nTip: Good lighting and hold steady");
        
        return panel;
    }
    
    private JPanel createDashboardContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Dashboard Stats");
        addText(panel, "• Total Products\n• Low Stock Items\n• Today's Sales\n• Today's Revenue");
        
        addSubtitle(panel, "Best Sellers");
        addText(panel, "Shows top 5 selling products with total sold and revenue.");
        
        addSubtitle(panel, "Stock Alerts");
        addText(panel, "Shows products running low or out of stock.\n");
        
        return panel;
    }
    
    private JPanel createReportsContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Export to CSV");
        addText(panel, "Click Export CSV button in:\n• Products - Full product list\n• Stock - Inventory levels\n• Records - Transaction history\n\nFiles open in Excel automatically.");
        
        addSubtitle(panel, "Filter by Date");
        addText(panel, "Records tab:\n1. Click Select Date Range\n2. Choose dates\n3. Click Apply\n4. Click Clear to show all");
        
        return panel;
    }
    
    private JPanel createSettingsContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Access Settings");
        addText(panel, "Click Settings button for:\n• Store Settings (Admin)\n• Employee Management (Admin)\n• Logout\n• About");
        
        addSubtitle(panel, "Store Settings");
        addText(panel, "[PROFILE] Profile - Update store details\n[USER] Username - Change username\n[LOCK] Password - Change password\n[DELETE] Delete Account - Remove account permanently");
        
        addSubtitle(panel, "Security Tips");
        addText(panel, "• Always logout when done\n• Don't share passwords\n• Use strong passwords (8+ characters)\n• Be careful with delete account");
        
        return panel;
    }
    
    private JPanel createEmployeeContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Add Employee");
        addText(panel, "1. Click Add Employee\n2. Enter username\n3. Set password\n4. Click Save");
        
        addSubtitle(panel, "Remove Employee");
        addText(panel, "1. Find employee in table\n2. Click Delete\n3. Confirm");
        
        addSubtitle(panel, "What Employees Can Do");
        addText(panel, "• View products and records\n• View dashboard\n\nCannot:\n• Edit products\n• Access settings\n• Process sales");
        
        return panel;
    }
    
    private JPanel createSystemInfoContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "SmartStock Inventory Management System");
        addText(panel, "Organize Your Business, Maximize Your Profits");
        
        addSubtitle(panel, "Version Information");
        addText(panel, "Version: 1.0.0\nRelease: December, 2025");
        
        addSubtitle(panel, "Technology Stack");
        addText(panel, "Java 21, Swing GUI, MySQL 8.0\nLibraries: ZXing (QR Code), Webcam Capture, JSON\nDatabase: XAMPP, MySQL with JDBC Connector");
        
        addSubtitle(panel, "Key Features");
        addText(panel, "• Product Management with Unit of Measurement Support\n• QR Code Generation & Scanning (Manual & Camera)\n• Real-time Inventory Tracking with Color-Coded Stock Alerts\n• Complete Transaction History (Sales, Returns, Refunds, Removals)\n• Dashboard Analytics (Best Sellers, Net Profit, Inventory Value)\n• Multi-User Support (Admin & Employee Roles)\n• Advanced Operations (Customer Returns, Supplier Refunds, Reject Items)\n• CSV Export & Store Information Management");
        
        addSubtitle(panel, "Support & License");
        addText(panel, "University: Quezon City Univerity\nCourse: Bachelor of Science in Information Technology\nSubject: Introduction to Programming (1st Semester, AY 2025-2026)");
        
        addText(panel, "© 2025 SmartStock Development Team. All Rights Reserved.");
        
        return panel;
    }
    
    private JPanel createTroubleshootingContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        addSubtitle(panel, "Database Error");
        addText(panel, "'Cannot connect to database'\n\nFix:\n• Make sure XAMPP is running\n• Start MySQL in XAMPP\n• Check database exists in phpMyAdmin");
        
        addSubtitle(panel, "Camera Problems");
        addText(panel, "Camera not working:\n• Close other camera apps\n• Check camera permissions\n• Restart app\n\nQR won't scan:\n• Good lighting needed\n• Hold steady 6-12 inches away\n• Try 'Scan from Image' instead");
        
        addSubtitle(panel, "CSV Won't Open");
        addText(panel, "• Right-click file\n• Open with Excel\n\nFile saved in project folder.");
        
        addSubtitle(panel, "Other Errors");
        addText(panel, "Product not found:\n• Switch tabs to refresh\n\nNot enough stock:\n• Check stock quantity first\n\nLogin failed:\n• Check Caps Lock is OFF\n• Verify username/password");
        
        return panel;
    }
    
    private void addSubtitle(JPanel panel, String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(UIConstants.LABEL_FONT_BOLD_16);
        label.setForeground(UIConstants.TEXT_DARK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(20, 0, 10, 0));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(label);
    }
    
    private void addText(JPanel panel, String text) {
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setFont(UIConstants.LABEL_FONT_PLAIN_15);
        textPane.setForeground(UIConstants.TEXT_PRIMARY);
        textPane.setBackground(UIConstants.WHITE);
        textPane.setEditable(false);
        textPane.setBorder(new EmptyBorder(0, 30, 20, 30));
        
        // Center align the text
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        panel.add(textPane);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HelpDialog dialog = new HelpDialog(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
    }
}

