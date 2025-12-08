package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();
    private ItemFactory itemfactory = new ItemFactory();



    public ObservableList<Item> getInventoryList() {
        return inventoryList;
    }

    public void addItem(ItemId itemid) {
        inventoryList.add(itemfactory.createItem(itemid));
    }
    private void removeItem(Item item) {
        inventoryList.remove(item);
    }

}
