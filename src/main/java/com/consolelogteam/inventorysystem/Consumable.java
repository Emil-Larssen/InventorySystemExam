package com.consolelogteam.inventorysystem;

public class Consumable extends Item {

    private int stacksize;
    public Consumable(String itemName, double weight, ItemId itemid) {
        super(itemName, weight, ItemType.CONSUMABLE, itemid);
        stacksize = 1;
    }
    public void incrementStacksize(){
        stacksize++;
    }
    public void decrementStacksize(){
        stacksize--;
    }


    public int getStacksize(){
        return stacksize;
    }


    @Override
    public String toString() {
        return getItemName() + "   " + getWeight() * getStacksize() + " kg";
    }

}
