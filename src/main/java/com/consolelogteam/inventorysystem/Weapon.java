package com.consolelogteam.inventorysystem;

public class Weapon extends Item {

    private WeaponEquip weaponEquip;
    public Weapon(String itemName, double weight, WeaponEquip weaponEquip) {
        super(itemName, weight, ItemType.WEAPON);
        this.weaponEquip = weaponEquip;
    }
}
