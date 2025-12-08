package com.consolelogteam.inventorysystem;

public class Item {
    private String itemName;
    private double weight;

    public Item(String itemName, double weight) {
        this.itemName = itemName;
        this.weight = weight;
    }

    public String getItemName() {
        return itemName;
    }

    public String toString() {
        return itemName + "   " + weight;
    }
}
