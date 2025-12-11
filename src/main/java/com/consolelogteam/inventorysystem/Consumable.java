package com.consolelogteam.inventorysystem;

public class Consumable extends Item {

    private int stacksize;
    private ItemId itemid;
    public Consumable(String itemName, double weight, ItemId itemid) {
        super(itemName, weight, ItemType.CONSUMABLE);
        this.itemid = itemid;
        stacksize = 1;
    }
    public void incrementStacksize(){
        stacksize++;
    }
    public void decrementStacksize(){
        stacksize--;
    }

}
