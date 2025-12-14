module com.consolelogteam.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.consolelogteam.inventorysystem to javafx.fxml;
    exports com.consolelogteam.inventorysystem;
    exports com.consolelogteam.inventorysystem.Exceptions;
    opens com.consolelogteam.inventorysystem.Exceptions to javafx.fxml;
    exports com.consolelogteam.inventorysystem.Model;
    opens com.consolelogteam.inventorysystem.Model to javafx.fxml;
}