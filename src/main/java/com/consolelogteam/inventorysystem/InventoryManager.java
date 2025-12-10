package com.consolelogteam.inventorysystem;

import com.consolelogteam.inventorysystem.ItemId;
import javafx.collections.ObservableList;

public class InventoryManager {
    private Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private Persistence persistence = new Persistence();

    //Works as a link between persistence and inventory
    public void loadingSavedInventory() {
        inventory.loadSavedList(persistence.loadListOfItems());
    }

    public void savingInventory() {
        persistence.saveListOfItems(inventory.getInventoryList());
    }

    public void addItemToInventory(ItemId itemId) {
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

    public void removeItemFromInventory(int inventoryindex) {
        inventory.removeItem(inventoryindex);
    }

    public ObservableList getItemList() {
        return inventory.getInventoryList();
    }

    public void sortingAfterName() {
        inventory.sortInventoryAlphabetically();
    }

    public void sortingAfterWeight() {
        inventory.sortInventoryByWeight();
    }

    public void sortInventoryByType() {
        inventory.sortInventoryByType();
    }

    public void updateSlotsFilled() {
        inventory.setSlotsFilled(inventory.getInventoryLength());
    }

    public String inventoryItemLimit() {
        return "Pladser: " + inventory.getSlotsFilled() + " / " + inventory.getItemSlotsLimit();
    }

    public void updateWeightFilled() {
        double weight = 0;
        for (Item item : inventory.getInventoryList()) {
            weight += item.getWeight();
        }
        inventory.setWeightFilled(weight);
    }

    public String inventoryWeightLimit() {
        return "Vægt: " + inventory.getWeightFilled() + " / " + inventory.getWeightLimit();
    }


}
