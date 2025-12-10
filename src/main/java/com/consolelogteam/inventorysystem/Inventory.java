package com.consolelogteam.inventorysystem;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;




import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();
    private ItemFactory itemfactory = new ItemFactory();

    //inventory constraints
    public final int startinventoryslots = 32;
    public int inventoryslots = 32;
    public final int maxinventoryslots = 192;
    public double weightlimit = 50.0;


    public void loadSavedList(ObservableList<Item> savedList){
        inventoryList = savedList;
    }


    public ObservableList<Item> getInventoryList() {
        return inventoryList;
    }

    public void addItem(ItemId itemid) {
        inventoryList.add(itemfactory.createItem(itemid));
    }
    protected void removeItem(int inventoryindex) {
        //Observable list remove() takes from-to indices. "to" being exclusive.
        inventoryList.remove(inventoryindex, inventoryindex+1);
    }





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
                if (inventoryList.get(i).getWeight() > inventoryList.get(i + 1).getWeight()) {
                    swappedSomething = true;


                    Item temp = inventoryList.get(i);
                    inventoryList.set(i, inventoryList.get(i + 1));
                    inventoryList.set(i + 1, temp);
                }
            }
        }
    }




}

