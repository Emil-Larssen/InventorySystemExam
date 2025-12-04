package com.consolelogteam.inventorysystem;

public class InventoryManager {
    Inventory inventory = new Inventory();

    private void putItemInInventory(String name) {
        inventory.addItem(name);
    }
    //removeItemFromInventory(){}

}
