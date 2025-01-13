package mdte.fab;

import entite.Custommer;
import entite.MDTE;
import entite.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.Modele;

import java.io.IOException;
import java.util.List;

public class ApplicationController {
    @FXML
    private Button RefreshButton, custommerDetails_btn;

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
        Custommer client = modele.getCustommerByID(clientID);

        try{
            custommer_field.setStyle("-fx-font-style: normal;");
            String clientName = client.getFirstName() + " " + client.getLastName();
            custommer_field.setText(clientName);
        } catch (Exception e){
            custommer_field.setStyle("-fx-border-color: red");
            custommer_field.setText("");
        }

        try{
            mdte_field.setStyle("-fx-font-style: normal;");
            String mdteID = selectedOrder.getMdteID();
            String mdteName = modele.getMDTEByID(mdteID).getName();
            mdte_field.setText(mdteName);
        } catch (Exception e){
            mdte_field.setStyle("-fx-border-color: red");
            mdte_field.setText("");
        }



        numOrder_field.setText(selectedOrder.getID());


        price_field.setText(selectedOrder.getTotalPrice());
    }

    protected void setNoEditableFields() {
        numOrder_field.setEditable(false);
        custommer_field.setEditable(false);
        mdte_field.setEditable(false);
        price_field.setEditable(false);
    }
}