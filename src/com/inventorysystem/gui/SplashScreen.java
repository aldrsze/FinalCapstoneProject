package com.inventorysystem.gui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public SplashScreen() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
        
        // Logo/Title area
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(400, 120));
        
        JLabel titleLabel = new JLabel("SmartStock");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Inventory Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContent.add(Box.createVerticalGlue());
        headerContent.add(titleLabel);
        headerContent.add(Box.createRigidArea(new Dimension(0, 5)));
        headerContent.add(subtitleLabel);
        headerContent.add(Box.createVerticalGlue());
        
        headerPanel.add(headerContent);
        
        // Progress area
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(320, 20));
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(41, 128, 185));
        progressBar.setBackground(new Color(236, 240, 241));
        
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
