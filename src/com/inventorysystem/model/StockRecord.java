package com.inventorysystem.model;

// StockRecord
public record StockRecord(
    int productId,
    String productName,
    String category,
    String unit,
    int stockIn,
    int stockOut,
    int endingStock
) {}