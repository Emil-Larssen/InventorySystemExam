package com.consolelogteam.inventorysystem;

public class Consumable extends Item {

    public int stacksize;
    public Consumable(String itemName, double weight) {
        super(itemName, weight, ItemType.CONSUMABLE);
        stacksize = 1;
    }
}
