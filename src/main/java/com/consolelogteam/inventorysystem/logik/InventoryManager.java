package com.consolelogteam.inventorysystem.logik;


import com.consolelogteam.inventorysystem.dal.Persistence;
import javafx.collections.ObservableList;

public class InventoryManager {

    /** Objects */
    private final Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private final Persistence persistence = new Persistence();


    private final ItemFactory itemFactory = new ItemFactory();


    /** Returning the Inventory List */
    public ObservableList<Item> getItemList() {
        return inventory.getInventoryList();
    }

    /** Loading and Saving Inventory Items*/
    //Works as a link between persistence and inventory
    public void loadingSavedInventory() {
        inventory.loadSavedList(persistence.loadListOfItems());
    }

    public void savingInventory() {
        persistence.saveListOfItems(inventory.getInventoryList());
    }


    /** Loading and Saving Inventory Slots */
    public void loadInventorySlots(){
        inventory.checkLoadedSlots(persistence.loadAmountOfInventorySlots());
    }

    //Calculating a new slotLimit
    public void calculateNewSlotLimit(){
        inventory.calculateNeededSlots();
    }

    public void saveInventorySlots(){
        persistence.saveAmountOfInventorySlots(inventory.getItemSlotsLimit());
    }



    /** Adding and Removing Items from Inventory */
    public void addItemToInventory(ItemId itemId) {
        inventory.checkAddItem(itemFactory.createItem(itemId), itemId);
    }

    public void removingItemFromInventory(int inventoryIndex, Item item) {
        inventory.checkRemoveItem(inventoryIndex, item);
    }

    /** Update Slots Filled and Formating the Limit */
    public void updateSlotsFilled() {
        inventory.refreshSlotsFilled();
    }

    public String printSlotsLimit() {
        return inventory.refreshPrintSlots();
    }

    /** Update Weight Filled and Formating the Limit */
    public void updateWeightFilled() {
        inventory.refreshWeightFilled();
    }

    public String printWeightLimit() {
        return inventory.refreshPrintWeight();
    }


    /** Increasing the Slot Limit */
    public void increasingSlotsLimit(){
        inventory.checkIncreaseSlotsLimit();
    }


    /** Sorting */
    public void sortingAfterName() {
        inventory.sortInventoryAlphabetically();
    }

    public void sortingAfterWeight() {
        inventory.sortInventoryByWeight();
    }

    public void sortInventoryByType() {
        inventory.sortInventoryByType();
    }

}
