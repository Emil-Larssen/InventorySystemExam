package com.consolelogteam.inventorysystem.logik;

public class Armor extends Item {
    private ArmorSlot armorslot;
    public Armor(String itemName, double weight, ArmorSlot armorslot, ItemId itemId ) {
        super(itemName, weight, ItemType.ARMOR, itemId);
        this.armorslot = armorslot;
    }
}
