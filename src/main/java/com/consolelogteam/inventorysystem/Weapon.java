package com.consolelogteam.inventorysystem;

public class Weapon extends Item {

    private WeaponEquip weaponEquip;
    public Weapon(String itemName, double weight, WeaponEquip weaponEquip, ItemId itemId) {
        super(itemName, weight, ItemType.WEAPON, itemId);
        this.weaponEquip = weaponEquip;
    }
}
