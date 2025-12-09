package com.inventorysystem.model;

// Stock record
public record StockRecord(
    int productId,
    String productName,
    String category,
    String unit,
    int stockIn,
    int stockOut,
    int endingStock
) {}