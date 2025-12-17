module com.consolelogteam.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.consolelogteam.inventorysystem to javafx.fxml;
    exports com.consolelogteam.inventorysystem;
    exports com.consolelogteam.inventorysystem.exceptions;
    opens com.consolelogteam.inventorysystem.exceptions to javafx.fxml;
    exports com.consolelogteam.inventorysystem.ui;
    opens com.consolelogteam.inventorysystem.ui to javafx.fxml;
    exports com.consolelogteam.inventorysystem.logik;
    opens com.consolelogteam.inventorysystem.logik to javafx.fxml;
    exports com.consolelogteam.inventorysystem.dal;
    opens com.consolelogteam.inventorysystem.dal to javafx.fxml;
}