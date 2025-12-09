package com.inventorysystem.gui;

import com.inventorysystem.data.StoreRepository;
import com.inventorysystem.data.UserRepository;
import com.inventorysystem.model.Store;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// User frame
public class userFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer;

    public int loggedInUserId;
    public String loggedInUsername;
    public String loggedInUserRole;

    private StoreRepository storeRepository;
    private UserRepository userRepository;

    public userFrame() {
        this.storeRepository = new StoreRepository();
        this.userRepository = new UserRepository();

        setTitle("SmartStock - Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Set icon
        try {
            Image icon = ImageIO.read(new File("src/resources/SmartStock.png"));
            // Scale icon
            java.util.List<Image> icons = new java.util.ArrayList<>();
            icons.add(icon.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            icons.add(icon.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            icons.add(icon.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            icons.add(icon.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
            setIconImages(icons);
        } catch (IOException e) {
            com.inventorysystem.util.DebugLogger.debug("App icon not found, using default");
        }

        // For 1366x768
        setSize(1366, 768);
        setPreferredSize(new Dimension(1366, 768));
        setMinimumSize(new Dimension(1200, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized

        mainContainer = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        SignupPanel signupPanel = new SignupPanel(this);

        mainContainer.add(loginPanel, "login");
        mainContainer.add(signupPanel, "signup");

        add(mainContainer);
        
        // Center for login
        setLocationRelativeTo(null);
    }

    // Login method
    public void handleLoginSuccess(int userId, String username, String role) {
        this.loggedInUserId = userId;
        this.loggedInUsername = username;
        this.loggedInUserRole = role;

        try {
            Store storeToUse;
            
            if (role.equalsIgnoreCase("Employee")) {
                // Get admin store
                int adminId = userRepository.getAdminIdForEmployee(userId);
                if (adminId == -1) {
                    JOptionPane.showMessageDialog(this, 
                        "Error: Your account is not linked to an Admin.\nPlease contact system administrator.", 
                        "Account Error", JOptionPane.ERROR_MESSAGE);
                    showLoginPanel();
                    return;
                }
                storeToUse = storeRepository.getStoreByUserId(adminId);
                if (storeToUse == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Error: Your Admin has not set up a store yet.\nPlease contact your administrator.", 
                        "Store Not Found", JOptionPane.ERROR_MESSAGE);
                    showLoginPanel();
                    return;
                }
            } else {
                // Get/create store
                storeToUse = storeRepository.getStoreByUserId(userId);
                if (storeToUse == null) {
                    storeToUse = promptAndCreateStore(userId);
                    if (storeToUse == null) {
                        showLoginPanel();
                        return;
                    }
                }
            }

            showMainApplication(storeToUse.name(), storeToUse.location(), storeToUse.contact());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching store: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            showLoginPanel();
        }
    }

    // Main method
    public void showMainApplication(String storeName, String storeLocation, String storeContact) {
        MainApplicationPanel mainAppPanel = new MainApplicationPanel(
            this, loggedInUsername, loggedInUserRole, storeName, storeLocation, storeContact
        );
        mainContainer.add(mainAppPanel, "mainApp");
        cardLayout.show(mainContainer, "mainApp");

        // Full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // UI method
    private Store promptAndCreateStore(int userId) throws SQLException {
        JTextField nameField = new JTextField(20);
        nameField.setFont(UIConstants.INPUT_FONT);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, false), // Border
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        JTextField locField = new JTextField(20);
        locField.setFont(UIConstants.INPUT_FONT);
        locField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, false), // Border
            new EmptyBorder(10, 12, 10, 12)
        ));
        
        JTextField contactField = new JTextField(20);
        contactField.setFont(UIConstants.INPUT_FONT);
        contactField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, false), // Border
            new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Store name
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Store Name: *");
        nameLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(nameLabel, gbc);
        
        gbc.gridy = 1;
        panel.add(nameField, gbc);
        
        // Location
        gbc.gridy = 2;
        JLabel locLabel = new JLabel("Location: (Optional)");
        locLabel.setFont(UIConstants.LABEL_FONT);
        locLabel.setForeground(UIConstants.TEXT_SECONDARY);
        panel.add(locLabel, gbc);
        
        gbc.gridy = 3;
        panel.add(locField, gbc);
        
        // Contact
        gbc.gridy = 4;
        JLabel contactLabel = new JLabel("Contact: (Optional)");
        contactLabel.setFont(UIConstants.LABEL_FONT);
        contactLabel.setForeground(UIConstants.TEXT_SECONDARY);
        panel.add(contactLabel, gbc);
        
        gbc.gridy = 5;
        panel.add(contactField, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(this, panel, "Set Up Your Store", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                String newLocation = locField.getText().trim();
                String newContact = contactField.getText().trim();

                if (newName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Store Name cannot be empty.", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                boolean success = storeRepository.addStore(userId, newName, newLocation, newContact);

                if (success) {
                    Store newStore = storeRepository.getStoreByUserId(userId);
                    if (newStore != null) {
                        return newStore;
                    } else {
                        throw new SQLException("Failed to retrieve store after creation.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add store. Please try again.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Cancelled
                JOptionPane.showMessageDialog(this, 
                    "Store setup is required to use the system.", 
                    "Setup Required", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        }
    }

    // Cancel method
    public void showLoginPanel() {
        for (Component comp : mainContainer.getComponents()) {
            if (comp instanceof MainApplicationPanel) {
                mainContainer.remove(comp);
            }
        }
        
        this.loggedInUserId = 0;
        this.loggedInUsername = null;
        this.loggedInUserRole = null;

        cardLayout.show(mainContainer, "login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized
        setLocationRelativeTo(null);
    }

    // End method
    public void showSignupPanel() {
        cardLayout.show(mainContainer, "signup");
    }
}