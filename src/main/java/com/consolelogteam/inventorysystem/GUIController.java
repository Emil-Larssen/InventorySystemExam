package com.consolelogteam.inventorysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GUIController {
    @FXML
    private ListView<Item> inventoryListView;

    @FXML
    private TextField selectedItemTextField;

    @FXML
    private Button removeItemOnClick;

    private final InventoryManager inventoryManager = new InventoryManager();

    @FXML
    public void initialize() {
        inventoryListView.setItems(inventoryManager.getAllItems());

        inventoryListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
                    if (newItem != null) {
                        selectedItemTextField.setText("Valgt frugt: " + newItem.getItemName());
                    } else {
                        selectedItemTextField.setText("Ingen frugt valgt");
                    }
                });

        selectedItemTextField.setText("Ingen frugt valgt");
    }
}
