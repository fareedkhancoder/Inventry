package com.example.inventry.Classes;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private double quantity;
    private double rate;
    private double taxPercentage;
    private double taxAmount;
    private double discountPercentage;
    private double discountAmount;
    private double subtotal;
    private double totalAmount;
    private String category;

    // Constructor
    public Product(String name, double quantity, double rate, double taxPercentage, double taxAmount,
                   double discountPercentage, double discountAmount, double subtotal, double totalAmount, String category) {
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
        this.taxPercentage = taxPercentage;
        this.taxAmount = taxAmount;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.category = category;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getRate() {
        return rate;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public String getCategory() {
        return category;
    }
}
