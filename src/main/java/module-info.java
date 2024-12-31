module mdte.fab {
    requires javafx.controls;
    requires javafx.fxml;


    opens mdte.fab to javafx.fxml;
    exports mdte.fab;
}