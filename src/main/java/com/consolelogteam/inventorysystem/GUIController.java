package com.consolelogteam.inventorysystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.consolelogteam.inventorysystem.ItemId;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class GUIController {

    private final InventoryManager inventoryManager = new InventoryManager();

    List<ItemId> list = Arrays.asList(ItemId.values());
    ObservableList<ItemId> availableItems = FXCollections.observableArrayList(list);


    @FXML
    private ListView<Item> inventoryListView;

    @FXML
    private ListView<ItemId> itemListView;

    @FXML
    private TextField selectedItemTextField;

    @FXML
    private Button addItemOnClick;

    @FXML
    private Button removeItemOnClick;

    @FXML
    private void addItemOnClick() {
        ItemId selectedId = itemListView.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            addItemToInventory(selectedId);
        }
    }


    @FXML
    public void initialize() {
        //Added loading the saved Inventory as the first part of initialize
        //Proper exception handling is a TO-DO
        inventoryManager.loadingSavedInventory();

        inventoryListView.setItems(inventoryManager.getItemList());

        inventoryListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
                    if (newItem != null) {
                        selectedItemTextField.setText("Valgt item: " + newItem.getItemName());
                    } else {
                        selectedItemTextField.setText("Ingen item valgt");
                    }
                });

        selectedItemTextField.setText("Ingen item valgt");

        //--------------------------------

        itemListView.setItems(availableItems);

    }

    public void addItemToInventory(ItemId itemId) {
        inventoryManager.addItemToInventory(itemId);
        //Added saving the inventory to the addItemFeature
        inventoryManager.savingInventory();
    }
}