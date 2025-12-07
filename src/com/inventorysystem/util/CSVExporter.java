package com.inventorysystem.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// CSVExporter
public class CSVExporter {
    
    // Export JTable data to CSV file
    public static void exportTableToCSV(JTable table, String defaultFileName, JComponent parent) {
        if (table == null || table.getModel().getRowCount() == 0) {
            JOptionPane.showMessageDialog(parent, 
                "No data to export!", 
                "Export Failed", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        
        // Center the file chooser on screen
        fileChooser.setPreferredSize(new java.awt.Dimension(800, 600));
        
        // Default filename with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = defaultFileName + "_" + timestamp + ".csv";
        fileChooser.setSelectedFile(new File(fileName));
        
        // Set file filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
        fileChooser.setFileFilter(filter);
        
        // Get parent window for centering
        java.awt.Window parentWindow = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        int userSelection = fileChooser.showSaveDialog(parentWindow);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Add .csv extension if not present
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try {
                writeTableToCSV(table, fileToSave, defaultFileName);
                
                JOptionPane.showMessageDialog(parentWindow,
                    "Data exported successfully!\n\nFile: " + fileToSave.getName() + "\nLocation: " + fileToSave.getParent() + "\nRows: " + table.getModel().getRowCount(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentWindow,
                    "Error exporting data: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Write table data to CSV file with Excel-friendly formatting
    private static void writeTableToCSV(JTable table, File file, String reportType) throws IOException {
        TableModel model = table.getModel();
        
        // Use UTF-8 with BOM for Excel compatibility
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            
            // Write UTF-8 BOM for Excel to recognize encoding
            writer.write('\ufeff');
            
            // Write metadata header
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss a"));
            writer.write("SmartStock Inventory System\n");
            writer.write("Report Type:," + formatReportName(reportType) + "\n");
            writer.write("Generated:," + timestamp + "\n");
            writer.write("Total Records:," + model.getRowCount() + "\n");
            writer.write("\n"); // Blank line separator
            
            // Determine which columns to skip (# column)
            int skipColumn = -1;
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (model.getColumnName(col).equals("#")) {
                    skipColumn = col;
                    break;
                }
            }
            
            // Write column headers (skip # column)
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col == skipColumn) continue; // Skip row number column
                
                writer.write(escapeCSV(model.getColumnName(col)));
                if (col < model.getColumnCount() - 1 && !(col + 1 == skipColumn && col + 1 == model.getColumnCount() - 1)) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            
            // Write data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                boolean firstColumn = true;
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col == skipColumn) continue; // Skip row number column
                    
                    if (!firstColumn) {
                        writer.write(",");
                    }
                    firstColumn = false;
                    
                    Object value = model.getValueAt(row, col);
                    String columnName = model.getColumnName(col);
                    String cellValue = formatCellValue(value, columnName);
                    writer.write(escapeCSV(cellValue));
                }
                writer.write("\n");
            }
            
            // Write footer summary
            writer.write("\n");
            writer.write("End of Report\n");
            writer.write("Exported by SmartStock v1.0\n");
        }
    }
    
    // Format report name for display
    private static String formatReportName(String reportType) {
        switch (reportType.toLowerCase()) {
            case "products": return "Product Inventory Report";
            case "stock_logs": return "Stock Movement Report";
            case "transaction_records": return "Transaction History Report";
            default: return reportType.substring(0, 1).toUpperCase() + reportType.substring(1) + " Report";
        }
    }
    
    // Format cell value for clean display with column-aware formatting
    private static String formatCellValue(Object value, String columnName) {
        if (value == null || value.toString().trim().isEmpty()) {
            return "-"; // Show dash for empty values
        }
        
        String strValue = value.toString().trim();
        
        // Handle date/time columns
        if (columnName.equalsIgnoreCase("Date") || columnName.toLowerCase().contains("date")) {
            strValue = formatDateValue(strValue);
        }
        
        // Handle currency columns (Cost, Price, Total, Retail)
        else if (columnName.toLowerCase().contains("price") || 
                 columnName.toLowerCase().contains("cost") || 
                 columnName.toLowerCase().contains("total") ||
                 columnName.toLowerCase().contains("retail")) {
            strValue = formatCurrencyValue(strValue);
        }
        
        // Handle percentage columns
        else if (columnName.toLowerCase().contains("markup") || columnName.contains("%")) {
            strValue = formatPercentageValue(strValue);
        }
        
        // Handle quantity/number columns
        else if (columnName.equalsIgnoreCase("Qty") || 
                 columnName.equalsIgnoreCase("Stock") || 
                 columnName.equalsIgnoreCase("Available") ||
                 columnName.toLowerCase().contains("stock in") ||
                 columnName.toLowerCase().contains("stock out")) {
            strValue = formatNumberValue(strValue);
        }
        
        return strValue;
    }
    
    // Format date values
    private static String formatDateValue(String value) {
        // Already well-formatted dates pass through
        if (value.contains("-") && value.contains(":") && value.length() > 15) {
            return value; // Likely already formatted like "01-Nov-2024 08:15 AM"
        }
        
        // Try to parse and format common date patterns
        try {
            // Handle full datetime formats like "2024-11-01 08:15:00" or "2024-11-01T08:15:00"
            if (value.matches("\\d{4}-\\d{2}-\\d{2}.*\\d{2}:\\d{2}.*")) {
                String dateTime = value.replace("T", " ");
                String[] parts = dateTime.split(" ");
                
                if (parts.length >= 2) {
                    String[] dateParts = parts[0].split("-");
                    String[] timeParts = parts[1].split(":");
                    
                    if (dateParts.length == 3 && timeParts.length >= 2) {
                        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        int hour = Integer.parseInt(timeParts[0]);
                        int minute = Integer.parseInt(timeParts[1]);
                        
                        String ampm = hour >= 12 ? "PM" : "AM";
                        int displayHour = hour % 12;
                        if (displayHour == 0) displayHour = 12;
                        
                        return String.format("%02d-%s-%d %02d:%02d %s", 
                            day, months[month], year, displayHour, minute, ampm);
                    }
                }
            }
            
            // Handle time-only formats like "08:15:00" or "14:30"
            else if (value.matches("\\d{2}:\\d{2}(:\\d{2})?")) {
                String[] timeParts = value.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                
                String ampm = hour >= 12 ? "PM" : "AM";
                int displayHour = hour % 12;
                if (displayHour == 0) displayHour = 12;
                
                // Get current date for time-only values
                LocalDateTime now = LocalDateTime.now();
                String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                
                return String.format("%02d-%s-%d %02d:%02d %s", 
                    now.getDayOfMonth(), months[now.getMonthValue()], now.getYear(), 
                    displayHour, minute, ampm);
            }
            
            // Handle date-only formats like "2024-11-01"
            else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                String[] dateParts = value.split("-");
                String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                
                return String.format("%02d-%s-%d", day, months[month], year);
            }
        } catch (Exception e) {
            // If parsing fails, return original value
        }
        
        return value;
    }
    
    // Format currency values - with PHP prefix and commas for readability
    private static String formatCurrencyValue(String value) {
        // Remove existing currency symbols and commas
        value = value.replace("₱", "").replace("PHP", "").replace(",", "").trim();
        
        // Try to parse as number and format
        try {
            double amount = Double.parseDouble(value);
            // Format with PHP prefix and thousand separators
            return String.format("PHP %,.2f", amount);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Format percentage values
    private static String formatPercentageValue(String value) {
        // Remove % symbol if present
        value = value.replace("%", "").trim();
        
        try {
            double percent = Double.parseDouble(value);
            return String.format("%.2f%%", percent);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Format number values (quantities)
    private static String formatNumberValue(String value) {
        try {
            // Check if it's a whole number
            if (value.contains(".")) {
                double num = Double.parseDouble(value);
                // If no decimal part, show as integer
                if (num == Math.floor(num)) {
                    return String.valueOf((int) num);
                }
                return String.format("%.2f", num);
            } else {
                // Already a whole number
                return value;
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Escape special characters in CSV
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // If contains comma, quote, or newline, wrap in quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Escape quotes by doubling them
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        
        return value;
    }
}
