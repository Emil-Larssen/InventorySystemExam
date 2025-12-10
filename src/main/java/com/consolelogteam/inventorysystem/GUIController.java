package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.consolelogteam.inventorysystem.ItemId;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;
import java.util.List;

public class GUIController {

    private final InventoryManager inventoryManager = new InventoryManager();

    List<ItemId> list = Arrays.asList(ItemId.values());
    ObservableList<ItemId> availableItems = FXCollections.observableArrayList(list);


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

    @FXML
    private void understoodButtonOnClick(){
        errorMessageAnchorPane.setVisible(false);
        errorMessageOutput.clear();
    }


    @FXML
    private void removeItemOnClick() {
        Item item = inventoryListView.getSelectionModel().getSelectedItem();
        if (item != null) {
            removeItemFromInventory(inventoryListView.getSelectionModel().getSelectedIndex());
        }
    }


    //Tilføjer en item til inventory
    @FXML
    private void addItemOnClick() {
        ItemId selectedId = itemListView.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            addItemToInventory(selectedId);
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

    @FXML
    public void initialize() {
        //Added loading the saved Inventory as the first part of initialize
        //TODO Proper exception handling is a TO-DO
        try {
            inventoryManager.loadingSavedInventory();
        } catch (RuntimeException re) {
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Loading: " + re.getMessage());
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
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + newItem.getWeight() + " kg  -  Våben");
                        }
                        if (newItem instanceof Armor){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + newItem.getWeight() + " kg  -  Rustning");
                        }
                        if (newItem instanceof Consumable){
                            selectedItemTextField.setText("Valgt item:  " + newItem.getItemName() + "  " + newItem.getWeight() + " kg  -  Konsumerbar");
                        }
                    } else {
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        selectedItemTextField.setText("Ingen item valgt");

        //--------------------------------
        //Viser valgt item fra itemListView i textfield
        itemListView.setItems(inventoryManager.getItemList());

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


        itemListView.setItems(availableItems);

        //-----------------------------------
        sortingCombobox.getItems().addAll("Sorter efter Navn", "Sorter efter Vægt", "Sorter efter Kategori");
    }


    //Forbinder addItemOnClick med inventory manager
    public void addItemToInventory(ItemId itemId) {
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
    public void removeItemFromInventory(int inventoryindex) {
        inventoryManager.removeItemFromInventory(inventoryindex);

        //Updating inventory variables
        updateAllInventoryVariables();

        //Added Saving the inventory to the removeItemFeature
        savingInventory();
    }


    public void updateAllInventoryVariables(){
        inventoryManager.updateSlotsFilled();
        inventoryLimitLabel.setText(inventoryManager.inventoryItemLimit());
        inventoryManager.updateWeightFilled();
        inventoryWeightLabel.setText(inventoryManager.inventoryWeightLimit());
    }

    public void savingInventory(){
        try {
            inventoryManager.savingInventory();
        } catch (RuntimeException re) {
            errorMessageAnchorPane.setVisible(true);
            errorMessageOutput.setText("Fejl ved Gem: " + re.getMessage());
        }
    }
}