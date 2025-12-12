package com.inventorysystem.gui;

import com.inventorysystem.data.StoreRepository;
import com.inventorysystem.data.UserRepository;
import com.inventorysystem.model.Store;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

// Store settings dialog
public class StoreSettingsDialog extends JDialog {
    
    private final userFrame mainFrame;
    private final StoreRepository storeRepo;
    private final JLabel storeNameLabel;
    private final JLabel storeLocationLabel;
    private final JLabel storeContactLabel;
    
    // Store profile
    private JTextField nameField;
    private JTextField locationField;
    private JTextField contactField;
    private JTextField markupField;
    
    // Account settings
    private JTextField currentUsernameField;
    private JPasswordField verifyPasswordField;
    private JTextField newUsernameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public StoreSettingsDialog(userFrame mainFrame, StoreRepository storeRepo,
                              JLabel nameLabel, JLabel locationLabel, JLabel contactLabel) {
        super(mainFrame, "Store Settings", true);
        this.mainFrame = mainFrame;
        this.storeRepo = storeRepo;
        this.storeNameLabel = nameLabel;
        this.storeLocationLabel = locationLabel;
        this.storeContactLabel = contactLabel;

        setupDialog();
        loadStoreData();
    }

    // Setup tabs
    private void setupDialog() {
        setLayout(new BorderLayout());
        
        // Window size
        setSize(800, 800);
        setLocationRelativeTo(mainFrame);
        setResizable(true);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(UIConstants.TITLE_FONT.deriveFont(Font.BOLD, 26f));
        titleLabel.setForeground(UIConstants.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Tabs: profile, username, password
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.BUTTON_FONT.deriveFont(Font.BOLD, 14f));
        tabbedPane.setBackground(UIConstants.BACKGROUND_COLOR);
        tabbedPane.setBorder(new EmptyBorder(15, 15, 15, 15));

        tabbedPane.addTab("  Store Profile  ", createStoreProfilePanel());
        tabbedPane.addTab("  Change Username  ", createUsernameChangePanel());
        tabbedPane.addTab("  Change Password  ", createPasswordChangePanel());
        tabbedPane.addTab("  Delete Account  ", createDeleteAccountPanel());

        add(tabbedPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    // Get store data
    private void loadStoreData() {
        try {
            Store currentStore = storeRepo.getStoreByUserId(mainFrame.loggedInUserId);
            if (currentStore == null) {
                JOptionPane.showMessageDialog(this, "Could not find store profile.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            // Store info
            nameField.setText(currentStore.name());
            locationField.setText(currentStore.location());
            contactField.setText(currentStore.contact());

            // Markup
            UserRepository userRepo = new UserRepository();
            double markup = userRepo.getDefaultMarkup(mainFrame.loggedInUserId);
            markupField.setText(String.valueOf(markup));
            
            // Username
            currentUsernameField.setText(mainFrame.loggedInUsername);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    // Tab 1: profile
    private JPanel createStoreProfilePanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        outerPanel.setBorder(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.FORM_COLOR);
        panel.setBorder(new EmptyBorder(35, 40, 35, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 5, 12, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // Section
        JLabel sectionTitle = new JLabel("Store Information");
        sectionTitle.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD, 18f));
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        gbc.insets = new Insets(0, 5, 25, 5);
        panel.add(sectionTitle, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(12, 5, 12, 5);

        // Name
        addLabel(panel, gbc, "Store Name:");
        nameField = addTextField(panel, gbc);

        // Location
        addLabel(panel, gbc, "Location:");
        locationField = addTextField(panel, gbc);

        // Contact
        addLabel(panel, gbc, "Contact Number:");
        contactField = addTextField(panel, gbc);
        
        // Separator
        gbc.gridy++;
        gbc.insets = new Insets(20, 5, 20, 5);
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        panel.add(separator, gbc);
        
        // Pricing
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 15, 5);
        JLabel pricingTitle = new JLabel("Pricing Settings");
        pricingTitle.setFont(UIConstants.LABEL_FONT_BOLD_16);
        pricingTitle.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(pricingTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(8, 5, 4, 5);
        JLabel markupLabel = new JLabel("Default Markup Percentage (%)");
        markupLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        markupLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(markupLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 6, 5);
        markupField = new JTextField();
        markupField.setFont(UIConstants.INPUT_FONT);
        markupField.setPreferredSize(new Dimension(550, 40));
        markupField.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(10, 12, 10, 12)));
        panel.add(markupField, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(2, 5, 12, 5);
        JLabel markupHelp = new JLabel("<html><i>Used when adding products without specific markup</i></html>");
        markupHelp.setFont(UIConstants.LABEL_FONT_PLAIN_12);
        markupHelp.setForeground(UIConstants.TEXT_SECONDARY);
        panel.add(markupHelp, gbc);
        
        outerPanel.add(panel, BorderLayout.CENTER);
        return outerPanel;
    }
    
    // Tab 2: username
    private JPanel createUsernameChangePanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        outerPanel.setBorder(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.FORM_COLOR);
        panel.setBorder(new EmptyBorder(40, 45, 40, 45));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // Section
        JLabel sectionTitle = new JLabel("Change Username");
        sectionTitle.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD, 20f));
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(sectionTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 15, 5);
        JLabel description = new JLabel("<html>Update your username. You'll need to verify with your password.</html>");
        description.setFont(UIConstants.LABEL_FONT_PLAIN_13);
        description.setForeground(UIConstants.TEXT_SECONDARY);
        panel.add(description, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 5, 6, 5);
        JLabel currentLabel = new JLabel("Current Username");
        currentLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        currentLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(currentLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 15, 5);
        currentUsernameField = new JTextField();
        currentUsernameField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        currentUsernameField.setEditable(false);
        currentUsernameField.setBackground(UIConstants.BACKGROUND_COLOR);
        currentUsernameField.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(12, 15, 12, 15)));
        currentUsernameField.setPreferredSize(new Dimension(550, 45));
        panel.add(currentUsernameField, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 6, 5);
        JLabel newUsernameLabel = new JLabel("New Username");
        newUsernameLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        newUsernameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(newUsernameLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 15, 5);
        newUsernameField = new JTextField();
        newUsernameField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        newUsernameField.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(12, 15, 12, 15)));
        newUsernameField.setPreferredSize(new Dimension(550, 45));
        panel.add(newUsernameField, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 6, 5);
        JLabel verifyLabel = new JLabel("Current Password");
        verifyLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        verifyLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(verifyLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 20, 5);
        
        verifyPasswordField = new JPasswordField();
        verifyPasswordField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        verifyPasswordField.setPreferredSize(new Dimension(490, 45));
        verifyPasswordField.setMaximumSize(new Dimension(490, 45));
        verifyPasswordField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel toggleVerifyBtn = createPasswordToggleButton(verifyPasswordField);
        toggleVerifyBtn.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JPanel verifyPasswordPanel = new JPanel(new BorderLayout());
        verifyPasswordPanel.setOpaque(false);
        verifyPasswordPanel.setPreferredSize(new Dimension(490, 45));
        verifyPasswordPanel.setMaximumSize(new Dimension(490, 45));
        verifyPasswordPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)));
        verifyPasswordPanel.add(verifyPasswordField, BorderLayout.CENTER);
        verifyPasswordPanel.add(toggleVerifyBtn, BorderLayout.EAST);
        panel.add(verifyPasswordPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 5, 10, 5);
        JButton changeUsernameBtn = new JButton("Save Changes");
        changeUsernameBtn.setFont(UIConstants.BUTTON_FONT.deriveFont(Font.BOLD, 14f));
        changeUsernameBtn.setPreferredSize(new Dimension(200, 42));
        changeUsernameBtn.setBackground(UIConstants.PRIMARY_COLOR);
        changeUsernameBtn.setForeground(Color.WHITE);
        changeUsernameBtn.setFocusPainted(false);
        changeUsernameBtn.setBorderPainted(false);
        changeUsernameBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeUsernameBtn.addActionListener(e -> changeUsername());
        changeUsernameBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { changeUsernameBtn.setBackground(UIConstants.PRIMARY_DARK); }
            public void mouseExited(MouseEvent e) { changeUsernameBtn.setBackground(UIConstants.PRIMARY_COLOR); }
        });
        panel.add(changeUsernameBtn, gbc);
        
        outerPanel.add(panel, BorderLayout.CENTER);
        return outerPanel;
    }
    
    // Tab 4: password
    private JPanel createPasswordChangePanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        outerPanel.setBorder(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.FORM_COLOR);
        panel.setBorder(new EmptyBorder(40, 45, 40, 45));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // Section
        JLabel sectionTitle = new JLabel("Change Password");
        sectionTitle.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD, 20f));
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(sectionTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 15, 5);
        JLabel description = new JLabel("<html>Your password must be at least 4 characters long.</html>");
        description.setFont(UIConstants.LABEL_FONT_PLAIN_13);
        description.setForeground(UIConstants.TEXT_SECONDARY);
        panel.add(description, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 5, 6, 5);
        JLabel currentPwLabel = new JLabel("Current Password");
        currentPwLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        currentPwLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(currentPwLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 15, 5);
        
        currentPasswordField = new JPasswordField();
        currentPasswordField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        currentPasswordField.setPreferredSize(new Dimension(490, 45));
        currentPasswordField.setMaximumSize(new Dimension(490, 45));
        currentPasswordField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel toggleCurrentBtn = createPasswordToggleButton(currentPasswordField);
        toggleCurrentBtn.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JPanel currentPasswordPanel = new JPanel(new BorderLayout());
        currentPasswordPanel.setOpaque(false);
        currentPasswordPanel.setPreferredSize(new Dimension(490, 45));
        currentPasswordPanel.setMaximumSize(new Dimension(490, 45));
        currentPasswordPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)));
        currentPasswordPanel.add(currentPasswordField, BorderLayout.CENTER);
        currentPasswordPanel.add(toggleCurrentBtn, BorderLayout.EAST);
        panel.add(currentPasswordPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 6, 5);
        JLabel newPwLabel = new JLabel("New Password");
        newPwLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        newPwLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(newPwLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 15, 5);
        
        newPasswordField = new JPasswordField();
        newPasswordField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        newPasswordField.setPreferredSize(new Dimension(490, 45));
        newPasswordField.setMaximumSize(new Dimension(490, 45));
        newPasswordField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel toggleNewBtn = createPasswordToggleButton(newPasswordField);
        toggleNewBtn.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JPanel newPasswordPanel = new JPanel(new BorderLayout());
        newPasswordPanel.setOpaque(false);
        newPasswordPanel.setPreferredSize(new Dimension(490, 45));
        newPasswordPanel.setMaximumSize(new Dimension(490, 45));
        newPasswordPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)));
        newPasswordPanel.add(newPasswordField, BorderLayout.CENTER);
        newPasswordPanel.add(toggleNewBtn, BorderLayout.EAST);
        panel.add(newPasswordPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 6, 5);
        JLabel confirmPwLabel = new JLabel("Confirm New Password");
        confirmPwLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        confirmPwLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(confirmPwLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 20, 5);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        confirmPasswordField.setPreferredSize(new Dimension(490, 45));
        confirmPasswordField.setMaximumSize(new Dimension(490, 45));
        confirmPasswordField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel toggleConfirmBtn = createPasswordToggleButton(confirmPasswordField);
        toggleConfirmBtn.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setOpaque(false);
        confirmPasswordPanel.setPreferredSize(new Dimension(490, 45));
        confirmPasswordPanel.setMaximumSize(new Dimension(490, 45));
        confirmPasswordPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)));
        confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);
        confirmPasswordPanel.add(toggleConfirmBtn, BorderLayout.EAST);
        panel.add(confirmPasswordPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 5, 10, 5);
        JButton changePasswordBtn = new JButton("Save Changes");
        changePasswordBtn.setFont(UIConstants.BUTTON_FONT.deriveFont(Font.BOLD, 14f));
        changePasswordBtn.setPreferredSize(new Dimension(200, 42));
        changePasswordBtn.setBackground(UIConstants.PRIMARY_COLOR);
        changePasswordBtn.setForeground(Color.WHITE);
        changePasswordBtn.setFocusPainted(false);
        changePasswordBtn.setBorderPainted(false);
        changePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePasswordBtn.addActionListener(e -> changePassword());
        changePasswordBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { changePasswordBtn.setBackground(UIConstants.PRIMARY_DARK); }
            public void mouseExited(MouseEvent e) { changePasswordBtn.setBackground(UIConstants.PRIMARY_COLOR); }
        });
        panel.add(changePasswordBtn, gbc);
        
        outerPanel.add(panel, BorderLayout.CENTER);
        return outerPanel;
    }

    // Password toggle
    private JLabel createPasswordToggleButton(JPasswordField passwordField) {
        JLabel toggleBtn = new JLabel("\uD83D\uDC41");
        toggleBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        toggleBtn.setForeground(UIConstants.TEXT_SECONDARY);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setHorizontalAlignment(SwingConstants.CENTER);
        toggleBtn.setVerticalAlignment(SwingConstants.CENTER);
        toggleBtn.setToolTipText("Show/Hide Password");
        
        toggleBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getEchoChar() == '\u0000') {
                    passwordField.setEchoChar('•');
                    toggleBtn.setForeground(UIConstants.TEXT_SECONDARY);
                } else {
                    passwordField.setEchoChar('\u0000');
                    toggleBtn.setForeground(UIConstants.PRIMARY_COLOR);
                }
            }
            public void mouseEntered(MouseEvent e) {
                toggleBtn.setForeground(UIConstants.PRIMARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                if (passwordField.getEchoChar() != '\u0000') {
                    toggleBtn.setForeground(UIConstants.TEXT_SECONDARY);
                }
            }
        });
        
        return toggleBtn;
    }
    
    // Add label
    private void addLabel(JPanel panel, GridBagConstraints gbc, String text) {
        gbc.gridy++;
        gbc.insets = new Insets(8, 5, 4, 5);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(label, gbc);
    }

    // Add text field
    private JTextField addTextField(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 12, 5);
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(550, 40));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 12, 10, 12)));
        panel.add(field, gbc);
        return field;
    }

    // Save/Cancel
    // Buttons at bottom
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        panel.setBackground(UIConstants.FORM_COLOR);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(2, 0, 0, 0, UIConstants.PRIMARY_LIGHT),
            new EmptyBorder(8, 15, 8, 15)));
            
        JButton cancelBtn = createButton("Cancel", UIConstants.GREY_COLOR, UIConstants.TEXT_SUBTITLE);
        cancelBtn.setForeground(UIConstants.TEXT_PRIMARY); // Make text visible
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = createButton("Save Changes", UIConstants.SUCCESS_COLOR, UIConstants.SUCCESS_DARK);
        saveBtn.addActionListener(e -> saveChanges());

        panel.add(cancelBtn);
        panel.add(saveBtn);

        return panel;
    }

    // Styled button
    private JButton createButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });

        return button;
    }
    
    // Change username
    private void changeUsername() {
        String newUsername = newUsernameField.getText().trim();
        String verifyPassword = new String(verifyPasswordField.getPassword()).trim();
        
        // Validate
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a new username.",
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (verifyPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your current password.",
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (newUsername.equals(mainFrame.loggedInUsername)) {
            JOptionPane.showMessageDialog(this, "New username is the same as current username.",
                "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            UserRepository userRepo = new UserRepository();
            
            // Check password
            if (!userRepo.verifyPassword(mainFrame.loggedInUserId, verifyPassword)) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.",
                    "Verification Failed", JOptionPane.ERROR_MESSAGE);
                verifyPasswordField.setText("");
                return;
            }
            
            // Update username
            userRepo.updateUsername(mainFrame.loggedInUserId, newUsername);
            mainFrame.loggedInUsername = newUsername;
            
            JOptionPane.showMessageDialog(this, "Username changed successfully!\nYou will be logged out for security.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            mainFrame.showLoginPanel();
            
        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.",
                    "Username Taken", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error changing username: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Change password
    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        
        // Validate
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all password fields.",
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.",
                "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.setText("");
            return;
        }
        
        if (newPassword.length() < 4) {
            JOptionPane.showMessageDialog(this, "New password must be at least 4 characters long.",
                "Password Too Short", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentPassword.equals(newPassword)) {
            JOptionPane.showMessageDialog(this, "New password must be different from current password.",
                "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            UserRepository userRepo = new UserRepository();
            
            // Check password
            if (!userRepo.verifyPassword(mainFrame.loggedInUserId, currentPassword)) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.",
                    "Verification Failed", JOptionPane.ERROR_MESSAGE);
                currentPasswordField.setText("");
                return;
            }
            
            // Update password
            userRepo.updatePassword(mainFrame.loggedInUserId, newPassword);
            
            JOptionPane.showMessageDialog(this, "Password changed successfully!\nYou will be logged out for security.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            mainFrame.showLoginPanel();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error changing password: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tab 4: delete
    private JPanel createDeleteAccountPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        outerPanel.setBorder(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.FORM_COLOR);
        panel.setBorder(new EmptyBorder(40, 45, 40, 45));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // Section
        JLabel sectionTitle = new JLabel("Delete Account");
        sectionTitle.setFont(UIConstants.SUBTITLE_FONT.deriveFont(Font.BOLD, 20f));
        sectionTitle.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(sectionTitle, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 10, 5);
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descPanel.setBackground(Color.WHITE);
        
        JLabel warningEmoji = new JLabel("\u26A0 ");
        warningEmoji.setFont(UIConstants.FONT_EMOJI_SMALL);
        warningEmoji.setForeground(new Color(220, 53, 69));
        descPanel.add(warningEmoji);
        
        JLabel description = new JLabel("<html><b>Warning:</b> This action cannot be undone. All your data will be permanently deleted.</html>");
        description.setFont(UIConstants.LABEL_FONT_PLAIN_13);
        description.setForeground(new Color(220, 53, 69));
        descPanel.add(description);
        
        panel.add(descPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 5, 10, 5);
        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.Y_AXIS));
        warningPanel.setBackground(new Color(255, 243, 243));
        warningPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.DANGER_COLOR, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel warningText = new JLabel("Deleting your account will permanently remove:");
        warningText.setFont(UIConstants.LABEL_FONT_PLAIN_13);
        warningText.setForeground(UIConstants.TEXT_SECONDARY);
        warningText.setAlignmentX(Component.LEFT_ALIGNMENT);
        warningPanel.add(warningText);
        warningPanel.add(Box.createVerticalStrut(8));
        
        String[] warnings = {
            "• All store information and settings",
            "• All products and inventory records",
            "• All sales and transaction history",
            "• All employee accounts and access"
        };
        
        for (String warn : warnings) {
            JLabel item = new JLabel(warn);
            item.setFont(UIConstants.LABEL_FONT_PLAIN_13);
            item.setForeground(UIConstants.TEXT_SECONDARY);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            warningPanel.add(item);
            if (!warn.equals(warnings[warnings.length - 1])) {
                warningPanel.add(Box.createVerticalStrut(4));
            }
        }
        
        panel.add(warningPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(20, 5, 6, 5);
        JLabel passwordLabel = new JLabel("Confirm Your Password");
        passwordLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        passwordLabel.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(passwordLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 5, 20, 5);
        
        JPasswordField deletePasswordField = new JPasswordField();
        deletePasswordField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        deletePasswordField.setPreferredSize(new Dimension(490, 45));
        deletePasswordField.setMaximumSize(new Dimension(490, 45));
        deletePasswordField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel toggleBtn = createPasswordToggleButton(deletePasswordField);
        toggleBtn.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setPreferredSize(new Dimension(490, 45));
        passwordPanel.setMaximumSize(new Dimension(490, 45));
        passwordPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)));
        passwordPanel.add(deletePasswordField, BorderLayout.CENTER);
        passwordPanel.add(toggleBtn, BorderLayout.EAST);
        panel.add(passwordPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 5, 10, 5);
        JButton deleteButton = new JButton("Delete My Account");
        deleteButton.setFont(UIConstants.BUTTON_FONT.deriveFont(Font.BOLD, 14f));
        deleteButton.setPreferredSize(new Dimension(200, 42));
        deleteButton.setBackground(UIConstants.DANGER_COLOR);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> handleDeleteAccount(deletePasswordField));
        deleteButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { deleteButton.setBackground(UIConstants.DANGER_DARK); }
            public void mouseExited(MouseEvent e) { deleteButton.setBackground(UIConstants.DANGER_COLOR); }
        });
        panel.add(deleteButton, gbc);
        
        outerPanel.add(panel, BorderLayout.CENTER);
        return outerPanel;
    }

    // Delete account
    private void handleDeleteAccount(JPasswordField passwordField) {
        String password = new String(passwordField.getPassword());
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your password to confirm.",
                "Password Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you absolutely sure?\n\nThis will permanently delete your account and all associated data.\n\n" +
            "This action CANNOT be undone!",
            "Final Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            passwordField.setText("");
            return;
        }

        try {
            UserRepository userRepo = new UserRepository();
            
            // Check password
            if (!userRepo.verifyPassword(mainFrame.loggedInUserId, password)) {
                JOptionPane.showMessageDialog(this, "Incorrect password. Account deletion cancelled.",
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                return;
            }

            // Delete account
            boolean deleted = userRepo.deleteUser(mainFrame.loggedInUserId);
            
            if (deleted) {
                JOptionPane.showMessageDialog(this,
                    "Your account has been permanently deleted.\n\nYou will now be logged out.",
                    "Account Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                mainFrame.showLoginPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete account. Please try again or contact support.",
                    "Deletion Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Save changes
    private void saveChanges() {
        String newName = nameField.getText().trim();
        String newLocation = locationField.getText().trim();
        String newContact = contactField.getText().trim();

        // Name required
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Store Name cannot be empty.",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Save store
            storeRepo.updateStore(mainFrame.loggedInUserId, newName, newLocation, newContact);
            
            // Update labels
            storeNameLabel.setText(newName);
            storeLocationLabel.setText(newLocation.isEmpty() ? "No location" : newLocation);
            storeContactLabel.setText(newContact.isEmpty() ? "No contact" : newContact);

            // Save markup
            double newMarkup = Double.parseDouble(markupField.getText().trim());
            if (newMarkup < 0) {
                JOptionPane.showMessageDialog(this, "Markup cannot be negative.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            UserRepository userRepo = new UserRepository();
            userRepo.updateDefaultMarkup(mainFrame.loggedInUserId, newMarkup);

            JOptionPane.showMessageDialog(this, "Settings saved successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid markup percentage.",
                "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Main test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            mockFrame.loggedInUsername = "Admin";
            
            JLabel nameLabel = new JLabel();
            JLabel locationLabel = new JLabel();
            JLabel contactLabel = new JLabel();
            
            StoreSettingsDialog dialog = new StoreSettingsDialog(
                mockFrame, new com.inventorysystem.data.StoreRepository(),
                nameLabel, locationLabel, contactLabel
            );
            dialog.setVisible(true);
        });
    }
}
