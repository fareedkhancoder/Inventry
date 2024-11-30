package com.example.inventry.Classes;

public class PurchaseDetails {
    private int purchaseDetailId;
    private int purchaseId;
    private int productId;
    private int quantityPurchased;
    private double Rate;
    private double discPercentage;
    private double discAmount;
    private double taxPercentage;
    private double taxAmount;
    private double subtotal;
    private double totalProductCost;

    // Constructor
    public PurchaseDetails(int purchaseDetailId, int purchaseId, int productId, int quantityPurchased, double Rate, double discPercentage, double discAmount, double taxPercentage, double taxAmount, double totalProductCost, double subtotal) {
        this.purchaseDetailId = purchaseDetailId;
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.quantityPurchased = quantityPurchased;
        this.Rate = Rate;
        this.discPercentage = discPercentage;
        this.discAmount = discAmount;
        this.taxPercentage = taxPercentage;
        this.taxAmount = taxAmount;
        this.totalProductCost = totalProductCost;
        this.subtotal = subtotal;
    }

    public double getTotalProductCost() {
        return totalProductCost;
    }

    public void setTotalProductCost(double totalProductCost) {
        this.totalProductCost = totalProductCost;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public double getDiscAmount() {
        return discAmount;
    }

    public void setDiscAmount(double discAmount) {
        this.discAmount = discAmount;
    }

    public double getDiscPercentage() {
        return discPercentage;
    }

    public void setDiscPercentage(double discPercentage) {
        this.discPercentage = discPercentage;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public void setPurchaseDetailId(int purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
    }
}