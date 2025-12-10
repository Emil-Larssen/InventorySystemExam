package com.consolelogteam.inventorysystem;

public class ItemFactory {

    public Item createItem(ItemId itemId) {

        return switch (itemId){
            case SWORD -> new Weapon("Sværd", 3, WeaponEquip.MAIN_HAND);
            case AXE -> new Weapon("Økse", 4.5, WeaponEquip.TWO_HANDED);
            case DAGGER -> new Weapon("Dolk", 0.5, WeaponEquip.OFF_HAND);
            case BOW -> new Weapon("Bue", 1.5, WeaponEquip.TWO_HANDED);
            case HELMET -> new Armor("Hjelm", 2, ArmorSlot.HEAD);
            case BREASTPLATE -> new Armor("Brystplade", 8, ArmorSlot.CHEST);
            case GREAVES -> new Armor("Benskinner", 3.5, ArmorSlot.LEGS);
            case BOOTS -> new Armor("Støvler", 2, ArmorSlot.FEET);
            case HEALTH_POTION -> new Consumable("Liv eliksir", 0.2);
            case MANA_POTION -> new Consumable("Mana eliksir", 0.2);
            case ARROW -> new Consumable("Pil", 0.1);
            case BOMB -> new Consumable("Bombe", 0.5);

            default -> null;
        };
    }

}
