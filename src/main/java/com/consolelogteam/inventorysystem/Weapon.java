package com.consolelogteam.inventorysystem;

public class Weapon extends Item {

    public Weapon(String itemName, double weight) {
        super(itemName, weight, ItemType.WEAPON);
    }
}
