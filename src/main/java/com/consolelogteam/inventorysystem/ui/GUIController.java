package com.consolelogteam.inventorysystem.ui;

import com.consolelogteam.inventorysystem.exceptions.ExceedItemLimitException;
import com.consolelogteam.inventorysystem.exceptions.ExceedWeightLimitException;
import com.consolelogteam.inventorysystem.exceptions.MaxInventorySlotsReachedException;
import com.consolelogteam.inventorysystem.logik.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;
import java.util.List;

public class GUIController {

    private final InventoryManager inventoryManager = new InventoryManager();

    //Listen af Enums der bruges til at lave objekter
    List<ItemId> list = Arrays.asList(ItemId.values());
    //Konvertering af ovenstående liste til en observableList,
    // der kan vises i et 'ListView' i scenebuilder
    ObservableList<ItemId> availableItems = FXCollections.observableArrayList(list);


    /** Scenebuilder Elements   */
    @FXML
    private ComboBox<String> sortingCombobox;

    @FXML
    private ListView<Item> inventoryListView;

    @FXML
    private ListView<ItemId> itemListView;

    @FXML
    private TextField selectedItemTextField;

    @FXML
    private Label inventoryLimitLabel;

    @FXML
    private Label inventoryWeightLabel;

    @FXML
    private AnchorPane errorMessageAnchorPane;

    @FXML
    private TextArea errorMessageOutput;


    /**  Code Run On Start-Up */
    @FXML
    public void initialize() {
        //Initialize er en metode som kaldes når programmet starter,

        try {
            //Gendanner inventory ved opstart
            // og kalder derfor metoden "loadingSavedInventory()" i InventoryManager
            inventoryManager.loadingSavedInventory();

            //håndterer eventuelle fejl senere i programmet, de kastes alle som en "RuntimeException",
            //derfor håndteres de all med samme generelle start men har hver sin egen besked
        } catch (RuntimeException re) {

            //Gør fejl-'vinduet/fanen' synlig for at skabe følelsen af en Alert-box eller pop-up
            errorMessageAnchorPane.setVisible(true);

            //Skriver teksten ind i fejlmeddelses-fanen, både den generelle del for en fejl ved gendannelse,
            //samt den specifikke meddelse
            errorMessageOutput.setText("Fejl ved Gendannelse af Inventory: " + re.getMessage());
        }

        //Kalder på en metode længere nede på siden,
        //som kalder på flere metoder der opdaterer vægt og brugte slots i inventory,
        //samt at opdatere de synlige variabler i GUI
        updateAllInventoryVariables();



        try {
            //Kalder på metoden i "InventoryManager" som gendanner antallet af slots
            inventoryManager.loadInventorySlots();

            //håndterer eventuelle fejl senere i programmet, de kastes alle som en "RuntimeException",
            //derfor håndteres de all med samme generelle start men har hver sin egen besked
        } catch (RuntimeException re) {

            //Gør fejl-'vinduet/fanen' synlig for at skabe følelsen af en Alert-box eller pop-up
            errorMessageAnchorPane.setVisible(true);

            //Skriver teksten ind i fejlmeddelses-fanen,
            //både den generelle del for en fejl ved gendannelse af slots,
            //samt den specifikke meddelse
            //Bemærk at det er 'appendText' rettere end 'setText',
            // da flere fejl kan opstå på samme tid og derfor begge skal vises i fejlmeddelsen.
            errorMessageOutput.appendText("\nFejl ved Gendannelse af Inventory Pladser: " + re.getMessage());

            //Udover det ovenstådende hvis der sker en fejl i gendannelse af slots,
            //så vil der kaldes en metode i "InventoryManager",
            // der udregner den nødvendige mængde af slots baseret på antallet af items i "Inventory"

            //Derefter gemmes det nye antal slots ved kald af metoden nedenunder i "InventoryManager"
            try {
                inventoryManager.saveInventorySlots();

                //Hvis der skulle ske en fejl i gem af det nye antal slots så vil der tillægges,
                //en extra fejlmeddelse til fejl-fanen, ved brug af 'appendText'.
            } catch (RuntimeException re2) {
                errorMessageOutput.appendText("\nFejl ved Gem af Inventory Pladser: " + re2.getMessage());
            }
        }

        //Kalder på en metode længere nede,
        //som kalder på flere metoder der opdaterer vægt og brugte slots i inventory,
        //samt at opdatere de synlige variabler i GUI.
        //Den kaldes her igen fordi antallet af slots kan have ændret sig.
        //Set i baksspejlet kunne man umiddelbart bare opdatere,
        //det synlige udprint af inventory slots i GUI.
        updateAllInventoryVariables();

        //------------------------------------
        //Viser valgt item fra inventoryListView i textfield

        //Skaber forbindelsen mellem 'ListView' af inventory i GUI og listen i "Inventory"
        //ved et kald på en metode i "InventoryManager",
        //som får fat på observableList af items
        inventoryListView.setItems(inventoryManager.getItemList());

        //Kode som gør listen interagerbar, sådan at info vises i toppen ved klik af elementer
        //I 'ListView' af inventory, skal der kunne selekteres et element, og
        //der læses/kigges på det valgte element dette er 'getSelectionModel().selectedItemProperty()'.
        //'.addListener' er selve evnen til at observere ændringen.
        //Derefter tager vi højde for den 'nyeItem', da det er det element som klikkes på
        inventoryListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {

                    //Lidt redundant/ligegyldigt men kun hvis det nye klikkede element ikke er null
                    if (newItem != null) {

                        //'String.format(%.2f,___") betyder bare, at der kun må være 2 decimaler

                        //Hvis elementer/Valgte Item er af typen "Weapon" skal teksten/info være som nedestående
                        if (newItem instanceof Weapon){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight()) + " kg  -  Våben");
                        }

                        //Hvis elementer/Valgte Item er af typen "Armor" skal teksten/info være som nedestående
                        if (newItem instanceof Armor){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight())  + " kg  -  Rustning");
                        }

                        //Hvis elementer/Valgte Item er af typen "Consumable" skal teksten/info være som nedestående
                        if (newItem instanceof Consumable){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight() * ((Consumable) newItem).getStacksize())  + " kg " + " antal: " + ((Consumable) newItem).getStacksize() + "  -  Konsumerbar");
                        }

                    } else {
                        //Hvis der ikke er valgt et element/item bliver der i stedet skrevet følgende
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        //Gør at info-linjen starter med at have teksten for ikke at have valgt et element/item
        selectedItemTextField.setText("Ingen item valgt");

        //--------------------------------
        //Viser valgt item-ID i form af Enums fra itemListView i textfield
        itemListView.setItems(availableItems);


        //Kode som gør listen interagerbar, sådan at info vises i toppen ved klik af elementer
        //I 'ListView' af enums fra "ItemId", skal der kunne selekteres et element, og
        //der læses/kigges på det valgte element dette er 'getSelectionModel().selectedItemProperty()'.
        //'.addListener' er selve evnen til at observere ændringen.
        //Derefter tager vi højde for den 'ItemId', da det er det element som klikkes på
        itemListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {

                    //Hvis det valgte "ItemId" ikke er null vises navnet i info-linjen
                    if (newItem != null) {
                        selectedItemTextField.setText("Valgt item: " + newItem.name());

                        //Ellers vises at ingen item er valgt
                    } else {
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        //Gør at ved opstart sættes info-linjen til nedenstående.
        //Teknisk set ligegyldig, da den allerede ovenstådende har samme linje
        selectedItemTextField.setText("Ingen item valgt");


        //-----------------------------------
        //Opretter mulighederne i combo-boxen
        sortingCombobox.getItems().addAll("Sorter efter Navn", "Sorter efter Vægt", "Sorter efter Kategori");
    }


    /** Buttons and ComboBoxes */

    //Metoden til at lukke fejl-meddelsen ved at sige at man har forstået
    @FXML
    private void understoodButtonOnClick(){
        //Gør fejl-vinduet/fanen usynlig
        errorMessageAnchorPane.setVisible(false);
        //Sletter den nuværende fejl-meddelse
        errorMessageOutput.clear();
    }

    //Metoden til at slette en item fra 'inventory'
    @FXML
    private void removeItemOnClick() {
        //Definerer et lokalt objekt af item på baggrund af det selekterede/valgte Item i 'ListView'
        Item item = inventoryListView.getSelectionModel().getSelectedItem();

        //Hvis dette item ikke er null (hvis intet er klikket på)
        if (item != null) {

            //Kalder på en metode længere nede som modtager index (pladsen af det valgte item i listen)
            //og "ItemId" af det valgte Item.
            //Set i bakspejlet havde det givet langt mere mening bare at sende objektet af Item med videre,
            //da det er Item der bruges til logiske tjek senere.
            removeItemFromInventory(inventoryListView.getSelectionModel().getSelectedIndex(),
                    inventoryListView.getSelectionModel().getSelectedItem().getItemId());

            //Hvis det valgte item er af typen "Consumable", så er det muligt at observableList ikke observere en ændring,
            //i tilfælde hvor der kun ændres på attributten "stackSize", så vi opdatere listen manuelt,
            //ved brug af 'ListViews' metode til at opdatere med '.refresh()'.
            if (item instanceof Consumable){
                inventoryListView.refresh();

                //Info-linjen opdateres også manuelt af sammenlignelige årsager
                selectedItemTextField.setText("Valgt item:  " + item.getItemName() + "  " + String.format("%.2f",item.getWeight() * ((Consumable) item).getStacksize())  + " kg " + " antal: " + ((Consumable) item).getStacksize() + "  -  Konsumerbar");
            }
        }

    }

    //Tilføjer en item til inventory
    @FXML
    private void addItemOnClick() {

        //Definerer et lokalt enum af typen "ItemId" baseret på det valgte element i 'ListView' af Items
        ItemId selectedId = itemListView.getSelectionModel().getSelectedItem();

        //Hvis det valgte element ikke er null
        if (selectedId != null) {
            //Kalder på en metode længere nede til at tilføje et Item,
            // og sender argumentet i form af "ItemId" med
            addingItemToInventory(selectedId);

            //Vi kan ikke være sikre på om det er et "Item" af typen "Consumable",
            // så vi gør at 'ListView' manuelt opdaterer ved brug af dens metode '.refresh()'.
            //Ved ændring af 'stackSize' er der nemlig ikke automatisk opdatering.
            inventoryListView.refresh();
        }
    }

    //Forøger mængden af item slots/pladser
    @FXML
    private void increaseSlotsOnClick(){
        try {
            //Kalder på en metode i "InventoryManager" der forsøger at forøge mængden af Inventory Pladser
            inventoryManager.increasingSlotsLimit();

            //Kalder på en metode længere nede,
            //som kalder på flere metoder der opdaterer vægt og brugte slots i inventory,
            //samt at opdatere de synlige variabler i GUI.
            //Set i bakspejlet kunne man nok bare opdaterer udprintet af slots i GUI.
            updateAllInventoryVariables();

            try {
                //Kalder på en metode i "InventoryManager" der forsøger gemme det nye antal maksimale slots
                inventoryManager.saveInventorySlots();

                //Fanger fejl i forbindelse med at der sker fejl ved at gemme slots
            } catch (RuntimeException re) {

                //Der bliver brugt 'appendText',
                // men der kan umiddelbart ikke opstå flere fejl på samme tid, så potentielt unødvendigt.
                //Det er ærlig talt svært at få en fejl her, men umiddelbart hvis der skete en fejl,
                //så ville der faktisk ikke dukke noget fejl-vindue/fane op, da den ikke bliver gjort synlig.
                //Der ses i hvert fald ikke umiddelbart.
                errorMessageOutput.appendText("\nFejl ved Gem af Inventory Pladser: " + re.getMessage());
            }

            //Fanger fejl i forbindelse med at have ramt det maksimale antal slots
        } catch (MaxInventorySlotsReachedException misre){

            //Gør fejl-vinduet synlig
            errorMessageAnchorPane.setVisible(true);

            //Sætter fejl-meddelsen i fejl-vinduet
            errorMessageOutput.setText("Fejl ved Forøgelse af Pladser: " + misre.getMessage());
        }
    }


    //Henter sorterings algoritmer fra InventoryManager/inventory.
    @FXML
    private void sortingButtonOnClick() {
        //Switch case som sender en metode kald til "InventoryManager",
        //baseret på hvilken mulighed i combo-boxen der vælges

        switch (sortingCombobox.getValue()) {
            case "Sorter efter Navn":
                inventoryManager.sortingAfterName();
                break;
            case "Sorter efter Vægt":
                inventoryManager.sortingAfterWeight();
                break;
            case "Sorter efter Kategori":
                inventoryManager.sortInventoryByType();
                break;
            default:
        }
    }


    /** Commonly Used Defined Methods */
    //Forbinder addItemOnClick med inventory manager
    private void addingItemToInventory(ItemId itemId) {
        try {
            //Forsøger at kalde på metoden '.addItemToInventory' i "InventoryManager",
            //hvor der bliver sendt et enum fra "ItemId" med som argument.
            inventoryManager.addItemToInventory(itemId);

            //Ved overskredet maximum af slots fanges denne fejl
        } catch (ExceedItemLimitException eile){

            //Gør fejl-vinduet synlig
            errorMessageAnchorPane.setVisible(true);

            //Sætter fejl-meddelsen i fejl-vinduet
            errorMessageOutput.setText("Fejl ved Pladser: " + eile.getMessage());

            //Ved for meget vægt fanges denne fejl
        } catch (ExceedWeightLimitException ewle){

            //Gør fejl-vinduet synlig
            errorMessageAnchorPane.setVisible(true);

            //Sætter fejl-meddelsen i fejl-vinduet
            errorMessageOutput.setText("Fejl ved Vægt: " + ewle.getMessage());
        }

        //Kalder på en metode længere nede,
        //som kalder på flere metoder der opdaterer vægt og brugte slots i inventory,
        //samt at opdatere de synlige variabler i GUI.
        updateAllInventoryVariables();

        //Kalder på en metode længere nede,
        //som forsøger at gemme 'inventory'
        savingInventory();
    }

    //Fjerner item fra inventory
    private void removeItemFromInventory(int inventoryindex, ItemId itemId) {

        //Kalder på en metode i "InventoryManager" som sender 2 argumenter,
        //pladseringen af Item i listen i form af en integer,
        //og et enum af "ItemId", der skal bruges til logik.
        inventoryManager.removeItemFromInventory(inventoryindex, itemId);


        //Kalder på en metode længere nede,
        //som kalder på flere metoder der opdaterer vægt og brugte slots i inventory,
        //samt at opdatere de synlige variabler i GUI.
        updateAllInventoryVariables();

        //Kalder på en metode længere nede,
        //som forsøger at gemme 'inventory'
        savingInventory();
    }

    //Opdaterer variabler og udprint i GUI
    private void updateAllInventoryVariables(){
        //Kalder en metode i "InventoryManager",
        // der opdaterer variablen om optagede pladser i "Inventory"
        inventoryManager.updateSlotsFilled();

        //Sætter printet af brugte og maksimale slots/pladser lig med opdaterede værdier,
        //ved et kald til en metode i "InventoryManager"
        inventoryLimitLabel.setText(inventoryManager.printItemLimit());

        //Kalder en metode i "InventoryManager",
        // der opdaterer variablen om brugt vægt i "Inventory"
        inventoryManager.updateWeightFilled();

        //Sætter printet af brugte og maksimale vægt lig med opdaterede værdier,
        //ved et kald til en metode i "InventoryManager"
        inventoryWeightLabel.setText(inventoryManager.printWeightLimit());
    }

    //Gemmer indholdet af "Inventory"
    private void savingInventory(){
        try {
            //Forsøger at gemme indholdet ved et metodekald til "InventoryManager"
            inventoryManager.savingInventory();

            //Fanger flere forskellige fejl på samme måde,
            // der alle er blevet kastet videre som RuntimeExceptions
        } catch (RuntimeException re) {

            //Fejl-vinduet bliver gjort synligt
            errorMessageAnchorPane.setVisible(true);

            //Fejl-meddelsen bliver sat, første del med det generelle,
            // og derefter den specifikke del til slut, der beskriver den specifikke fejl
            errorMessageOutput.setText("Fejl ved Gem af Inventory: " + re.getMessage());
        }
    }
}