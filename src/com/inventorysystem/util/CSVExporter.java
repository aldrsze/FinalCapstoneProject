package com.inventorysystem.util;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// CSV exporter
public class CSVExporter {
    
    // Export to CSV
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
        
        // Center file chooser
        fileChooser.setPreferredSize(new java.awt.Dimension(800, 600));
        
        // Filename with time
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = defaultFileName + "_" + timestamp + ".csv";
        fileChooser.setSelectedFile(new File(fileName));
        
        // File filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
        fileChooser.setFileFilter(filter);
        
        // Get parent window
        java.awt.Window parentWindow = parent != null ? SwingUtilities.getWindowAncestor(parent) : null;
        int userSelection = fileChooser.showSaveDialog(parentWindow);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Add .csv if missing
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
    
    // Write table to CSV
    private static void writeTableToCSV(JTable table, File file, String reportType) throws IOException {
        TableModel model = table.getModel();
        
        // Use UTF-8 BOM
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            
            // Write BOM
            writer.write('\ufeff');
            
            // Write header
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss a"));
            writer.write("SmartStock Inventory System\n");
            writer.write("Report Type:," + formatReportName(reportType) + "\n");
            writer.write("Generated:," + timestamp + "\n");
            writer.write("Total Records:," + model.getRowCount() + "\n");
            writer.write("\n"); // Blank line
            
            // Skip # col
            int skipColumn = -1;
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (model.getColumnName(col).equals("#")) {
                    skipColumn = col;
                    break;
                }
            }
            
            // Write headers
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col == skipColumn) continue; // Skip #
                
                writer.write(escapeCSV(model.getColumnName(col)));
                if (col < model.getColumnCount() - 1 && !(col + 1 == skipColumn && col + 1 == model.getColumnCount() - 1)) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            
            // Write rows
            for (int row = 0; row < model.getRowCount(); row++) {
                boolean firstColumn = true;
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col == skipColumn) continue; // Skip #
                    
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
            
            // Write footer
            writer.write("\n");
            writer.write("End of Report\n");
            writer.write("Exported by SmartStock v1.0\n");
        }
    }
    
    // Format report name
    private static String formatReportName(String reportType) {
        switch (reportType.toLowerCase()) {
            case "products": return "Product Inventory Report";
            case "stock_logs": return "Stock Movement Report";
            case "transaction_records": return "Transaction History Report";
            default: return reportType.substring(0, 1).toUpperCase() + reportType.substring(1) + " Report";
        }
    }
    
    // Format cell value
    private static String formatCellValue(Object value, String columnName) {
        if (value == null || value.toString().trim().isEmpty()) {
            return "-"; // Dash for empty
        }
        
        String strValue = value.toString().trim();
        
        // Date/time cols
        if (columnName.equalsIgnoreCase("Date") || columnName.toLowerCase().contains("date")) {
            strValue = formatDateValue(strValue);
        }
        
        // Currency cols
        else if (columnName.toLowerCase().contains("price") || 
                 columnName.toLowerCase().contains("cost") || 
                 columnName.toLowerCase().contains("total") ||
                 columnName.toLowerCase().contains("retail")) {
            strValue = formatCurrencyValue(strValue);
        }
        
        // Percent cols
        else if (columnName.toLowerCase().contains("markup") || columnName.contains("%")) {
            strValue = formatPercentageValue(strValue);
        }
        
        // Number cols
        else if (columnName.equalsIgnoreCase("Qty") || 
                 columnName.equalsIgnoreCase("Stock") || 
                 columnName.equalsIgnoreCase("Available") ||
                 columnName.toLowerCase().contains("stock in") ||
                 columnName.toLowerCase().contains("stock out")) {
            strValue = formatNumberValue(strValue);
        }
        
        return strValue;
    }
    
    // Format date
    private static String formatDateValue(String value) {
        // Well-formatted dates
        if (value.contains("-") && value.contains(":") && value.length() > 15) {
            return value; // Already formatted
        }
        
        // Parse date
        try {
            // Full datetime
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
            
            // Time only
            else if (value.matches("\\d{2}:\\d{2}(:\\d{2})?")) {
                String[] timeParts = value.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                
                String ampm = hour >= 12 ? "PM" : "AM";
                int displayHour = hour % 12;
                if (displayHour == 0) displayHour = 12;
                
                // Get current date
                LocalDateTime now = LocalDateTime.now();
                String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                
                return String.format("%02d-%s-%d %02d:%02d %s", 
                    now.getDayOfMonth(), months[now.getMonthValue()], now.getYear(), 
                    displayHour, minute, ampm);
            }
            
            // Date only
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
            // If fail, return original
        }
        
        return value;
    }
    
    // Format currency
    private static String formatCurrencyValue(String value) {
        // Remove symbols
        value = value.replace("₱", "").replace("PHP", "").replace(",", "").trim();
        
        // Parse number
        try {
            double amount = Double.parseDouble(value);
            // Format PHP, commas
            return String.format("PHP %,.2f", amount);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Format percent
    private static String formatPercentageValue(String value) {
        // Remove %
        value = value.replace("%", "").trim();
        
        try {
            double percent = Double.parseDouble(value);
            return String.format("%.2f%%", percent);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Format number
    private static String formatNumberValue(String value) {
        try {
            // Whole number
            if (value.contains(".")) {
                double num = Double.parseDouble(value);
                // No decimal, show int
                if (num == Math.floor(num)) {
                    return String.valueOf((int) num);
                }
                return String.format("%.2f", num);
            } else {
                // Already int
                return value;
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    // Escape CSV
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // If comma/quote/newline, quote
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Double quotes
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        
        return value;
    }
}
