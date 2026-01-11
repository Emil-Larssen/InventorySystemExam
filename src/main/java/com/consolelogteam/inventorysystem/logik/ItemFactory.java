package com.consolelogteam.inventorysystem.logik;

public class ItemFactory {

    //Et samlet factory/fabrik som samler al oprettelse af "Item" et sted.
    public Item createItem(ItemId itemId) {

        //Der bruges lambda expression i et return switch,
        // for at gøre den mere læsbar og overskuelig.
        //Kig længere nede for at se hvordan den ellers ville have set ud.
        //Bliver også kaldt "enhanced switch" af intellij


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




/* switch (itemId){
            case SWORD:
                return new Weapon("Sværd", 3, WeaponEquip.MAIN_HAND,ItemId.SWORD);
            case AXE:
                return new Weapon("Økse", 4.5, WeaponEquip.TWO_HANDED, ItemId.AXE);
            case DAGGER:
                return new Weapon("Dolk", 0.5, WeaponEquip.OFF_HAND, ItemId.DAGGER);
            case BOW:
                return new Weapon("Bue", 1.5, WeaponEquip.TWO_HANDED, ItemId.BOW);
            case HELMET:
                return new Armor("Hjelm", 2, ArmorSlot.HEAD, ItemId.HELMET);
            case BREASTPLATE:
                return new Armor("Brystplade", 8, ArmorSlot.CHEST, ItemId.BREASTPLATE);
            case GREAVES:
                return new Armor("Benskinner", 3.5, ArmorSlot.LEGS, ItemId.GREAVES);
            case BOOTS:
                return new Armor("Støvler", 2, ArmorSlot.FEET, ItemId.BOOTS);
            case HEALTH_POTION:
                return new Consumable("Liv eliksir", 0.2, ItemId.HEALTH_POTION);
            case MANA_POTION:
                return new Consumable("Mana eliksir", 0.2,  ItemId.MANA_POTION);
            case ARROW:
                return new Consumable("Pil", 0.1, ItemId.ARROW);
            case BOMB:
                return new Consumable("Bombe", 0.5, ItemId.BOMB);

            default:  return null;}}}

         */

