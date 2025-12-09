package com.inventorysystem.model;

// Product model
public record Product(
    int id,
    String name,
    String categoryName,
    String unit,
    double costPrice,
    double retailPrice,
    int stock,
    double totalCost
) {}