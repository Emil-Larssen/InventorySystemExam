package com.consolelogteam.inventorysystem.logik;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Inventory {

    private ObservableList<Item> inventoryList = FXCollections.observableArrayList();
    private ItemFactory itemfactory = new ItemFactory();

    /** Inventory constraints */
    private final int STARTINGINVENTORYSLOTS = 32;

    //Konstant som definerer hvor meget antal pladser stiger med hver gang de øges.
    private final int INCREMENTINCREASESLOTS = 32;

    private final int MAXINVENTORYSLOTS = 192;
    private final double WEIGHTLIMIT = 50.0;

    private int inventorySlotsLimit = STARTINGINVENTORYSLOTS;

    /** Inventory Variables */
    private int slotsFilled = 0;
    private double weightFilled = 0;


    /** Getters */
    public ObservableList<Item> getInventoryList() {
        return inventoryList;
    }


    public int getMaxInventorySlotLimit(){
        return MAXINVENTORYSLOTS;
    }

    //Inventory Slots
    public int getStartingInventorySlots(){
        return STARTINGINVENTORYSLOTS;
    }

    public int getIncrementInventorySlots(){
        return INCREMENTINCREASESLOTS;
    }

    public int getItemSlotsLimit(){
        return inventorySlotsLimit;
    }

    public int getSlotsFilled(){
        return slotsFilled;
    }

    //Weight
    public double getWeightLimit(){
        return WEIGHTLIMIT;
    }

    public double getWeightFilled(){
        return weightFilled;
    }

    public int getInventoryLength(){
        return inventoryList.size();
    }


    /** Setters */
    public void setSlotLimit(int newLimit){
        inventorySlotsLimit = newLimit;
    }

    public void setSlotsFilled(int slotsFilled){
        this.slotsFilled = slotsFilled;
    }

    public void setWeightFilled(double weightFilled){
        this.weightFilled = weightFilled;
    }


    /** Adding and removing Items from Inventory */
    //Tilføjer "Item" til listen
    public void addItem(ItemId itemid) {
        //Der tilføjes en "Item" til listen som laves af et metode kald til "ItemFactory",
        //som bruger et enum af "ItemId" til at lave den specifikke "Item"
        inventoryList.add(itemfactory.createItem(itemid));
    }


    protected void removeItem(int inventoryindex) {
        //ObservableList har også adgang til metoder fra list og kan derfor bare bruge metoden remove
        // som sletter baseret på indeksets værdi
        inventoryList.remove(inventoryindex);  //ændring er sket her siden aflevering
    }


    /** Loading the Saved List */
    public void loadSavedList(ObservableList<Item> savedList){
        //Modtager en 'observablelist' som argument og siger at listen skal være lig med den.
        inventoryList = savedList;
    }


    /** Temporarily make an Item for Logic Purposes */
    public Item tempMakeItem(ItemId itemId){
        //En metode som laver et metode kald til "ItemFactory" for at lave et "Item",
        // der returneres og skal bruges til logik.
         return itemfactory.createItem(itemId);
    }


    /** Sorting of Inventory */
    // bobble sortering Alfabetisk
    public void sortInventoryAlphabetically() {

        //Er relevant i forhold til at starte sorteringen,
        // og hænger sammen med at sortering kan slutte tidligt,
        // hvis intet har behov for at rykkes en hel omgang.
        boolean swappedSomething = true;

        //En løkke som betyder at så længe at der ændres noget i løbet af en omgang,
        //så skal sorteringen fortsætte.
        while (swappedSomething) {

            //Starter med at nulstille, da intet i denne omgang har skulle byttes endnu
            swappedSomething = false;

            //Selve omgangen, som er en for-løkke der går gennem alle elementerne.
            //Den er baseret på længden af listen - 1, da der ellers vil blive sammenlignet, med null i det sidste element.
            for (int i = 0; i < inventoryList.size() - 1; i++) {

                //Metoden '.compareTo' kan sammenligne String(s)
                // Metoden returnere en værdi over 0, hvis den første String er senest i alfabetet.
                // Metoden returnere en værdi lig med 0, hvis begge String(s) er ens, altså samme ord.
                // Metoden returnere en værdi under 0, hvis den anden String er senest i alfabetet.

                //Så hvis navnet af den første "Item" kommer senere i alfabetisk rækkefølge end den anden,
                // så bliver boolean 'swappedSomething' sat til "true"
                if (inventoryList.get(i).getItemName()
                        .compareTo(inventoryList.get(i + 1).getItemName()) > 0) {
                    swappedSomething = true;

                    //Herefter skiftes de to "Item(s)".
                    //Den første "Item" kopieres over i en midlertidig variable kaldet "temp"
                    Item temp = inventoryList.get(i);
                    //Den anden "Item" overskriver den første plads.
                    inventoryList.set(i, inventoryList.get(i + 1));
                    //Herefter bliver den kopierede første "Item" i temp sat ind på den anden plads.
                    //De har hermed byttet plads
                    inventoryList.set(i + 1, temp);

                    //Dette tjek sker for alle elementer i rækken i hver omgang af while-løkken.
                    //While-løkken fortsætter så indtil en hel omgang sker, uden noget skulle byttes.
                }
            }
        }
    }


    // bobble sortering weight
    public void sortInventoryByWeight() {

        //Er relevant i forhold til at starte sorteringen,
        // og hænger sammen med at sortering kan slutte tidligt,
        // hvis intet har behov for at rykkes en hel omgang.
        boolean swappedSomething = true;

        //En løkke som betyder at så længe at der ændres noget i løbet af en omgang,
        //så skal sorteringen fortsætte.
        while (swappedSomething) {

            //Starter med at nulstille, da intet i denne omgang har skulle byttes endnu
            swappedSomething = false;

            //Selve omgangen, som er en for-løkke der går gennem alle elementerne.
            //Den er baseret på længden af listen - 1, da der ellers vil blive sammenlignet, med null i det sidste element.
            for (int i = 0; i < inventoryList.size() - 1; i++) {

                //Definerer 2 variabler til at håndtere, vægten af stakke af "Item"(s),
                // i forhold til "Consumables"
                double weightOfFirst;
                double weightOfSecond;

                //Hvis den første "Item" er af typen "Consumable" så skal der tages højde for stackSize/antal i udregning af vægt.
                if (inventoryList.get(i) instanceof Consumable){
                    weightOfFirst = inventoryList.get(i).getWeight() * ((Consumable) inventoryList.get(i)).getStacksize();
                } else {
                    //Ellers bare tag vægten
                    weightOfFirst = inventoryList.get(i).getWeight();
                }

                //På sammen vis, hvis den anden "Item" er af typen "Consumable" så skal der tages højde for stackSize/antal i udregning af vægt.
                if (inventoryList.get(i+1) instanceof Consumable){
                    weightOfSecond = inventoryList.get(i+1).getWeight() * ((Consumable) inventoryList.get(i+1)).getStacksize();
                } else {
                    //Ellers bare tag vægten
                   weightOfSecond = inventoryList.get(i + 1).getWeight();
                }

                //Tjekker bare om vægten af den første "Item" er højere end den anden,
                //hvis ja, så bliver 'swappedSomething' sat til true.
                if (weightOfFirst > weightOfSecond) {
                    swappedSomething = true;

                    //Herefter skiftes de to Item(s)
                    //Den første "Item" kopieres over i en midlertidig variable kaldet "temp"
                    Item temp = inventoryList.get(i);
                    //Den anden "Item" overskriver den første plads.
                    inventoryList.set(i, inventoryList.get(i + 1));
                    //Herefter bliver den kopierede første "Item" i temp sat ind på den anden plads.
                    //De har hermed byttet plads
                    inventoryList.set(i + 1, temp);

                    //Dette tjek sker for alle elementer i rækken i hver omgang af while-løkken.
                    //While-løkken fortsætter så indtil en hel omgang sker, uden noget skulle byttes.
                }
            }
        }
    }
    // bobble sortering efter type og alfabetisk rækkefølge
    public void sortInventoryByType() {

        //Er relevant i forhold til at starte sorteringen,
        // og hænger sammen med at sortering kan slutte tidligt,
        // hvis intet har behov for at rykkes en hel omgang.
        boolean swappedSomething = true;

        //En løkke som betyder at så længe at der ændres noget i løbet af en omgang,
        //så skal sorteringen fortsætte.
        while (swappedSomething) {

            //Starter med at nulstille, da intet i denne omgang har skulle byttes endnu
            swappedSomething = false;


            //Selve omgangen, som er en for-løkke der går gennem alle elementerne.
            //Den er baseret på længden af listen - 1, da der ellers vil blive sammenlignet, med null i det sidste element.
            for (int i = 0; i < inventoryList.size() - 1; i++) {
                //Variablen a bliver midlertidigt tillagt den første "Item"
                Item a = inventoryList.get(i);
                //Variablen b bliver midlertidigt tillagt den anden "Item"
                Item b = inventoryList.get(i + 1);

                //Metoden '.compareTo' kan sammenligne enums, som sorteres efter rækkefølgen i Enum klassen.
                //Rækkefølgen i "ItemType" er: Weapon, Armor, Consumable

                // Metoden returnere en værdi over 0, hvis det første ItemId er senere i den ovenstående rækkefølge.
                // Metoden returnere en værdi lig med 0, hvis begge ItemId er ens.
                // Metoden returnere en værdi under 0, hvis den anden ItemId er senere i den ovenstående rækkefølge.

                //En integer variabel bliver sat lig med sammenligningen.
                int typeCompare = a.getItemType().compareTo(b.getItemType()); // sortere efter enum itemType rækkefølge
                //En integer variabel oprettes også i tilfælde af at man skal sammenligne navne af "Item(s)"
                int nameCompare = 0;

                //Hvis de sammenlignede "Item(s)" har samme type, så vil der udregnes en sammenligning,
                // I forhold til navnet på hver "Item", baseret på alfabetet.
                if (typeCompare == 0) {
                    // hvis itemType er ens vil den sortere alfabetisk i stedet
                    nameCompare = a.getItemName().compareTo(b.getItemName());
                }


                /* (if) her siger at enten skal det første index være større end det andet (i forhold til typernes enum rækkefølge),
                eller typerne er ens men det første navn er en større værdig end den anden i alfabetet og skal byttes fx("B"compareTo"A")
                hvis en af disse er tilfældet bliver swappedSomething sat til true og sorteringen fortsætter */
                if (typeCompare > 0 || (typeCompare == 0 && nameCompare > 0)) {
                    swappedSomething = true;

                    //Herefter skiftes de to "Item(s)".
                    //Den første "Item" kopieres over i en midlertidig variable kaldet "temp"
                    Item temp = a;
                    //Den anden "Item" overskriver den første plads.
                    inventoryList.set(i, b);
                    //Herefter bliver den kopierede første "Item" i temp sat ind på den anden plads.
                    //De har hermed byttet plads
                    inventoryList.set(i + 1, temp);

                    //Dette tjek sker for alle elementer i rækken i hver omgang af while-løkken.
                    //While-løkken fortsætter så indtil en hel omgang sker, uden noget skulle byttes.
                }
            }
        }
    }




}

