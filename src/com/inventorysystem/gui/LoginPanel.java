package com.inventorysystem.gui;

import com.inventorysystem.data.UserRepository;
import com.inventorysystem.model.User;
import com.inventorysystem.util.SoundUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

// Login panel
public class LoginPanel extends JPanel {

    private userFrame mainFrame;
    private UserRepository userRepository;
    private BufferedImage backgroundImage;

    public LoginPanel(userFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userRepository = new UserRepository();
        
        // Load bg image
        try {
            backgroundImage = ImageIO.read(new File("src/resources/login_bg.png"));
        } catch (IOException e) {
            com.inventorysystem.util.DebugLogger.debug("Background image not found, using default background");
        }

        setLayout(new GridBagLayout());

        // Login form - Exact match to SignupPanel layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.OVERLAY_BACKGROUND);
        formPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(50, 60, 50, 60)
        ));
        formPanel.setPreferredSize(new Dimension(500, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // App name (Brand)
        JLabel brandLabel = new JLabel("SmartStock", SwingConstants.CENTER);
        brandLabel.setFont(UIConstants.FONT_BRAND);
        brandLabel.setForeground(UIConstants.PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(brandLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Inventory Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(UIConstants.LABEL_FONT);
        subtitleLabel.setForeground(UIConstants.TEXT_SUBTITLE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitleLabel, gbc);

        // Welcome Text
        JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(UIConstants.TITLE_FONT.deriveFont(Font.BOLD, 24f));
        welcomeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(welcomeLabel, gbc);

        // Username Label
        gbc.gridy = 3;
        gbc.insets = new Insets(12, 0, 5, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        userLabel.setForeground(UIConstants.TEXT_PRIMARY);
        formPanel.add(userLabel, gbc);

        // Username Input
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 18, 0);
        JTextField userField = new JTextField(20);
        userField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        userField.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(12, 15, 12, 15)
        ));
        formPanel.add(userField, gbc);

        // Password Label
        gbc.gridy = 5;
        gbc.insets = new Insets(12, 0, 5, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UIConstants.LABEL_BOLD_FONT);
        passLabel.setForeground(UIConstants.TEXT_PRIMARY);
        formPanel.add(passLabel, gbc);

        // Password Input
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 25, 0);
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(UIConstants.INPUT_FONT.deriveFont(15f));
        passField.setBorder(new EmptyBorder(12, 15, 12, 45));
        
        JLabel passToggle = new JLabel("👁");
        passToggle.setFont(UIConstants.FONT_EMOJI_SMALL);
        passToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passToggle.setForeground(UIConstants.TEXT_SECONDARY);
        passToggle.setBorder(new EmptyBorder(0, 0, 0, 10));
        passToggle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (passField.getEchoChar() == '\u0000') {
                    passField.setEchoChar('•');
                    passToggle.setForeground(UIConstants.TEXT_SECONDARY);
                } else {
                    passField.setEchoChar('\u0000');
                    passToggle.setForeground(UIConstants.PRIMARY_COLOR);
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                passToggle.setForeground(UIConstants.PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (passField.getEchoChar() != '\u0000') {
                    passToggle.setForeground(UIConstants.TEXT_SECONDARY);
                }
            }
        });
        
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setBackground(UIConstants.FORM_COLOR);
        passPanel.setBorder(new CompoundBorder(
            UIConstants.BORDER_INPUT,
            new EmptyBorder(0, 0, 0, 0)
        ));
        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(passToggle, BorderLayout.EAST);
        
        formPanel.add(passPanel, gbc);

        // Buttons
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 0, 0);
        JPanel buttonPanel = createButtonPanel(userField, passField);
        buttonPanel.setBackground(UIConstants.FORM_COLOR); // Match form background (white)
        formPanel.add(buttonPanel, gbc);
        
        // Enter key listener
        java.awt.event.KeyAdapter enterKeyListener = new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    handleLogin(userField, passField);
                }
            }
        };
        userField.addKeyListener(enterKeyListener);
        passField.addKeyListener(enterKeyListener);

        add(formPanel);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(UIConstants.BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Button Panel - Matches SignupPanel GridLayout
    private JPanel createButtonPanel(JTextField userField, JPasswordField passField) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(UIConstants.FORM_COLOR);

        // Using standard sizes from SignupPanel (150x45)
        JButton loginButton = createStyledButton("Login", UIConstants.PRIMARY_COLOR, UIConstants.PRIMARY_DARK);
        JButton signupButton = createStyledButton("Sign Up", UIConstants.SUCCESS_GREEN, UIConstants.SUCCESS_GREEN_HOVER);

        panel.add(loginButton);
        panel.add(signupButton);

        loginButton.addActionListener(e -> handleLogin(userField, passField));
        signupButton.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            mainFrame.showSignupPanel();
        });

        return panel;
    }

    // Styled button - Matches SignupPanel dimensions
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT.deriveFont(15f));
        button.setPreferredSize(new Dimension(150, 45)); // Same as SignupPanel
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void handleLogin(JTextField userField, JPasswordField passField) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStyledMessage("Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Startup splash = new Startup();
            splash.updateProgress(30, "Authenticating...");
            splash.showSplash();

            SwingUtilities.invokeLater(() -> {
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            });

            User user = userRepository.login(username, password);
            if (user != null) {
                splash.updateProgress(80, "Login successful!");
                SwingUtilities.invokeLater(() -> {
                    splash.closeSplash();
                    mainFrame.handleLoginSuccess(user.userId(), user.username(), user.role());
                    showWelcomeDialog(user);
                });
            } else {
                splash.closeSplash();
                showStyledMessage("Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            showStyledMessage("Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", UIConstants.LABEL_FONT);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void showWelcomeDialog(User user) {
        JDialog welcomeDialog = new JDialog(mainFrame, true);
        welcomeDialog.setUndecorated(true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setSize(UIConstants.DIALOG_LOGIN_WELCOME);
        welcomeDialog.setLocationRelativeTo(mainFrame);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.FORM_COLOR);
        mainPanel.setBorder(new LineBorder(UIConstants.BORDER_COLOR, 3));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel iconLabel = new JLabel();
        iconLabel.setFont(UIConstants.FONT_ICON_LARGE);
        iconLabel.setForeground(UIConstants.SUCCESS_COLOR);
        iconLabel.setText("\u2713");
        mainPanel.add(iconLabel, gbc);

        gbc.gridy++;
        JLabel titleLabel = new JLabel("Login Successful");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 20, 20, 20);
        JLabel userLabel = new JLabel("Welcome, " + user.username());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userLabel.setForeground(UIConstants.TEXT_SECONDARY);
        mainPanel.add(userLabel, gbc);
        
        welcomeDialog.add(mainPanel, BorderLayout.CENTER);
        SoundUtil.play("success.wav");

        Timer timer = new Timer(1500, e -> welcomeDialog.dispose());
        timer.setRepeats(false);
        timer.start();

        welcomeDialog.setVisible(true);
    }
}