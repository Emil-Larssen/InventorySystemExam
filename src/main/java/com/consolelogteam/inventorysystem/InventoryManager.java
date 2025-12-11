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
            if (item instanceof Consumable){
                weight += item.getWeight() * ((Consumable) item).getStacksize();
            } else {
                weight += item.getWeight();
            }
        }
        inventory.setWeightFilled(weight);
    }

    public String inventoryWeightLimit() {
        return "Vægt: " + inventory.getWeightFilled() + " kg" + " / " + inventory.getWeightLimit() + " kg";
    }


}
