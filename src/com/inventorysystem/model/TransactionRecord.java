package com.inventorysystem.model;

import java.sql.Timestamp;

// TransactionRecord
public record TransactionRecord(
    Timestamp transactionDate,
    String productName,
    String unit,
    String transactionType,
    int quantity,
    double unitPrice,
    double retailPrice,
    double total,
    String notes
) {}