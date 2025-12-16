package com.consolelogteam.inventorysystem.Logik;

import java.io.Serializable;

//To be able to be saved to a file like we are doing the object must implement Serializable
//It shouldn't have any impact on any of the other code whatsoever
public abstract class Item implements Serializable {
    private String itemName;
    private double weight;
    private ItemType itemType;
    private ItemId itemId;

    public Item(String itemName, double weight, ItemType itemType, ItemId itemId) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.weight = weight;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String toString() {
        return itemName + "   " + String.format("%.2f",weight)  + " kg";
    }

    public double getWeight() {
        return weight;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public ItemId getItemId(){
        return itemId;
    }

}
