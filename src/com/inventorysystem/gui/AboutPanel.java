package com.inventorysystem.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class AboutPanel extends JPanel {

    public AboutPanel(userFrame mockFrame) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.WHITE);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(UIConstants.WHITE);
        mainContent.setBorder(new EmptyBorder(30, 20, 30, 20));

        // About section
        mainContent.add(createAboutSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContent.add(createDivider());
        mainContent.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Team section
        mainContent.add(createTeamSection());
        mainContent.add(Box.createRigidArea(new Dimension(0, 40)));
        mainContent.add(createFooter());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createAboutSection() {
        JPanel aboutSection = new JPanel();
        aboutSection.setLayout(new BoxLayout(aboutSection, BoxLayout.Y_AXIS));
        aboutSection.setBackground(UIConstants.WHITE);
        aboutSection.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        JLabel titleLabel = new JLabel("About Us", SwingConstants.CENTER);
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutSection.add(titleLabel);
        aboutSection.add(Box.createRigidArea(new Dimension(0, 30)));

        aboutSection.add(createParagraph(
            "We are a dedicated team focused on helping small businesses operate more efficiently. Through our " +
            "comprehensive inventory management system, we strive to enable small businesses to monitor sales, control inventory, " +
            "and make informed decisions—all while staying within budget."
        ));
        aboutSection.add(Box.createRigidArea(new Dimension(0, 20)));

        aboutSection.add(createParagraph(
            "Developed with dedication for growing enterprises, SmartStock adapts to your business needs, " +
            "combining user-friendly design, powerful features, and reliable performance."
        ));
        aboutSection.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel copyrightLabel = new JLabel("© 2025 SmartStock Inventory System. All rights reserved", SwingConstants.CENTER);
        copyrightLabel.setFont(UIConstants.LABEL_FONT);
        copyrightLabel.setForeground(UIConstants.TEXT_SECONDARY);
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutSection.add(copyrightLabel);

        return aboutSection;
    }

    private JSeparator createDivider() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setForeground(UIConstants.BORDER_COLOR);
        return separator;
    }

    private JPanel createTeamSection() {
        JPanel teamSection = new JPanel();
        teamSection.setLayout(new BoxLayout(teamSection, BoxLayout.Y_AXIS));
        teamSection.setBackground(UIConstants.WHITE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(UIConstants.WHITE);
        JLabel teamTitle = new JLabel("Development Team");
        teamTitle.setFont(UIConstants.TITLE_FONT);
        teamTitle.setForeground(UIConstants.PRIMARY_COLOR);
        headerPanel.add(teamTitle);
        teamSection.add(headerPanel);
        teamSection.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel subHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subHeaderPanel.setBackground(UIConstants.WHITE);
        JLabel subTitleLabel = new JLabel("BSIT-1B | Group #3");
        subTitleLabel.setFont(UIConstants.LABEL_FONT_PLAIN_15);
        subTitleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        subHeaderPanel.add(subTitleLabel);
        teamSection.add(subHeaderPanel);
        teamSection.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel membersContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        membersContainer.setBackground(UIConstants.WHITE);
        membersContainer.add(createMemberCard("George Harold A. Alcantara", "Project Manager / Documentation Writer", "GHA", UIConstants.PRIMARY_DARK));
        membersContainer.add(createMemberCard("Aldrin Miguel A. Jariel", "System Analyst / Dev / QA / Documentation Writer", "AMA", UIConstants.ACCENT_COLOR));
        membersContainer.add(createMemberCard("John Christoper A. Perez", "UI/UX Designer / Documentation Writer", "JCA", UIConstants.SUCCESS_COLOR));
        membersContainer.add(createMemberCard("Ron Paulo G. Angeles", "Documentation Writer", "RPG", UIConstants.WARNING_COLOR));
        membersContainer.add(createMemberCard("Matthew Dane D. Calangian", "Documentation Writer", "MDD", UIConstants.WARNING_COLOR));
        teamSection.add(membersContainer);

        return teamSection;
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(UIConstants.WHITE);
        JLabel footerLabel = new JLabel("© 2025 SmartStock Development Team. All Rights Reserved.");
        footerLabel.setFont(UIConstants.LABEL_FONT_PLAIN_13);
        footerLabel.setForeground(UIConstants.TEXT_SECONDARY);
        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private JTextArea createParagraph(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(UIConstants.LABEL_FONT_PLAIN_15);
        textArea.setForeground(UIConstants.TEXT_PRIMARY);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIConstants.WHITE);
        textArea.setBorder(null);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
        return textArea;
    }

    private JPanel createMemberCard(String name, String role, String initials, Color avatarColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(240, 300));
        card.setBackground(UIConstants.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 15, 25, 15)
        ));

        JLabel avatarLabel = createAvatarLabel(initials, avatarColor);
        avatarLabel.setPreferredSize(new Dimension(130, 130));
        avatarLabel.setMaximumSize(new Dimension(130, 130));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("<html><center>" + name + "</center></html>");
        nameLabel.setFont(UIConstants.LABEL_FONT_BOLD_16);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel roleLabel = new JLabel("<html><center>" + role + "</center></html>");
        roleLabel.setFont(UIConstants.LABEL_FONT_PLAIN_13.deriveFont(Font.ITALIC));
        roleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(Box.createVerticalGlue());
        card.add(avatarLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(roleLabel);
        card.add(Box.createVerticalGlue());
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(UIConstants.BACKGROUND_COLOR);
                card.setBorder(new LineBorder(UIConstants.PRIMARY_COLOR, 1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(UIConstants.WHITE);
                card.setBorder(new LineBorder(UIConstants.BORDER_COLOR, 1));
            }
        });

        return card;
    }

    private JLabel createAvatarLabel(String initials, Color avatarColor) {
        BufferedImage avatarImage = loadAvatarImage(initials);
        JLabel avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        if (avatarImage != null) {
            avatarLabel.setIcon(new ImageIcon(createCircularImage(avatarImage, 130)));
        } else {
            avatarLabel.setText(initials);
            avatarLabel.setFont(UIConstants.TITLE_FONT.deriveFont(36f));
            avatarLabel.setForeground(UIConstants.WHITE);
            avatarLabel.setOpaque(true);
            avatarLabel.setBackground(avatarColor);
            avatarLabel.setBorder(new LineBorder(avatarColor, 5, true));
        }
        
        return avatarLabel;
    }

    private BufferedImage loadAvatarImage(String initials) {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        for (String ext : extensions) {
            try {
                File imgFile = new File("src/resources/" + initials + ext);
                if (imgFile.exists()) {
                    return ImageIO.read(imgFile);
                }
            } catch (Exception e) {
                // Continue to next extension
            }
        }
        return null;
    }

    private BufferedImage createCircularImage(BufferedImage source, int diameter) {
        BufferedImage output = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
        Image scaledImage = source.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        g2.drawImage(scaledImage, 0, 0, null);
        g2.dispose();
        return output;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AboutPanel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            userFrame mockFrame = new userFrame();
            mockFrame.loggedInUserId = 1;
            mockFrame.loggedInUserRole = "Admin";
            frame.add(new AboutPanel(mockFrame));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
