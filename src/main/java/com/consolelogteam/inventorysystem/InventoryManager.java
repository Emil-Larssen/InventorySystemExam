package com.consolelogteam.inventorysystem;
import com.consolelogteam.inventorysystem.ItemId;
import javafx.collections.ObservableList;

public class InventoryManager {
    private Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private Persistence persistence = new Persistence();

    //Works as a link between persistence and inventory
    public void loadingSavedInventory(){
        inventory.loadSavedList(persistence.loadListOfItems());
    }

    public void savingInventory(){
        persistence.saveListOfItems(inventory.getInventoryList());
    }

    public void addItemToInventory(ItemId itemId) {
        inventory.addItem(itemId);
    }

    public void removeItemFromInventory(int inventoryindex){
        inventory.removeItem(inventoryindex);
    }

    public ObservableList getItemList(){
        return inventory.getInventoryList();
    }

    public void sortingAfterName() {
        inventory.sortInventoryAlphabetically();

    }

    public void sortingAfterWeight() {
        inventory.sortInventoryByWeight();
    }




}
