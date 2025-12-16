module com.consolelogteam.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.consolelogteam.inventorysystem to javafx.fxml;
    exports com.consolelogteam.inventorysystem;
    exports com.consolelogteam.inventorysystem.Exceptions;
    opens com.consolelogteam.inventorysystem.Exceptions to javafx.fxml;
    exports com.consolelogteam.inventorysystem.UI;
    opens com.consolelogteam.inventorysystem.UI to javafx.fxml;
    exports com.consolelogteam.inventorysystem.Logik;
    opens com.consolelogteam.inventorysystem.Logik to javafx.fxml;
    exports com.consolelogteam.inventorysystem.Dal;
    opens com.consolelogteam.inventorysystem.Dal to javafx.fxml;
}