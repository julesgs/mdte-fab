package mdte.fab;

import entite.Custommer;
import entite.Order;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.Modele;

import java.util.List;

public class ApplicationController {
    @FXML
    private Button RefreshButton;

    @FXML
    private ListView<String> orders_listView;

    @FXML
    private TextField numOrder_field, custommer_field, mdte_field, price_field;

    private List<Custommer> _custommers;




    private final Modele modele = new Modele();

    public void initialize() {
        onRefreshButtonClick();
        setNoEditableFields();
    }

    @FXML
    protected void onRefreshButtonClick() {
        List<Order> orders = modele.getOrder();
        orders_listView.getItems().clear();
        for (Order o : orders) {
            orders_listView.getItems().add(o.getID());
        }

        this._custommers = modele.getCustommer();
    }

    @FXML
    protected void onSelectOrder() {
        String selectedItem = (String) orders_listView.getSelectionModel().getSelectedItem();
        Order selectedOrder = modele.getOrderByID(selectedItem);

        String clientID = selectedOrder.getClientID();
        String clientName = modele.getCustommerByID(clientID);

        numOrder_field.setText(selectedOrder.getID());
        custommer_field.setText(clientName);
        mdte_field.setText(selectedOrder.getMdteID());
        price_field.setText(selectedOrder.getTotalPrice());
    }

    protected void setNoEditableFields() {
        numOrder_field.setEditable(false);
        custommer_field.setEditable(false);
        mdte_field.setEditable(false);
        price_field.setEditable(false);
    }
}