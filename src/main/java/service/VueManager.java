package service;

import entite.Order;
import entite.Stock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class VueManager {

    public void showLabelDisponible(Label label, Stock s) {
        label.setStyle("-fx-text-fill: #24bf00;");
        label.setText(s.getID() + "   -   Disponible");
    }

    public void showLabelIndisponible(Label label, Stock s) {
        label.setStyle("-fx-text-fill: red;");
        label.setText(s.getID() + "   -   Indisponible");
    }

    public void showErrorStocks(Label label, Order o) {
        label.setStyle("-fx-text-fill: red;");
        label.setText("Erreur lors du chargement des stocks pour la commande " + o.getID());
    }

    public void setFieldError(TextField tf) {
        tf.setStyle("-fx-border-color: red");
        tf.setText("");
    }

    public void showValueInField(TextField tf, String value) {
        tf.setStyle("-fx-font-style: normal;");
        tf.setText(value);
    }

}
