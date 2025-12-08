package com.consolelogteam.inventorysystem;
import com.consolelogteam.inventorysystem.ItemId;

public class ItemFactory {

    public Item createItem(ItemId itemId) {

        return switch (itemId){
            case GENERIC_WEAPON -> new Weapon("Generic Weapon", 3);
            case GENERIC_ARMOR -> new Armor("Generic Armor", 3);
            case GENERIC_CONSUMABLE -> new Consumable("Generic Consumable", 3);
            default -> null;
        };
    }

}
