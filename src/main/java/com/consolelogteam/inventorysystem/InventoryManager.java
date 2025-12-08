package com.consolelogteam.inventorysystem;
import com.consolelogteam.inventorysystem.ItemId;

public class InventoryManager {
    private Inventory inventory = new Inventory();

    private void addItemToInventory(ItemId itemId) {
        inventory.addItem(itemId);
    }


    //removeItemFromInventory(){}

}
