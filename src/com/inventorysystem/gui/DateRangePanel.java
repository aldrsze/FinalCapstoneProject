package com.inventorysystem.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

// Date range panel
public class DateRangePanel extends JPanel {
    
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JComboBox<String> presetComboBox;
    private List<DateRangeChangeListener> listeners = new ArrayList<>();
    
    // Listener
    public interface DateRangeChangeListener {
        void onDateRangeChanged();
    }
    
    public DateRangePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, UIConstants.COMPONENT_SPACING, 5));
        setBackground(UIConstants.FORM_COLOR);
        setBorder(new CompoundBorder(
            new LineBorder(UIConstants.BORDER_COLOR, 1),
            new EmptyBorder(UIConstants.COMPONENT_SPACING / 2, UIConstants.COMPONENT_SPACING, UIConstants.COMPONENT_SPACING / 2, UIConstants.COMPONENT_SPACING)
        ));

        // "From" date picker
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(UIConstants.LABEL_FONT);
        add(fromLabel);
        startDateChooser = new JDateChooser();
        startDateChooser.setPreferredSize(new Dimension(140, UIConstants.INPUT_HEIGHT));
        startDateChooser.setDate(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
        add(startDateChooser);

        // "To" date picker
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(UIConstants.LABEL_FONT);
        add(toLabel);
        endDateChooser = new JDateChooser();
        endDateChooser.setPreferredSize(new Dimension(140, UIConstants.INPUT_HEIGHT));
        endDateChooser.setDate(new Date());
        add(endDateChooser);

        // Quick presets dropdown (Today, Last 7 Days, etc.)
        JLabel quickLabel = new JLabel("Quick:");
        quickLabel.setFont(UIConstants.LABEL_FONT);
        add(quickLabel);
        presetComboBox = new JComboBox<>(new String[]{
            "Custom", "Today", "Yesterday", "Last 7 Days", 
            "Last 30 Days", "This Month", "Last Month", "All Time"
        });
        presetComboBox.setPreferredSize(new Dimension(140, UIConstants.INPUT_HEIGHT));
        presetComboBox.setFont(UIConstants.LABEL_FONT);
        presetComboBox.addActionListener(e -> {
            applyPreset();
            notifyListeners();
        });
        add(presetComboBox);
        
        // Listen for manual date changes
        startDateChooser.addPropertyChangeListener("date", e -> {
            if ("Custom".equals(presetComboBox.getSelectedItem())) {
                notifyListeners();
            }
        });
        
        endDateChooser.addPropertyChangeListener("date", e -> {
            if ("Custom".equals(presetComboBox.getSelectedItem())) {
                notifyListeners();
            }
        });
    }
    
    // Other panels can register to know when dates change
    public void addDateRangeChangeListener(DateRangeChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeDateRangeChangeListener(DateRangeChangeListener listener) {
        listeners.remove(listener);
    }
    
    // Tell all registered panels that dates changed
    private void notifyListeners() {
        for (DateRangeChangeListener listener : listeners) {
            listener.onDateRangeChanged();
        }
    }
    
    // Apply quick preset (Today, Last 7 Days, etc.)
    private void applyPreset() {
        String preset = (String) presetComboBox.getSelectedItem();
        LocalDate now = LocalDate.now();
        LocalDate start = now;
        LocalDate end = now;
        
        switch (preset) {
            case "Today":
                start = now;
                break;
            case "Yesterday":
                start = now.minusDays(1);
                end = now.minusDays(1);
                break;
            case "Last 7 Days":
                start = now.minusDays(6);
                break;
            case "Last 30 Days":
                start = now.minusDays(29);
                break;
            case "This Month":
                start = now.withDayOfMonth(1);
                break;
            case "Last Month":
                start = now.minusMonths(1).withDayOfMonth(1);
                end = now.withDayOfMonth(1).minusDays(1);
                break;
            case "All Time":
                start = LocalDate.of(2000, 1, 1);
                end = LocalDate.now().plusYears(1);
                break;
            case "Custom":
                return;
        }
        
        startDateChooser.setDate(java.sql.Date.valueOf(start));
        endDateChooser.setDate(java.sql.Date.valueOf(end));
    }
    
    public Date getStartDate() {
        return startDateChooser.getDate();
    }
    
    public Date getEndDate() {
        return endDateChooser.getDate();
    }
    
    // Get date for SQL queries (start of day: 00:00:00)
    public java.sql.Date getSqlStartDate() {
        Date date = startDateChooser.getDate();
        if (date == null) return null;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    // Get date for SQL queries (end of day: 23:59:59)
    public java.sql.Date getSqlEndDate() {
        Date date = endDateChooser.getDate();
        if (date == null) return null;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        cal.set(java.util.Calendar.MILLISECOND, 999);
        
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DateRangePanel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            DateRangePanel panel = new DateRangePanel();
            panel.addDateRangeChangeListener(() -> {
                System.out.println("Start: " + panel.getStartDate());
                System.out.println("End: " + panel.getEndDate());
            });
            
            frame.add(panel, BorderLayout.NORTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
