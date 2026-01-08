package com.consolelogteam.inventorysystem.logik;

import com.consolelogteam.inventorysystem.exceptions.ExceedItemLimitException;
import com.consolelogteam.inventorysystem.exceptions.ExceedWeightLimitException;
import com.consolelogteam.inventorysystem.exceptions.MaxInventorySlotsReachedException;
import com.consolelogteam.inventorysystem.dal.Persistence;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InventoryManager {

    /** Objects */
    private Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private Persistence persistence = new Persistence();


    private ItemFactory itemFactory = new ItemFactory();


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
        try {
            inventory.checkLoadedSlots(persistence.loadAmountOfInventorySlots());
        } catch (FileNotFoundException fnfe){
            inventory.calculateNeededSlots();
            throw new RuntimeException("Der blev ikke fundet en fil til gemte inventory pladser");

        } catch (IOException ioe){
            inventory.calculateNeededSlots();
            throw new RuntimeException("Der gik noget galt i forbindelse med at gendanne inventory pladser");

        } catch (NumberFormatException nfe){
            inventory.calculateNeededSlots();
            throw new RuntimeException("Der blev ikke fundet et heltal i den gemte fil");
        }
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
        inventory.setSlotsFilled(inventory.getInventoryLength());
    }

    public String printItemLimit() {
        return "Pladser: " + inventory.getSlotsFilled() + " / " + inventory.getItemSlotsLimit();
    }

    /** Update Weight Filled and Formating the Limit */
    public void updateWeightFilled() {
        double weight = 0;
        for (Item item : inventory.getInventoryList()) {
            if (item instanceof Consumable){
                weight += item.getWeight() * ((Consumable) item).getStacksize();
            } else {
                weight += item.getWeight();
            }
        }
        inventory.setWeightFilled(weight);
    }

    public String printWeightLimit() {
        return "Vægt: " + String.format("%.2f",inventory.getWeightFilled())  + " kg" + " / " + String.format("%.2f",inventory.getWeightLimit()) + " kg";
    }


    /** Increasing the Slot Limit */
    public void increasingSlotsLimit(){
        if (inventory.getItemSlotsLimit() + inventory.getIncrementInventorySlots() <= inventory.getMaxInventorySlotLimit()){
            inventory.setSlotLimit(inventory.getItemSlotsLimit()+inventory.getIncrementInventorySlots());
        } else {
            throw new MaxInventorySlotsReachedException("Du kan ikke forøge inventory pladser til mere end " + inventory.getMaxInventorySlotLimit());
        }
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
