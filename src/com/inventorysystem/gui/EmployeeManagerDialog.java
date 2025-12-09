package com.inventorysystem.gui;

import com.inventorysystem.data.UserRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

// Employee manager dialog
public class EmployeeManagerDialog extends JDialog {
    
    private final userFrame mainFrame;
    private final DefaultListModel<String> listModel;
    private final JList<String> employeeList;

    public EmployeeManagerDialog(userFrame mainFrame) {
        super(mainFrame, "Manage Employees", true);
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(mainFrame);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Manage Employees");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Employee list
        listModel = new DefaultListModel<>();
        employeeList = new JList<>(listModel);
        employeeList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        employeeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        employeeList.setFixedCellHeight(45);
        employeeList.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Custom cell renderer with lines between items
        employeeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    new EmptyBorder(10, 15, 10, 15)
                ));
                return label;
            }
        });
        
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top panel with title and selection buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel subTitleLabel = new JLabel("Employees Under Your Account");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subTitleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        topPanel.add(subTitleLabel, BorderLayout.WEST);
        
        // Selection buttons
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        selectionPanel.setBackground(Color.WHITE);
        
        JButton selectAllBtn = new JButton("Select All");
        selectAllBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        selectAllBtn.setPreferredSize(new Dimension(90, 28));
        selectAllBtn.setBackground(new Color(240, 240, 240));
        selectAllBtn.setForeground(UIConstants.TEXT_PRIMARY);
        selectAllBtn.setFocusPainted(false);
        selectAllBtn.setBorderPainted(false);
        selectAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectAllBtn.addActionListener(e -> employeeList.setSelectionInterval(0, listModel.getSize() - 1));
        
        JButton unselectBtn = new JButton("Unselect");
        unselectBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        unselectBtn.setPreferredSize(new Dimension(80, 28));
        unselectBtn.setBackground(new Color(240, 240, 240));
        unselectBtn.setForeground(UIConstants.TEXT_PRIMARY);
        unselectBtn.setFocusPainted(false);
        unselectBtn.setBorderPainted(false);
        unselectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        unselectBtn.addActionListener(e -> employeeList.clearSelection());
        
        selectionPanel.add(selectAllBtn);
        selectionPanel.add(unselectBtn);
        topPanel.add(selectionPanel, BorderLayout.EAST);
        
        listPanel.add(topPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(employeeList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        add(listPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadEmployees();
    }

    // Load employees from database
    private void loadEmployees() {
        try {
            java.util.List<String> employees = new UserRepository().getEmployeesByAdmin(mainFrame.loggedInUserId);
            for (String emp : employees) {
                listModel.addElement(emp);
            }
        } catch (SQLException e) {
            listModel.addElement("Error loading employees: " + e.getMessage());
        }
    }

    // Buttons at bottom
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, UIConstants.PRIMARY_LIGHT),
            new EmptyBorder(8, 15, 8, 15)));

        JButton addBtn = createButton("Add Employee", UIConstants.PRIMARY_COLOR, UIConstants.PRIMARY_DARK);
        addBtn.addActionListener(e -> addEmployee());

        JButton editBtn = createButton("Edit Employee", new Color(52, 152, 219), new Color(41, 128, 185));
        editBtn.addActionListener(e -> editEmployee());

        JButton removeBtn = createButton("Remove Selected", UIConstants.DANGER_COLOR, UIConstants.DANGER_DARK);
        removeBtn.addActionListener(e -> removeEmployee());

        JButton closeBtn = createButton("Close", new Color(149, 165, 166), new Color(127, 140, 141));
        closeBtn.addActionListener(e -> dispose());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(removeBtn);
        panel.add(closeBtn);

        return panel;
    }

    // Create styled button
    private JButton createButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
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

    // Add new employee
    private void addEmployee() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(new EmptyBorder(5, 5, 5, 40));
        
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.setOpaque(false);
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createRigidArea(new Dimension(40, 0)));

        JLabel toggleLabel = new JLabel("👁");
        toggleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        toggleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleLabel.setForeground(new Color(100, 100, 100));
        toggleLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
        toggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getEchoChar() == '\u0000') {
                    passwordField.setEchoChar('•');
                } else {
                    passwordField.setEchoChar('\u0000');
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                toggleLabel.setForeground(UIConstants.PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                toggleLabel.setForeground(new Color(100, 100, 100));
            }
        });
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(toggleLabel, BorderLayout.EAST);

        panel.add(new JLabel("Username:"));
        panel.add(usernamePanel);
        panel.add(new JLabel("Password:"));
        panel.add(passwordPanel);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Employee",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (!username.isEmpty() && !password.isEmpty()) {
                try {
                    boolean success = new UserRepository().addEmployeeUnderAdmin(
                        mainFrame.loggedInUserId, username, password);
                    if (success) {
                        listModel.addElement(username);
                        JOptionPane.showMessageDialog(this, 
                            "Employee '" + username + "' added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Edit selected employee
    private void editEmployee() {
        String selected = employeeList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Step 1: Admin password authentication
        JPanel authPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        authPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPasswordField adminPasswordField = new JPasswordField(15);
        adminPasswordField.setBorder(new EmptyBorder(5, 5, 5, 40));
        
        JLabel adminToggleLabel = new JLabel("👁");
        adminToggleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        adminToggleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminToggleLabel.setForeground(new Color(100, 100, 100));
        adminToggleLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
        adminToggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (adminPasswordField.getEchoChar() == '\u0000') {
                    adminPasswordField.setEchoChar('•');
                } else {
                    adminPasswordField.setEchoChar('\u0000');
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                adminToggleLabel.setForeground(UIConstants.PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                adminToggleLabel.setForeground(new Color(100, 100, 100));
            }
        });
        
        JPanel adminPasswordPanel = new JPanel(new BorderLayout());
        adminPasswordPanel.setOpaque(false);
        adminPasswordPanel.add(adminPasswordField, BorderLayout.CENTER);
        adminPasswordPanel.add(adminToggleLabel, BorderLayout.EAST);
        
        authPanel.add(new JLabel("Enter your password:"));
        authPanel.add(adminPasswordPanel);
        
        int authResult = JOptionPane.showConfirmDialog(this, authPanel, 
            "Authentication Required", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (authResult != JOptionPane.OK_OPTION) {
            return;
        }
        
        String adminPassword = new String(adminPasswordField.getPassword());
        
        // Verify admin password
        try {
            UserRepository userRepo = new UserRepository();
            if (!userRepo.verifyPassword(mainFrame.loggedInUserId, adminPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect password. Access denied.",
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Step 2: Get current employee password and show edit dialog
            String currentPassword = userRepo.getEmployeePassword(selected);
            if (currentPassword == null) {
                JOptionPane.showMessageDialog(this, "Employee not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JTextField usernameField = new JTextField(selected, 15);
            JPasswordField passwordField = new JPasswordField(currentPassword, 15);
            passwordField.setBorder(new EmptyBorder(5, 5, 5, 40));
            
            JLabel toggleLabel = new JLabel("👁");
            toggleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            toggleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            toggleLabel.setForeground(new Color(100, 100, 100));
            toggleLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
            toggleLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (passwordField.getEchoChar() == '\u0000') {
                        passwordField.setEchoChar('•');
                    } else {
                        passwordField.setEchoChar('\u0000');
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    toggleLabel.setForeground(UIConstants.PRIMARY_COLOR);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    toggleLabel.setForeground(new Color(100, 100, 100));
                }
            });
            
            JPanel passwordPanel = new JPanel(new BorderLayout());
            passwordPanel.setOpaque(false);
            passwordPanel.add(passwordField, BorderLayout.CENTER);
            passwordPanel.add(toggleLabel, BorderLayout.EAST);

            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordPanel);

            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Employee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newUsername = usernameField.getText().trim();
                String newPassword = new String(passwordField.getPassword());
                
                if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                    boolean success = userRepo.updateEmployeeCredentials(selected, newUsername, newPassword);
                    
                    if (success) {
                        int index = listModel.indexOf(selected);
                        listModel.set(index, newUsername);
                        JOptionPane.showMessageDialog(this, 
                            "Employee updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Username and password cannot be empty.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove selected employee(s)
    private void removeEmployee() {
        java.util.List<String> selected = employeeList.getSelectedValuesList();
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one employee to remove.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = selected.size() == 1 
            ? "Remove employee '" + selected.get(0) + "'?\nThis will delete their account."
            : "Remove " + selected.size() + " employees?\nThis will delete their accounts.";
            
        int confirm = JOptionPane.showConfirmDialog(this, message,
            "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            StringBuilder errors = new StringBuilder();
            
            for (String emp : selected) {
                try {
                    boolean success = new UserRepository().removeEmployee(emp);
                    if (success) {
                        listModel.removeElement(emp);
                        successCount++;
                    }
                } catch (SQLException ex) {
                    errors.append(emp).append(": ").append(ex.getMessage()).append("\n");
                }
            }
            
            if (successCount > 0) {
                String msg = successCount == 1 
                    ? "Employee removed successfully!"
                    : successCount + " employees removed successfully!";
                JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this, "Errors occurred:\n" + errors.toString(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            EmployeeManagerDialog dialog = new EmployeeManagerDialog(mockFrame);
            dialog.setVisible(true);
        });
    }
}
