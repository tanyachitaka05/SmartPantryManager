package com.example.smartpantry.model;

/**
 * A single ingredient the user actually has at home.
 * Mirrors one row of the pantry_items table.
 */
public class PantryItem {

    private long id;
    private String name;      // display name, e.g. "Tomatoes"
    private double quantity;  // how much the user has
    private String unit;      // e.g. g, kg, ml, l, pcs
    private String expiryDate; // optional ISO date yyyy-MM-dd, may be null/empty

    public PantryItem() { }

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
