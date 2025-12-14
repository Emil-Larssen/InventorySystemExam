package com.consolelogteam.inventorysystem;

import com.consolelogteam.inventorysystem.Exceptions.ExceedItemLimitException;
import com.consolelogteam.inventorysystem.Exceptions.ExceedWeightLimitException;
import com.consolelogteam.inventorysystem.Exceptions.MaxInventorySlotsReachedException;
import com.consolelogteam.inventorysystem.Model.Consumable;
import com.consolelogteam.inventorysystem.Model.Item;
import com.consolelogteam.inventorysystem.Model.ItemId;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InventoryManager {

    /** Objects */
    private Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private Persistence persistence = new Persistence();


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
            int loadedInventorySlots = persistence.loadAmountOfInventorySlots();
            if (loadedInventorySlots >= inventory.getStartingInventorySlots() && loadedInventorySlots <= inventory.getMaxInventorySlotLimit()){
                if (loadedInventorySlots >= inventory.getSlotsFilled()){
                    inventory.setSlotLimit(loadedInventorySlots);
                } else {
                    inventory.setSlotLimit(calculateNeededSlots());
                    throw new RuntimeException("Der blev fundet et antal inventory pladser som var mindre end det nødvendige");
                }
            } else {
                inventory.setSlotLimit(calculateNeededSlots());
                throw new RuntimeException("Der blev fundet et antal inventory pladser som er under minimum eller over maksimum");
            }

        } catch (FileNotFoundException fnfe){
            inventory.setSlotLimit(calculateNeededSlots());
            throw new RuntimeException("Der blev ikke fundet en fil til gemte inventory pladser");

        } catch (IOException ioe){
            inventory.setSlotLimit(calculateNeededSlots());
            throw new RuntimeException("Der gik noget galt i forbindelse med at gendanne inventory pladser");

        } catch (NumberFormatException nfe){
            inventory.setSlotLimit(calculateNeededSlots());
            throw new RuntimeException("Der blev ikke fundet et heltal i den gemte fil");
        }
    }

    public void saveInventorySlots(){
        persistence.saveAmountOfInventorySlots(inventory.getItemSlotsLimit());
    }

    /** Calculating the Needed Slots in case of File Error */
    public int calculateNeededSlots(){
        int calculatedSlots = inventory.getStartingInventorySlots();
        while(calculatedSlots < inventory.getSlotsFilled()){
            calculatedSlots += inventory.getIncrementInventorySlots();
        }
        return calculatedSlots;
    }


    /** Adding and Removing Items from Inventory */
    public void addItemToInventory(ItemId itemId) {
        boolean existsInInventory = false;

        if (inventory.tempMakeItem(itemId) instanceof Consumable){
            if (inventory.getWeightFilled() + (inventory.tempMakeItem(itemId).getWeight()) <= inventory.getWeightLimit()) {
                for (Item item : inventory.getInventoryList()) {
                    if (item instanceof Consumable) {
                        if (item.getItemId() == itemId) {
                            ((Consumable) item).incrementStacksize();
                            existsInInventory = true;
                        }
                    }
                }
            }
        }
        if (!existsInInventory) {
            if (inventory.getSlotsFilled() < inventory.getItemSlotsLimit()) {
                if (inventory.getWeightFilled() + (inventory.tempMakeItem(itemId).getWeight()) <= inventory.getWeightLimit()) {
                    inventory.addItem(itemId);
                } else {
                    throw new ExceedWeightLimitException("Du må ikke overskride den maksimale vægt");
                }
            } else {
                throw new ExceedItemLimitException("Du må ikke overskride det maksimale antal items");
            }
        }
    }

    public void removeItemFromInventory(int inventoryIndex, ItemId itemId) {
        boolean existsInInventory = false;
        if (inventory.tempMakeItem(itemId) instanceof Consumable){
            for (Item item : inventory.getInventoryList()){
                if (item instanceof Consumable){
                    if (item.getItemId() == itemId){
                        ((Consumable) item).decrementStacksize();
                        if (((Consumable) item).getStacksize() != 0) {
                            existsInInventory = true;
                        }
                    }
                }
            }
        }
        if (!existsInInventory) {
            inventory.removeItem(inventoryIndex);
        }
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
        return "Vægt: " + inventory.getWeightFilled()  + " kg" + " / " + inventory.getWeightLimit() + " kg";
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
