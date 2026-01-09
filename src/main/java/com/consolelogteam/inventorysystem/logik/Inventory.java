package com.consolelogteam.inventorysystem.logik;
import com.consolelogteam.inventorysystem.exceptions.ExceedItemLimitException;
import com.consolelogteam.inventorysystem.exceptions.ExceedWeightLimitException;
import com.consolelogteam.inventorysystem.exceptions.MaxInventorySlotsReachedException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();

    /** Inventory constraints */
    private final int STARTINGINVENTORYSLOTS = 32;
    private final int INCREMENTINCREASESLOTS = 32;
    private final int MAXINVENTORYSLOTS = 192;
    private final double WEIGHTLIMIT = 50.0;

    private int inventorySlotsLimit = STARTINGINVENTORYSLOTS;

    /** Inventory Variables */
    private int slotsFilled = 0;
    private double weightFilled = 0;


    /** Getters */
    public ObservableList<Item> getInventoryList() {
        return inventoryList;
    }


    public int getMaxInventorySlotLimit(){
        return MAXINVENTORYSLOTS;
    }

    //Inventory Slots
    public int getStartingInventorySlots(){
        return STARTINGINVENTORYSLOTS;
    }

    public int getIncrementInventorySlots(){
        return INCREMENTINCREASESLOTS;
    }

    public int getItemSlotsLimit(){
        return inventorySlotsLimit;
    }

    public int getSlotsFilled(){
        return slotsFilled;
    }

    //Weight
    public double getWeightLimit(){
        return WEIGHTLIMIT;
    }

    public double getWeightFilled(){
        return weightFilled;
    }

    public int getInventoryLength(){
        return inventoryList.size();
    }


    /** Setters */
    public void setSlotLimit(int newLimit){
        inventorySlotsLimit = newLimit;
    }

    public void setSlotsFilled(int slotsFilled){
        this.slotsFilled = slotsFilled;
    }

    public void setWeightFilled(double weightFilled){
        this.weightFilled = weightFilled;
    }


    /** Adding and removing Items from Inventory */
    public void checkAddItem(Item itemToCheck, ItemId itemid) {
        if (weightFilled + itemToCheck.getWeight() <= WEIGHTLIMIT){
            if (itemToCheck instanceof Consumable){
                for (Item item : inventoryList){
                    if (item instanceof Consumable) {
                        if (itemid == item.getItemId()) {
                            ((Consumable) item).incrementStacksize();
                            return;
                        }
                    }
                }

            }
            if (slotsFilled < inventorySlotsLimit){
                inventoryList.add(itemToCheck);
            } else {
                throw new ExceedItemLimitException("Der kan ikke tilføjes flere items end det maksimale antal items");
            }
        } else {
            throw new ExceedWeightLimitException("Der kan ikke tilføjes mere vægt end den maksimale vægt");
        }
    }

    protected void checkRemoveItem(int inventoryindex, Item itemToCheck) {
        if (itemToCheck instanceof Consumable){
            if (((Consumable) itemToCheck).getStacksize()>1){
                ((Consumable) itemToCheck).decrementStacksize();
            } else {
                inventoryList.remove(inventoryindex);
            }
        } else {
            inventoryList.remove(inventoryindex);
        }
    }

    public void checkLoadedSlots(int loadedInventorySlots) {
            if (loadedInventorySlots >= STARTINGINVENTORYSLOTS && loadedInventorySlots <= MAXINVENTORYSLOTS) {
                if (loadedInventorySlots >= slotsFilled) {
                    inventorySlotsLimit = loadedInventorySlots;
                } else {
                    calculateNeededSlots();
                    throw new RuntimeException("Der blev fundet et antal inventory pladser som var mindre end det nødvendige");
                }
            } else {
                calculateNeededSlots();
                throw new RuntimeException("Der blev fundet et antal inventory pladser som er under minimum eller over maksimum");
            }
    }

    public void calculateNeededSlots(){
        int calculatedSlots = STARTINGINVENTORYSLOTS;
        while(calculatedSlots < slotsFilled){
            calculatedSlots += INCREMENTINCREASESLOTS;
        }
        inventorySlotsLimit = calculatedSlots;
    }

    public void checkIncreaseSlotsLimit(){
        int newLimit = inventorySlotsLimit + INCREMENTINCREASESLOTS;
        if (newLimit <= MAXINVENTORYSLOTS){
             inventorySlotsLimit = newLimit;
        } else {
            throw new MaxInventorySlotsReachedException("Du kan ikke forøge inventory pladser til mere end "
                    + MAXINVENTORYSLOTS);
        }
    }

    public void refreshSlotsFilled(){
        slotsFilled = inventoryList.size();
    }

    public String refreshPrintSlots(){
        return "Pladser: " + slotsFilled + " / " + inventorySlotsLimit;
    }


    /** Loading the Saved List */
    public void loadSavedList(ObservableList<Item> savedList){
        inventoryList = savedList;
    }


    /** Sorting of Inventory */
    // bobble sortering Alfabetisk
    public void sortInventoryAlphabetically() {
        boolean swappedSomething = true;

        while (swappedSomething) {
            swappedSomething = false;

            for (int i = 0; i < inventoryList.size() - 1; i++) {
                if (inventoryList.get(i).getItemName()
                        .compareTo(inventoryList.get(i + 1).getItemName()) > 0) {
                    swappedSomething = true;

                    Item temp = inventoryList.get(i);
                    inventoryList.set(i, inventoryList.get(i + 1));
                    inventoryList.set(i + 1, temp);
                }
            }
        }
    }


    // bobble sortering weight
    public void sortInventoryByWeight() {
        boolean swappedSomething = true;

        while (swappedSomething) {
            swappedSomething = false;

            for (int i = 0; i < inventoryList.size() - 1; i++) {

                //I have added a check that makes sure to differentiate between stackables, so that total weight is taken into account
                double weightOfFirst;
                double weightOfSecond;

                if (inventoryList.get(i) instanceof Consumable){
                    weightOfFirst = inventoryList.get(i).getWeight() * ((Consumable) inventoryList.get(i)).getStacksize();
                } else {
                    weightOfFirst = inventoryList.get(i).getWeight();
                }

                if (inventoryList.get(i+1) instanceof Consumable){
                    weightOfSecond = inventoryList.get(i+1).getWeight() * ((Consumable) inventoryList.get(i+1)).getStacksize();
                } else {
                   weightOfSecond = inventoryList.get(i + 1).getWeight();
                }

                if (weightOfFirst > weightOfSecond) {
                    swappedSomething = true;


                    Item temp = inventoryList.get(i);
                    inventoryList.set(i, inventoryList.get(i + 1));
                    inventoryList.set(i + 1, temp);
                }
            }
        }
    }
    // bobble sortering efter type og alfabetisk rækkefølge
    public void sortInventoryByType() {
        boolean swappedSomething = true;

        while (swappedSomething) {
            swappedSomething = false;

            for (int i = 0; i < inventoryList.size() - 1; i++) {
                Item a = inventoryList.get(i);
                Item b = inventoryList.get(i + 1);

                /* "A".compareTo("B") < 0
                   "B".compareTo("A") > 0
                   "A".compareTo("A") == 0 */

                int typeCompare = a.getItemType().compareTo(b.getItemType()); // sortere efter enum itemType rækkefølge
                int nameCompare = 0;

                if (typeCompare == 0) {
                    // hvis itemType er ens vil den sortere alfabetisk i stedet
                    nameCompare = a.getItemName().compareTo(b.getItemName());
                }


                /* (if) her siger at enten skal det første index være større end det andet (i forhold til typernes enum rækkefølge),
                eller typerne er ens men det første navn er en større værdig end den anden i alfabetet og skal byttes fx("B"compareTo"A")
                hvis en af disse er tilfældet bliver swappedSomething sat til true og sorteringen fortsætter */
                if (typeCompare > 0 || (typeCompare == 0 && nameCompare > 0)) {
                    swappedSomething = true;

                    Item temp = a;
                    inventoryList.set(i, b);
                    inventoryList.set(i + 1, temp);


                }
            }
        }
    }




}

