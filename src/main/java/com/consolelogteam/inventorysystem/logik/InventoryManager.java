package com.consolelogteam.inventorysystem.logik;

import com.consolelogteam.inventorysystem.exceptions.ExceedItemLimitException;
import com.consolelogteam.inventorysystem.exceptions.ExceedWeightLimitException;
import com.consolelogteam.inventorysystem.exceptions.MaxInventorySlotsReachedException;
import com.consolelogteam.inventorysystem.dal.Persistence;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InventoryManager {

    /** Objects */
    private Inventory inventory = new Inventory();

    //Persistence object added to interact with the class
    private Persistence persistence = new Persistence();


    /** Returning the Inventory List */
    public ObservableList<Item> getItemList() {
        return inventory.getInventoryList();
    }

    /** Loading and Saving Inventory Items*/
    //Works as a link between persistence and inventory
    public void loadingSavedInventory() {
        //Kalder på metoden 'loadListOfItems()' i "Persistence", som returnerer en 'observableList'
        //Der gives videre som argument i metodekaldet 'loadSavedList()' i "Inventory"
        inventory.loadSavedList(persistence.loadListOfItems());
    }

    public void savingInventory() {
        //Kalder på metoden 'getInventoryList()' i "Inventory", som returnerer en 'observableList'
        //Der gives videre som argument i metodekaldet 'saveListOfItems()' i "Persistence"
        persistence.saveListOfItems(inventory.getInventoryList());
    }


    /** Loading and Saving Inventory Slots */
    public void loadInventorySlots(){
        try {
            //Forsøger at modtage en integer fra et metode kald til "Persistence", som er mængden af optjente slots/pladser
            int loadedInventorySlots = persistence.loadAmountOfInventorySlots();

            //Tjekker om den modtagede integer er mere eller lig med, hvor mange slots/pladser man starter med
            //Og tjekker derefter om den modtagede integer er mindre eller lig med det maksimale antal slots/pladser man kan have.
            if (loadedInventorySlots >= inventory.getStartingInventorySlots() && loadedInventorySlots <= inventory.getMaxInventorySlotLimit()){

                //Tjekker om den modtagede integer er større eller lig med mængden af brugte slots/pladser
                if (loadedInventorySlots >= inventory.getSlotsFilled()){

                    //Sætter antallet af slots til det modtagede ved et kald til metoden 'setSlotLimit' i "Inventory"
                    inventory.setSlotLimit(loadedInventorySlots);
                } else {

                    //Hvis der ikke er nok pladser i forhold til mængden af slots/pladser kaldes en metode længere nede,
                    //til at udregne den nødvendige mængde og fastsætte det nye slotLimit ved et kald til metoden 'setSlotLimit' i "Inventory"
                    inventory.setSlotLimit(calculateNeededSlots());

                    //Der kastes en "RuntimeException" som fanges i GUI Controller.
                    throw new RuntimeException("Der blev fundet et antal inventory pladser som var mindre end det nødvendige");
                }
            } else {
                //Hvis der er for mange eller for få slots/pladser i forhold til max og min, kaldes en metode længere nede,
                //til at udregne den nødvendige mængde og fastsætter det nye slotLimit ved et kald til metoden 'setSlotLimit' i "Inventory"
                inventory.setSlotLimit(calculateNeededSlots());

                //Der kastes en "RuntimeException" som fanges i GUI Controller.
                throw new RuntimeException("Der blev fundet et antal inventory pladser som er under minimum eller over maksimum");
            }

        } catch (FileNotFoundException fnfe){
            //Hvis der sker en fejl i form af ikke at kunne finde filen, kaldes en metode længere nede,
            //til at udregne den nødvendige mængde og fastsætte det nye slotLimit ved et kald til metoden 'setSlotLimit' i "Inventory"
            inventory.setSlotLimit(calculateNeededSlots());

            //Der kastes en "RuntimeException" som fanges i GUI Controller.
            throw new RuntimeException("Der blev ikke fundet en fil til gemte inventory pladser");

        } catch (IOException ioe){
            //Hvis der sker en generel IO fejl, kaldes en metode længere nede,
            //til at udregne den nødvendige mængde og fastsætte det nye slotLimit ved et kald til metoden 'setSlotLimit' i "Inventory"
            inventory.setSlotLimit(calculateNeededSlots());

            //Der kastes en "RuntimeException" som fanges i GUI Controller.
            throw new RuntimeException("Der gik noget galt i forbindelse med at gendanne inventory pladser");

        } catch (NumberFormatException nfe){
            //Hvis der sker en fejl ved konvertering af inholdet af filen til en Integer, kaldes en metode længere nede,
            //til at udregne den nødvendige mængde og fastsætte det nye slotLimit ved et kald til metoden 'setSlotLimit' i "Inventory"
            inventory.setSlotLimit(calculateNeededSlots());

            //Der kastes en "RuntimeException" som fanges i GUI Controller.
            throw new RuntimeException("Der blev ikke fundet et heltal i den gemte fil");
        }
    }

    public void saveInventorySlots(){
        //Et kald af metoden 'getItemSlotsLimit()' i "Inventory" returnere en Integer,
        //som gemmes i en fil af metoden 'saveAmountOfInventorySlots()' i "Persistence"
        persistence.saveAmountOfInventorySlots(inventory.getItemSlotsLimit());
    }

    /** Calculating the Needed Slots in case of File Error */
    public int calculateNeededSlots(){
        //Udregner den nødvendige mængde af slots/pladser i forbindelse med fejl ved indlæsning.
        //Variablen 'calculatedSlots' starter med at sættes til standard start antallet af slots/pladser
        int calculatedSlots = inventory.getStartingInventorySlots();

        //Herefter er der en while-løkke der så længe at der er mindre maksimale pladser end brugte pladser,
        //Vil der blive lagt det specificerede 'increment' til, som ligger defineret i "Inventory"
        while(calculatedSlots < inventory.getSlotsFilled()){
            calculatedSlots += inventory.getIncrementInventorySlots();
        }
        //Når løkken er ovre vil det endelige antal returneres
        return calculatedSlots;
    }


    /** Adding and Removing Items from Inventory */
    public void addItemToInventory(ItemId itemId) {

        //Primært en boolean variabel i forhold til "Consumable",
        //i forhold til om den allerede eksisterer i listen.
        boolean existsInInventory = false;

        //Tjekker om typen af "Item" er "Consumble", ved at lave et midlertidigt Item,
        // ved et metodekald til "Inventory" som returnere et "Item"
        if (inventory.tempMakeItem(itemId) instanceof Consumable){

            //Tjekker om vægten ville overskride grænsen hvis vægten af den nye consumable blev lagt oveni.
            //Tjekker ved at se hvor meget vægt der er brugt lagt sammen med den nye vægt og om det er mindre eller lig med max.
            //Set i bakspejlet havde det nok været smartere at definere det midlertidige "Item" som en lokal variabel,
            // i stedet for at lave 2 separate midlertidige objekter.
            if (inventory.getWeightFilled() + (inventory.tempMakeItem(itemId).getWeight()) <= inventory.getWeightLimit()) {

                //Hvis vægten passer tjekkes hele listen igennem med en for-each-løkke
                for (Item item : inventory.getInventoryList()) {
                    //Tjekker om det specifikke "Item" i listen er af typen "Consumable"
                    if (item instanceof Consumable) {
                        //Tjekker om de har samme "ItemId"
                        if (item.getItemId() == itemId) {
                            //Hvis ja, bliver 'stackSize' forøget med 1, ved en metode i "Consumable"
                            ((Consumable) item).incrementStacksize();
                            //Derudover noteres det at den allerede eksisterer
                            existsInInventory = true;
                        }
                    }
                }
            }
        }
        //Hvis den ikke allerede eksisterer
        if (!existsInInventory) {
            //Tjekker om brugte slots/pladser er mindre end antallet af maksimale slots/pladser
            if (inventory.getSlotsFilled() < inventory.getItemSlotsLimit()) {
                //Tjekker om vægten ville overskride grænsen hvis vægten af den nye consumable blev lagt oveni.
                //Tjekker ved at se hvor meget vægt der er brugt lagt sammen med den nye vægt og om det er mindre eller lig med max.
                //Set i bakspejlet havde det nok været smartere at definere det midlertidige "Item" som en lokal variabel,
                // i stedet for at lave nu 3 separate midlertidige objekter.
                if (inventory.getWeightFilled() + (inventory.tempMakeItem(itemId).getWeight()) <= inventory.getWeightLimit()) {
                    //Hvis ja, laves og lægges det nye "Item" i listen,
                    //ved et kald til metoden 'addItem' med argumentet i form af et "ItemId"
                    inventory.addItem(itemId);
                } else {
                    //Hvis det ville overskride den maksimale vægt kastes der en fejl som modtages og håndteres i "GUIController"
                    throw new ExceedWeightLimitException("Du må ikke overskride den maksimale vægt");
                }
            } else {
                //Hvis det ville overskride den maksimale antal slots/pladser kastes der en fejl som modtages og håndteres i "GUIController"
                throw new ExceedItemLimitException("Du må ikke overskride det maksimale antal items");
            }
        }
    }

    public void removeItemFromInventory(int inventoryIndex, ItemId itemId) {
        //Primært en boolean variabel i forhold til "Consumable",
        // der er i forhold til om den stadig skal eksistere (fordi stacksize bare gik ned),
        // eller om den helt slettes
        boolean existsInInventory = false;

        //Tjekker om typen af "Item" er "Consumble", ved at lave et midlertidigt Item,
        // ved et metodekald til "Inventory" som returnere et "Item"
        //Det bør overvejes at hele balladen med at lave et midlertidigt "Item",
        //ved brug af metoden 'tempMakeItem' set i bakspejlet kunne være undgået hvis "Item"-et
        //havde været taget med videre som argument rettere end dets "ItemId" i tidligere dele af programmet.
        if (inventory.tempMakeItem(itemId) instanceof Consumable){

            //Listen af items gennemgås ved et for-each-løkke
            for (Item item : inventory.getInventoryList()){
                //Tjekker om den specifikke "Item" er af typen "Consumable"
                if (item instanceof Consumable){
                    //Tjekker om den specifikke "Item" har samme "ItemId" (kunne igen have tjekket et "Item" i stedet)
                    if (item.getItemId() == itemId){
                        //Hvis ja bliver 'stackSize' sat ned med 1 ved en metode i "Consumable"
                        ((Consumable) item).decrementStacksize();
                        //Tjekker om 'stackSize' ikke er lig med nul, for så skal den stadig eksisterer.
                        //Ellers skal den slettes.
                        //Man burde nok bare tjekke om den er mere end 0, dog burde det ikke være et problem i den nuværende kode.
                        if (((Consumable) item).getStacksize() != 0) {
                            existsInInventory = true;
                        }
                    }
                }
            }
        }
        //Hvis der ikke er tale om et consumable, eller hvis 'stackSize' er 0 så slettes den specifikke "Item",
        //ved et kald til metoden 'removeItem()' i "Inventory" som sletter baseret på indekset, altså pladsen i listen.
        if (!existsInInventory) {
            inventory.removeItem(inventoryIndex);
        }
    }

    /** Update Slots Filled and Formating the Limit */
    public void updateSlotsFilled() {
        //Opdaterer antallet af brugte slots, ret simpelt ved at,
        //kalde en metode i "Inventory" som returnere længden af listen,
        // og derefter kaldes en anden metode i "Inventory",
        // som sætter brugte slots/pladser lig med det antal
        inventory.setSlotsFilled(inventory.getInventoryLength());
    }

    public String printItemLimit() {
        //Returnere en String som udgør printet af brugte og maksimale slots, som GUI viser.
        return "Pladser: " + inventory.getSlotsFilled() + " / " + inventory.getItemSlotsLimit();
    }

    /** Update Weight Filled and Formating the Limit */
    public void updateWeightFilled() {
        //En lokal variabel for den samlede vægt oprettes.
        double weight = 0;

        //En for-each-løkke går alle "Itemo(s)" igennem.
        for (Item item : inventory.getInventoryList()) {
            //Hvis "Item" er af typen "Consumable" ganges vægten med stacksize
            if (item instanceof Consumable){
                weight += item.getWeight() * ((Consumable) item).getStacksize();
            } else {
                //Ellers lægges bare vægten til
                weight += item.getWeight();
            }
        }

        //Til sidst sættes den brugte vægt til den udregnede vægt,
        // ved et metode kald til "Inventory" med den samlede vægt som argument.
        inventory.setWeightFilled(weight);
    }

    public String printWeightLimit() {
        //Returnere en String som udgør printet af brugte og maksimale slots, som GUI viser.
        return "Vægt: " + String.format("%.2f",inventory.getWeightFilled())  + " kg" + " / " + String.format("%.2f",inventory.getWeightLimit()) + " kg";
    }


    /** Increasing the Slot Limit */
    public void increasingSlotsLimit(){
        //Tjekker om det nuværende maksimale antal slots/pladser lagt sammen med det definerede 'increment',
        // er mindre eller lig med det absolutte maksimum af antal slots/pladser.
        if (inventory.getItemSlotsLimit() + inventory.getIncrementInventorySlots() <= inventory.getMaxInventorySlotLimit()){

            //Hvis det er bliver det nye maksimale antal sat lig med det nuværende maksimale antal lagt sammen med det definerede 'increment'
            inventory.setSlotLimit(inventory.getItemSlotsLimit()+inventory.getIncrementInventorySlots());
        } else {

            //Hvis ikke kastes der en fejl som modtages og håndteres i "GUIController"
            throw new MaxInventorySlotsReachedException("Du kan ikke forøge inventory pladser til mere end " + inventory.getMaxInventorySlotLimit());
        }
    }


    /** Sorting */

    public void sortingAfterName() {
        //Kald af metoden til at sortere alfabetisk i "Inventory"
        inventory.sortInventoryAlphabetically();
    }

    public void sortingAfterWeight() {
        //Kald af metoden til at sortere efter vægt i "Inventory"
        inventory.sortInventoryByWeight();
    }

    public void sortInventoryByType() {
        //Kald af metoden til at sortere først efter type og derefter alfabetisk i "Inventory"
        inventory.sortInventoryByType();
    }

}
