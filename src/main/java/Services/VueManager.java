package Services;

import entite.Order;
import entite.Stock;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VueManager {

    public void showLabelDisponible(Label label, Stock s) {
        label.setStyle("-fx-text-fill: #24bf00;");
        label.setText(s.getIDOption() + "   -   Disponible");
    }

    public void showLabelIndisponible(Label label, Stock s) {
        label.setStyle("-fx-text-fill: red;");
        label.setText(s.getIDOption() + "   -   Indisponible");
    }

    public void showErrorStocks(Label label, Order o) {
        label.setStyle("-fx-text-fill: red;");
        label.setText("Erreur lors du chargement des stocks pour la commande " + o.getID());
    }

    public void setFieldError(TextField tf) {
        tf.setStyle("-fx-border-color: red");
        tf.setText("");
    }

    public void setFieldError2(TextField tf) {
        tf.setStyle("-fx-border-color: red");
    }

    public void reEnableField(TextField tf) {
        tf.setStyle("-fx-font-style: normal;");
    }

    public void showError(Label label, String message) {
        label.setStyle("-fx-text-fill: red;");
        label.setText(message);
    }

    public void showValueInField(TextField tf, String value) {
        tf.setStyle("-fx-font-style: normal;");
        tf.setText(value);
    }

    public void deleteError(Label label) {
        label.setText("");
    }

    public void disableField(TextField tf) {
        tf.setDisable(true);
    }

    public void enableField(TextField tf) {
        tf.setDisable(false);
    }


}
