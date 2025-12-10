package com.consolelogteam.inventorysystem;

import java.io.Serializable;

//To be able to be saved to a file like we are doing the object must implement Serializable
//It shouldn't have any impact on any of the other code whatsoever
public abstract class Item implements Serializable {
    private String itemName;
    private double weight;
    private ItemType itemType;

    public Item(String itemName, double weight, ItemType itemType) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.weight = weight;
    }

    public String getItemName() {
        return itemName;
    }

    public String toString() {
        return itemName + "   " + weight + " kg";
    }

    public double getWeight() {
        return weight;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
