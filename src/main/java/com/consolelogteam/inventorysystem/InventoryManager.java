package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryManager {
    Inventory inventory = new Inventory();

    public ObservableList<Item> getAllItems() {
        return FXCollections.observableArrayList(
                new Item ("Example", 2.3)
        );

    //private void putItemInInventory(String name) {
    //    inventory.addItem(name);
    }
    //removeItemFromInventory(){}

}
