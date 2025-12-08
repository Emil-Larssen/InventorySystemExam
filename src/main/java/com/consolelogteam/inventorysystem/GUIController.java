package com.consolelogteam.inventorysystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.consolelogteam.inventorysystem.ItemId;

public class GUIController {
    @FXML
    private ListView<Item> inventoryListView;

    @FXML
    private TextField selectedItemTextField;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> WeaponsBox;
    @FXML
    private ComboBox<String> ConsumeableBox;
    @FXML
    private ComboBox<String> ArmorBox;

    @FXML
    private Button removeItemOnClick;



    private final InventoryManager inventoryManager = new InventoryManager();

    @FXML
    private void onWeaponClick() {
        String name = txtName.getText().trim();
        String tema = WeaponsBox.getValue();

    }

    @FXML
    public void initialize() {
        inventoryListView.setItems(inventoryManager.getItemList());

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

        // fyld comboboxen med Tekst, når viewet loades
        WeaponsBox.getItems().addAll("Sværd", "Pistol", "MagiskStav");
        ConsumeableBox.getItems().addAll("ManaPotions", "HealthPotions", "Pile");
        ArmorBox.getItems().addAll("Hjelm", "Rustning", "IronPants");
    }


}



