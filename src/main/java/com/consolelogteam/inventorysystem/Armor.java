package com.consolelogteam.inventorysystem;

public class Armor extends Item {
    private ArmorSlot armorslot;
    public Armor(String itemName, double weight, ArmorSlot armorslot ) {
        super(itemName, weight, ItemType.ARMOR);
        this.armorslot = armorslot;
    }
}
