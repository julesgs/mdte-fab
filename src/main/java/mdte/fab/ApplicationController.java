package mdte.fab;

import entite.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import service.Modele;

import java.util.List;

public class ApplicationController {
    @FXML
    private Button RefreshButton;

    @FXML
    private ListView<String> orders_listView;

    private final Modele modele = new Modele();

    @FXML
    protected void onRefreshButtonClick() {
        List<Order> orders = modele.getOrder();
        for (Order o : orders) {
            orders_listView.getItems().add(o.get_id());
        }
        System.out.println(orders);
    }
}