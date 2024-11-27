package com.example.inventry.Classes;

public class Products {
    private int id;
    private String name;
    private double purchaseRate;
    private int availableQuantities;
    private int categoryId;

    public Products(int id, String name, double purchaseRate, int availableQuantities, int categoryId) {
        this.id = id;
        this.name = name;
        this.purchaseRate = purchaseRate;
        this.availableQuantities = availableQuantities;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public int getAvailableQuantities() {
        return availableQuantities;
    }

    public void setAvailableQuantities(int availableQuantities) {
        this.availableQuantities = availableQuantities;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return name;
    }
}
