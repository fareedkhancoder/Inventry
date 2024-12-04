package com.example.inventry.Classes;

public class Supplier {
    private final String name;
    private final String contact;

    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
