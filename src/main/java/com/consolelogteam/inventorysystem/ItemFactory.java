package com.consolelogteam.inventorysystem;

import com.consolelogteam.inventorysystem.Model.*;

public class ItemFactory {

    public Item createItem(ItemId itemId) {

        return switch (itemId){
            case SWORD -> new Weapon("Sværd", 3, WeaponEquip.MAIN_HAND,ItemId.SWORD);
            case AXE -> new Weapon("Økse", 4.5, WeaponEquip.TWO_HANDED, ItemId.AXE);
            case DAGGER -> new Weapon("Dolk", 0.5, WeaponEquip.OFF_HAND, ItemId.DAGGER);
            case BOW -> new Weapon("Bue", 1.5, WeaponEquip.TWO_HANDED, ItemId.BOW);
            case HELMET -> new Armor("Hjelm", 2, ArmorSlot.HEAD, ItemId.HELMET);
            case BREASTPLATE -> new Armor("Brystplade", 8, ArmorSlot.CHEST, ItemId.BREASTPLATE);
            case GREAVES -> new Armor("Benskinner", 3.5, ArmorSlot.LEGS, ItemId.GREAVES);
            case BOOTS -> new Armor("Støvler", 2, ArmorSlot.FEET, ItemId.BOOTS);
            case HEALTH_POTION -> new Consumable("Liv eliksir", 0.2, ItemId.HEALTH_POTION);
            case MANA_POTION -> new Consumable("Mana eliksir", 0.2,  ItemId.MANA_POTION);
            case ARROW -> new Consumable("Pil", 0.1, ItemId.ARROW);
            case BOMB -> new Consumable("Bombe", 0.5, ItemId.BOMB);

            default -> null;
        };
    }

}
