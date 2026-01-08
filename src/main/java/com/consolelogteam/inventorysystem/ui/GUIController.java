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

    List<ItemId> list = Arrays.asList(ItemId.values());
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
        //Added loading the saved Inventory as the first part of initialize
        try {
            inventoryManager.loadingSavedInventory();
        } catch (RuntimeException re) {
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Gendannelse af Inventory: " + re.getMessage());
        }

        updateAllInventoryVariables();

        //Added loading of the saved inventory of slots
        try {
            inventoryManager.loadInventorySlots();
        } catch (RuntimeException re) {
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.appendText("\nFejl ved Gendannelse af Inventory Pladser: " + re.getMessage());

            //In case an error has occurred the new calculated inventory slots should be properly saved
            try {
                inventoryManager.saveInventorySlots();
            } catch (RuntimeException re2) {
                errorMessageOutput.appendText("\nFejl ved Gem af Inventory Pladser: " + re2.getMessage());
            }
        }

        updateAllInventoryVariables();

        //------------------------------------
        //Viser valgt item fra inventoryListView i textfield
        inventoryListView.setItems(inventoryManager.getItemList());

        inventoryListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
                    if (newItem != null) {
                        if (newItem instanceof Weapon){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight()) + " kg  -  Våben");
                        }
                        if (newItem instanceof Armor){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight())  + " kg  -  Rustning");
                        }
                        if (newItem instanceof Consumable){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + String.format("%.2f",newItem.getWeight() * ((Consumable) newItem).getStacksize())  + " kg " + " antal: " + ((Consumable) newItem).getStacksize() + "  -  Konsumerbar");
                        }
                    } else {
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        selectedItemTextField.setText("Ingen item valgt");

        //--------------------------------
        //Viser valgt item fra itemListView i textfield
        itemListView.setItems(availableItems);

        itemListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
                    if (newItem != null) {
                        selectedItemTextField.setText("Valgt item: " + newItem.name());
                    } else {
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        selectedItemTextField.setText("Ingen item valgt");

        //-----------------------------------
        sortingCombobox.getItems().addAll("Sorter efter Navn", "Sorter efter Vægt", "Sorter efter Kategori");
    }


    /** Buttons and ComboBoxes */
    @FXML
    private void understoodButtonOnClick(){
        errorMessageAnchorPane.setVisible(false);
        errorMessageOutput.clear();
    }

    @FXML
    private void removeItemOnClick() {
        Item item = inventoryListView.getSelectionModel().getSelectedItem();
        if (item != null) {
            removeItemFromInventory(inventoryListView.getSelectionModel().getSelectedIndex(),
                    inventoryListView.getSelectionModel().getSelectedItem());

            if (item instanceof Consumable){
                inventoryListView.refresh();
                selectedItemTextField.setText("Valgt item:  " + item.getItemName() + "  " + String.format("%.2f",item.getWeight() * ((Consumable) item).getStacksize())  + " kg " + " antal: " + ((Consumable) item).getStacksize() + "  -  Konsumerbar");
            }
        }

    }

    //Tilføjer en item til inventory
    @FXML
    private void addItemOnClick() {
        ItemId selectedId = itemListView.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            addingItemToInventory(selectedId);

            inventoryListView.refresh();
        }
    }


    @FXML
    private void increaseSlotsOnClick(){
        try {
            inventoryManager.increasingSlotsLimit();
            updateAllInventoryVariables();
            try {
                inventoryManager.saveInventorySlots();
            } catch (RuntimeException re) {
                errorMessageOutput.appendText("\nFejl ved Gem af Inventory Pladser: " + re.getMessage());
            }

        } catch (MaxInventorySlotsReachedException misre){
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Forøgelse af Pladser: " + misre.getMessage());
        }
    }


    //Henter sorterings algoritmer fra InventoryManager/inventory.
    @FXML
    private void sortingButtonOnClick() {
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
            inventoryManager.addItemToInventory(itemId);

        } catch (ExceedItemLimitException eile){
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Pladser: " + eile.getMessage());
        } catch (ExceedWeightLimitException ewle){
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Vægt: " + ewle.getMessage());
        }

        //Updating inventory variables
        updateAllInventoryVariables();

        //Added saving the inventory to the addItemFeature
        savingInventory();
    }

    //Fjerner item fra inventory
    private void removeItemFromInventory(int inventoryindex, Item item) {
        inventoryManager.removingItemFromInventory(inventoryindex, item);

        //Updating inventory variables
        updateAllInventoryVariables();

        //Added Saving the inventory to the removeItemFeature
        savingInventory();
    }

    //Updates all inventory variables
    private void updateAllInventoryVariables(){
        inventoryManager.updateSlotsFilled();
        inventoryLimitLabel.setText(inventoryManager.printItemLimit());
        inventoryManager.updateWeightFilled();
        inventoryWeightLabel.setText(inventoryManager.printWeightLimit());
    }

    //Saves the inventory
    private void savingInventory(){
        try {
            inventoryManager.savingInventory();
        } catch (RuntimeException re) {
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Gem af Inventory: " + re.getMessage());
        }
    }
}