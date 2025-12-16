package com.consolelogteam.inventorysystem.Logik;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();
    private ItemFactory itemfactory = new ItemFactory();

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
    public void addItem(ItemId itemid) {
        inventoryList.add(itemfactory.createItem(itemid));
    }
    protected void removeItem(int inventoryindex) {
        //Observable list remove() takes from-to indices. "to" being exclusive.
        inventoryList.remove(inventoryindex, inventoryindex+1);
    }


    /** Loading the Saved List */
    public void loadSavedList(ObservableList<Item> savedList){
        inventoryList = savedList;
    }


    /** Temporarily make an Item for Logic Purposes */
    public Item tempMakeItem(ItemId itemId){
         return itemfactory.createItem(itemId);
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

