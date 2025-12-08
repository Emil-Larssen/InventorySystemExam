package com.consolelogteam.inventorysystem;
import com.consolelogteam.inventorysystem.ItemId;
import javafx.collections.ObservableList;

public class InventoryManager {
    private Inventory inventory = new Inventory();

    private void addItemToInventory(ItemId itemId) {
        inventory.addItem(itemId);
    }

    public ObservableList getItemList(){
        return inventory.getInventoryList();
    }




    //removeItemFromInventory(){}

}
