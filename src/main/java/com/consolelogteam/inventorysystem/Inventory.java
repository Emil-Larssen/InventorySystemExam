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

