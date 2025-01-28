module mdte.fab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.commons.net;


    opens mdte.fab to javafx.fxml;
    exports mdte.fab;
}