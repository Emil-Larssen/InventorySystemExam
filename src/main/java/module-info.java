module com.consolelogteam.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.consolelogteam.inventorysystem to javafx.fxml;
    exports com.consolelogteam.inventorysystem;
}