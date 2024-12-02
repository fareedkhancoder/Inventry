package com.example.inventry.Classes;

public class PurchaseHistory {

    private String date;
    private double rate;
    private int quantity;
    private String supplierName; // Add supplier name field
    private int serialNumber;

    // Constructor
    public PurchaseHistory(String date, double rate, int quantity, String supplierName, int serialNumber) {
        this.date = date;
        this.rate = rate;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.serialNumber = serialNumber;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public double getRate() {
        return rate;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName; // Getter for supplier name
    }

    public int getSerialNumber() {
        return serialNumber; // Getter for serial number
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
