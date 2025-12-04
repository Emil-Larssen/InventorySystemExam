package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();

    public ObservableList<Item> getInventoryList() {
        return inventoryList;
    }
    public void addItem(String name) {
        inventoryList.add(new Item(name,0.96));
    }
    private void removeItem(Item item) {
        inventoryList.remove(item);
    }

}
