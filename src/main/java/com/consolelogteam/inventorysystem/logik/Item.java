package com.consolelogteam.inventorysystem.logik;

import java.io.Serializable;

//To be able to be saved to a file like we are doing the object must implement Serializable
//It shouldn't have any impact on any of the other code whatsoever

//Implementering af "Serializable" er forsimplet sagt, en måde,
// når man bruger "ObjectInputStream" og "ObjectOutputStream",
// som markerer at den kan konverteres om til en anden form på en meningsfuld måde.
//Dette er i form af en eller anden repræsentation af objektet.
public abstract class Item implements Serializable {
    private String itemName;
    private double weight;
    private ItemType itemType;
    private ItemId itemId;

    public Item(String itemName, double weight, ItemType itemType, ItemId itemId) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.weight = weight;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    //toString metoden er skrevet, da det er måden/formen 'ListView' viser elementer
    //God skik havde nok været at skrive @Override
    public String toString() {
        return itemName + "   " + String.format("%.2f",weight)  + " kg";
    }

    public double getWeight() {
        return weight;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public ItemId getItemId(){
        return itemId;
    }

}
