package com.consolelogteam.inventorysystem;

public class Consumable extends Item {
    public Consumable(String itemName, double weight) {
        super(itemName, weight, ItemType.CONSUMABLE);
    }
}
