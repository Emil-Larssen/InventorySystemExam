package com.consolelogteam.inventorysystem.logik;

//Sub-klasse af "Item"
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

    //Overskrives her, fra 'toString()' metoden i "Item",
    // da 'ListView' skal inkluderer vægt baseret på stackSize/antal samt at oplyse antallet.
    //Det er nemlig ved 'toString()' metoden at 'ListView' viser elementer i listen.
    @Override
    public String toString() {
        return getItemName() + "   " + String.format("%.2f",getWeight() * getStacksize()) + " kg" + "   antal " + stacksize ;
    }

}
