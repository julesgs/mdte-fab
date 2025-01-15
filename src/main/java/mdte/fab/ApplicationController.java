package mdte.fab;

import entite.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import service.Modele;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ApplicationController {
    @FXML
    private Button RefreshButton;

    @FXML
    private Label selectOrder_label, fabricationOrder_label, option_1_label, option_2_label, option_3_label;

    @FXML
    private ListView<String> orders_listView, options_listView, stocks_listView;

    @FXML
    private TextField numOrder_field, custommer_field, mdte_field, price_field;

    @FXML
    private Rectangle rectangle;

    private List<Custommer> _custommers;


    private final Modele modele = new Modele();

    public void initialize() {
        onRefreshButtonClick();
        setNoEditableFields();
    }

    @FXML
    protected void onRefreshButtonClick() {
        selectOrder_label.setVisible(true);
        fabricationOrder_label.setVisible(false);

        option_1_label.setVisible(false);
        option_2_label.setVisible(false);
        option_3_label.setVisible(false);
        rectangle.setVisible(false);


        List<Order> orders = modele.getOrder();
        orders_listView.getItems().clear();
        for (Order o : orders) {
            orders_listView.getItems().add(o.getID());
        }
        this._custommers = modele.getCustommer();

        List<Stock> lesStocks = modele.getStocks();
        stocks_listView.getItems().clear();
        for (Stock s : lesStocks) {
            stocks_listView.getItems().add(s.getID() + "    =>    " + s.getQuantity() + " pièce(s) disponibles");
        }
    }

    @FXML
    protected void onSelectOrder() {
        String selectedItem = (String) orders_listView.getSelectionModel().getSelectedItem();
        Order selectedOrder = modele.getOrderByID(selectedItem);

        if (selectedOrder != null) {
            selectOrder_label.setVisible(false);

            fabricationOrder_label.setVisible(true);
            fabricationOrder_label.setText("Fabrication de la commande " + selectedOrder.getID());
            option_1_label.setVisible(true);
            option_2_label.setVisible(true);
            option_3_label.setVisible(true);
            rectangle.setVisible(true);

        }

        List<String> lesOptions = selectedOrder.getOptions();
        options_listView.getItems().clear();
        for (String opt : lesOptions) {
            Options option = modele.getOptionByID(opt);
            try {
                String optionName = option.getName();
                options_listView.getItems().add(optionName);
            } catch (Exception e) {
                options_listView.getItems().add("No name for option id = " + opt);
            }
        }

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
        price_field.setText(selectedOrder.getTotalPrice() + " €");

        try {
            List<String> lesOpts = selectedOrder.getOptions();
            List<Label> labels = Arrays.asList(option_1_label, option_2_label, option_3_label);

            for (int i = 0; i < lesOpts.size(); i++) {
                String opt = lesOpts.get(i);
                Stock s = modele.getStockByRefOption(opt);

                if (i < labels.size()) {
                    Label currentLabel = labels.get(i);
                    if (s.getQuantity() > 0) {
                        currentLabel.setStyle("-fx-text-fill: #24bf00;");
                        currentLabel.setText(s.getID() + "   -   Disponible");
                    } else {
                        currentLabel.setStyle("-fx-text-fill: red;");
                        currentLabel.setText(s.getID() + "   -   Indisponible");
                    }
                }
            }
        } catch (Exception e){
            option_1_label.setStyle("-fx-text-fill: red;");
            option_1_label.setText("Erreur lors du chargement des stocks pour la commande " + selectedOrder.getID());
            option_2_label.setVisible(false);
            option_3_label.setVisible(false);
            rectangle.setVisible(false);
        }
    }

    protected void setNoEditableFields() {
        numOrder_field.setEditable(false);
        custommer_field.setEditable(false);
        mdte_field.setEditable(false);
        price_field.setEditable(false);
    }
}