package com.example.inventry.Classes;

public class Category {
    private int id;
    private String name;
    private String description;
    private String date;

    public Category(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Constructor without ID (for new categories that don't have an ID yet)
    public Category(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Getters and setters for each field
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
