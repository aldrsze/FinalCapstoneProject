package com.inventorysystem.model;

// Product
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