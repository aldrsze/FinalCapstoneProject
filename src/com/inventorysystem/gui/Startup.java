package com.inventorysystem.gui;

import javax.swing.*;
import java.awt.*;

public class Startup extends JWindow {
    
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public Startup() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConstants.FORM_COLOR);
        content.setBorder(BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR, 2));
        
        // Logo/title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(400, 120));
        
        JLabel titleLabel = new JLabel("SmartStock");
        titleLabel.setFont(UIConstants.FONT_BRAND);
        titleLabel.setForeground(UIConstants.WHITE);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Inventory Management System");
        subtitleLabel.setFont(UIConstants.LABEL_FONT);
        subtitleLabel.setForeground(UIConstants.TEXT_LIGHT);
        
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(UIConstants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContent.add(Box.createVerticalGlue());
        headerContent.add(titleLabel);
        headerContent.add(Box.createRigidArea(new Dimension(0, 5)));
        headerContent.add(subtitleLabel);
        headerContent.add(Box.createVerticalGlue());
        
        headerPanel.add(headerContent);
        
        // Progress
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(UIConstants.FORM_COLOR);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(UIConstants.LABEL_FONT_PLAIN_12);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(320, 20));
        progressBar.setStringPainted(false);
        progressBar.setForeground(UIConstants.PRIMARY_COLOR);
        progressBar.setBackground(UIConstants.TEXT_LIGHT);
        
        progressPanel.add(statusLabel);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        progressPanel.add(progressBar);
        
        content.add(headerPanel, BorderLayout.NORTH);
        content.add(progressPanel, BorderLayout.CENTER);
        
        setContentPane(content);
        setSize(400, 220);
        setLocationRelativeTo(null);
    }
    
    public void updateProgress(int value, String status) {
        progressBar.setValue(value);
        statusLabel.setText(status);
    }
    
    public void showSplash() {
        setVisible(true);
    }
    
    public void closeSplash() {
        setVisible(false);
        dispose();
    }
}
