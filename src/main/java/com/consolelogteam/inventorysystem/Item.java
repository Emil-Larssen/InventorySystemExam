package com.consolelogteam.inventorysystem;

public class Item {
    private String itemName;
    private double weight;
    ItemType itemType;

    public Item(String itemName, double weight, ItemType itemType) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.weight = weight;
    }
}
