package com.inventorysystem.gui;

import com.inventorysystem.data.StoreRepository;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Main app panel
public class MainApplicationPanel extends JPanel {
    
    private final userFrame mainFrame;
    private final String userRole;
    private final StoreRepository storeRepository;
    
    private final CardLayout cardLayout;
    private final JPanel mainContentPanel;
    private JLabel storeNameLabel;
    private JLabel storeLocationLabel;
    private JLabel storeContactLabel;
    private JButton currentSelectedButton;
    
    private JPanel navPanel; // Store reference to navigation panel
    private boolean navPanelVisible = true; // Track visibility state
    private JButton toggleNavButton; // Toggle button reference

    public MainApplicationPanel(userFrame mainFrame, String username, String role, 
                                String storeName, String storeLocation, String storeContact) {
        this.mainFrame = mainFrame;
        this.userRole = role;
        this.storeRepository = new StoreRepository();
        this.cardLayout = new CardLayout();
        
        setLayout(new BorderLayout());

        navPanel = createNavigationPanel(username, role, storeName, storeLocation, storeContact);
        add(navPanel, BorderLayout.WEST);

        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        mainContentPanel.add(new dashboardPanel(mainFrame), "Dashboard");
        mainContentPanel.add(new productsPanel(mainFrame), "Products");
        mainContentPanel.add(new stockPanel(mainFrame), "Stocks");
        mainContentPanel.add(new recordsPanel(mainFrame), "Records");
        mainContentPanel.add(new AboutPanel(mainFrame), "About");
        
        JScrollPane contentScrollPane = new JScrollPane(mainContentPanel);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Create top bar with toggle button
        JPanel topBar = createTopBarWithToggle();
        
        // Wrapper panel for top bar and content
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.add(topBar, BorderLayout.NORTH);
        contentWrapper.add(contentScrollPane, BorderLayout.CENTER);
        
        add(contentWrapper, BorderLayout.CENTER);
        
        cardLayout.show(mainContentPanel, "Dashboard");
    }
    
    /**
     * Creates the top bar with hamburger menu toggle button
     */
    private JPanel createTopBarWithToggle() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIConstants.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR));
        topBar.setPreferredSize(new Dimension(0, 50));
        
        // Hamburger menu button with custom paint
        toggleNavButton = new JButton("") { // Empty text to avoid emoji boxes
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw three horizontal lines (hamburger icon)
                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int lineWidth = 20;
                int lineSpacing = 6;
                
                // Top line
                g2.drawLine(centerX - lineWidth/2, centerY - lineSpacing, 
                           centerX + lineWidth/2, centerY - lineSpacing);
                // Middle line
                g2.drawLine(centerX - lineWidth/2, centerY, 
                           centerX + lineWidth/2, centerY);
                // Bottom line
                g2.drawLine(centerX - lineWidth/2, centerY + lineSpacing, 
                           centerX + lineWidth/2, centerY + lineSpacing);
            }
        };
        
        toggleNavButton.setPreferredSize(new Dimension(50, 50));
        toggleNavButton.setBackground(UIConstants.WHITE);
        toggleNavButton.setForeground(UIConstants.TEXT_PRIMARY);
        toggleNavButton.setFocusPainted(false);
        toggleNavButton.setBorderPainted(false);
        toggleNavButton.setContentAreaFilled(false);
        toggleNavButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleNavButton.setToolTipText("Toggle Navigation Panel");
        toggleNavButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24)); // Fallback if custom paint fails
        
        toggleNavButton.addActionListener(e -> toggleNavigationPanel());
        
        toggleNavButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                toggleNavButton.setBackground(new Color(245, 245, 245));
                toggleNavButton.setOpaque(true);
            }
            public void mouseExited(MouseEvent e) {
                toggleNavButton.setBackground(Color.WHITE);
                toggleNavButton.setOpaque(false);
            }
        });
        
        topBar.add(toggleNavButton, BorderLayout.WEST);
        
        return topBar;
    }
    
    /**
     * Toggles the visibility of the navigation panel
     */
    private void toggleNavigationPanel() {
        navPanelVisible = !navPanelVisible;
        navPanel.setVisible(navPanelVisible);
        
        // Update tooltip
        if (navPanelVisible) {
            toggleNavButton.setToolTipText("Hide Navigation Panel");
        } else {
            toggleNavButton.setToolTipText("Show Navigation Panel");
        }
        
        revalidate();
        repaint();
    }

    private JPanel createNavigationPanel(String username, String role, String storeName, 
                                        String storeLocation, String storeContact) {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setPreferredSize(new Dimension(280, 0));
        navPanel.setBackground(UIConstants.NAV_BACKGROUND);
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(0, 0, 0, 30)));

        navPanel.add(createStoreInfoSection(storeName, storeLocation, storeContact, username, role), 
                    BorderLayout.NORTH);
        navPanel.add(createNavigationButtons(), BorderLayout.CENTER);
        navPanel.add(createActionButtons(), BorderLayout.SOUTH);

        return navPanel;
    }

    private JPanel createStoreInfoSection(String storeName, String storeLocation, 
                                         String storeContact, String username, String role) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.NAV_BACKGROUND);
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel("CURRENT STORE");
        titleLabel.setFont(UIConstants.LABEL_BOLD_FONT.deriveFont(11f));
        titleLabel.setForeground(UIConstants.GREY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        storeNameLabel = new JLabel(storeName);
        storeNameLabel.setFont(UIConstants.TITLE_FONT.deriveFont(20f));
        storeNameLabel.setForeground(UIConstants.WHITE);
        storeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(storeNameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        storeLocationLabel = new JLabel(storeLocation.isEmpty() ? "No location" : storeLocation);
        storeLocationLabel.setFont(UIConstants.LABEL_FONT_PLAIN_12);
        storeLocationLabel.setForeground(UIConstants.BORDER_DARK);
        storeLocationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(storeLocationLabel);

        storeContactLabel = new JLabel(storeContact.isEmpty() ? "No contact" : storeContact);
        storeContactLabel.setFont(UIConstants.LABEL_FONT_PLAIN_12);
        storeContactLabel.setForeground(UIConstants.BORDER_DARK);
        storeContactLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(storeContactLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(127, 140, 141, 100));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel userLabel = new JLabel(username);
        userLabel.setFont(UIConstants.LABEL_BOLD_FONT.deriveFont(16f));
        userLabel.setForeground(UIConstants.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(createRoleBadge(role));

        return panel;
    }

    private JPanel createRoleBadge(String role) {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badge.setMaximumSize(new Dimension(150, 36));
        badge.setPreferredSize(new Dimension(150, 36));
        badge.setBackground(UIConstants.ACCENT_COLOR);
        badge.setBorder(new EmptyBorder(8, 20, 8, 20));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(role.toUpperCase());
        roleLabel.setFont(UIConstants.LABEL_BOLD_FONT.deriveFont(13f));
        roleLabel.setForeground(UIConstants.WHITE);
        badge.add(roleLabel);

        return badge;
    }

    // Main navigation buttons (Dashboard, Products, etc.)
    private JPanel createNavigationButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.NAV_BACKGROUND);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create all buttons
        JButton dashboardBtn = createNavButton("Dashboard", "Dashboard");
        JButton productsBtn = createNavButton("Products", "Products");
        JButton stocksBtn = createNavButton("Stocks", "Stocks");
        JButton recordsBtn = createNavButton("Records", "Records");
        JButton aboutBtn = createNavButton("About", "About");

        // Add buttons with spacing
        panel.add(dashboardBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(productsBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(stocksBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(recordsBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(aboutBtn);
        panel.add(Box.createVerticalGlue());

        // Dashboard is selected by default
        currentSelectedButton = dashboardBtn;
        updateButtonSelection(dashboardBtn);

        return panel;
    }

    private JButton createNavButton(String text, String panelName) {
        String icon = getNavIcon(text);
        JButton button = createBaseButton(icon + "  " + text, UIConstants.NAV_BACKGROUND, new Color(206, 214, 224), UIConstants.NAV_HOVER, false);
        button.setFont(UIConstants.FONT_EMOJI_LARGE.deriveFont(Font.BOLD, 14f));

        button.addActionListener(e -> {
            cardLayout.show(mainContentPanel, panelName);
            updateButtonSelection(button);
        });

        return button;
    }

    private String getNavIcon(String text) {
        switch(text) {
            case "Dashboard": return "📊";
            case "Products": return "📦";
            case "Stocks": return "📈";
            case "Records": return "📋";
            case "About": return "ℹ️";
            default: return "●";
        }
    }

    // Highlight the selected button
    private void updateButtonSelection(JButton selectedButton) {
        // Reset old button
        if (currentSelectedButton != null) {
            currentSelectedButton.setForeground(new Color(206, 214, 224));
            currentSelectedButton.setBackground(UIConstants.NAV_BACKGROUND);
            currentSelectedButton.setOpaque(false);
        }
        
        // Highlight new button
        currentSelectedButton = selectedButton;
        selectedButton.setForeground(Color.WHITE);
        selectedButton.setBackground(UIConstants.ACCENT_COLOR);
        selectedButton.setOpaque(true);
    }

    // Bottom action buttons (Store Settings, Logout, etc.)
    private JPanel createActionButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.NAV_BACKGROUND);
        panel.setBorder(new EmptyBorder(10, 10, 15, 10));

        // Only admins can see these buttons
        if (userRole.equalsIgnoreCase("Admin")) {
            JButton storeDetailsBtn = createActionButton("Store Details", 
                UIConstants.PRIMARY_COLOR, UIConstants.PRIMARY_DARK);
            storeDetailsBtn.addActionListener(e -> openStoreSettings());
            panel.add(storeDetailsBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
            
            JButton manageEmployeesBtn = createActionButton("Manage Employees", 
                UIConstants.PRIMARY_COLOR, UIConstants.PRIMARY_DARK);
            manageEmployeesBtn.addActionListener(e -> openEmployeeManager());
            panel.add(manageEmployeesBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Help button (available to all users)
        JButton helpBtn = createActionButton("Help & Guide", 
            new Color(52, 152, 219), new Color(41, 128, 185));
        helpBtn.addActionListener(e -> openHelpDialog());
        panel.add(helpBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Everyone can logout
        JButton logoutBtn = createActionButton("Logout", 
            UIConstants.DANGER_COLOR, UIConstants.DANGER_DARK);
        logoutBtn.addActionListener(e -> logout());
        panel.add(logoutBtn);

        return panel;
    }

    // Create action button with colors
    private JButton createActionButton(String text, Color bgColor, Color hoverColor) {
        return createBaseButton(text, bgColor, Color.WHITE, hoverColor, true);
    }

    // Helper method to consolidate button creation logic
    private JButton createBaseButton(String text, Color bgColor, Color fgColor, Color hoverColor, boolean isOpaque) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT.deriveFont((bgColor == UIConstants.NAV_BACKGROUND) ? 14f : 12f));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false); // Key for hover effects
        button.setOpaque(isOpaque);
        button.setHorizontalAlignment(bgColor == UIConstants.NAV_BACKGROUND ? SwingConstants.LEFT : SwingConstants.CENTER); // Alignment differs
        
        // Size handling
        if (bgColor == UIConstants.NAV_BACKGROUND) {
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            button.setPreferredSize(new Dimension(240, 48));
            button.setBorder(new EmptyBorder(12, 25, 12, 25));
        } else {
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            button.setPreferredSize(new Dimension(220, 42));
            button.setBorder(new EmptyBorder(10, 18, 10, 18));
        }
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Common MouseListener for Hover Effects
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Logic for Nav Buttons (Selection state check)
                if (bgColor == UIConstants.NAV_BACKGROUND) {
                    if (button != currentSelectedButton) {
                        button.setBackground(hoverColor);
                        button.setForeground(Color.WHITE);
                        button.setOpaque(true);
                    }
                } 
                // Logic for Action Buttons (Simple hover)
                else {
                    button.setBackground(hoverColor);
                    button.setOpaque(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                // Logic for Nav Buttons
                if (bgColor == UIConstants.NAV_BACKGROUND) {
                    if (button != currentSelectedButton) {
                        button.setForeground(fgColor);
                        button.setOpaque(false);
                        button.setBackground(bgColor);
                    }
                } 
                // Logic for Action Buttons
                else {
                    button.setBackground(bgColor);                    
                }
            }
        });

        return button;
    }
    // ========== BUTTON ACTIONS ==========

    // Open store settings dialog
    private void openStoreSettings() {
        StoreSettingsDialog dialog = new StoreSettingsDialog(mainFrame, storeRepository, 
            storeNameLabel, storeLocationLabel, storeContactLabel);
        dialog.setVisible(true);
    }

    // Open employee management dialog
    private void openEmployeeManager() {
        EmployeeManagerDialog dialog = new EmployeeManagerDialog(mainFrame);
        dialog.setVisible(true);
    }

    // Open help and user guide dialog
    private void openHelpDialog() {
        HelpDialog helpDialog = new HelpDialog(mainFrame);
        helpDialog.setVisible(true);
    }

    // Logout confirmation
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(mainFrame,
            "Are you sure you want to logout?", "Confirm Logout",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            mainFrame.showLoginPanel();
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MainApplicationPanel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            mockFrame.loggedInUsername = "Admin";
            mockFrame.loggedInUserRole = "Admin";
            
            MainApplicationPanel panel = new MainApplicationPanel(
                mockFrame, "Admin", "Admin", 
                "Test Store", "123 Main St", "555-1234"
            );
            
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}