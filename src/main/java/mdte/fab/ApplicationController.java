package mdte.fab;

import entite.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import service.Modele;
import service.VueManager;

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
    private final VueManager vueManager = new VueManager();

    public void initialize() {
        onRefreshButtonClick();
        setNoEditableFields();
    }

    @FXML
    protected void onRefreshButtonClick() {
        selectOrder_label.setVisible(true);
        fabricationOrder_label.setVisible(false);

        showOptionsLabels(false);

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
            showOptionsLabels(true);

        }


        assert selectedOrder != null;
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
            String clientName = client.getFirstName() + " " + client.getLastName();
            vueManager.showValueInField(custommer_field, clientName);

        } catch (Exception e){
            vueManager.setFieldError(custommer_field);
        }

        try{
            String mdteID = selectedOrder.getMdteID();
            String mdteName = modele.getMDTEByID(mdteID).getName();

            vueManager.showValueInField(mdte_field, mdteName);

        } catch (Exception e){
            vueManager.setFieldError(mdte_field);
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
                        vueManager.showLabelDisponible(currentLabel, s);
                    } else {
                       vueManager.showLabelIndisponible(currentLabel, s);
                    }
                }
            }
        } catch (Exception e){
            vueManager.showErrorStocks(option_1_label, selectedOrder);

            showOptionsLabels(false);
            option_1_label.setVisible(true);
        }
    }


    //Fonctions d'affichage

    private void showOptionsLabels(boolean show) {
        option_1_label.setVisible(show);
        option_2_label.setVisible(show);
        option_3_label.setVisible(show);
        rectangle.setVisible(show);
    }

    protected void setNoEditableFields() {
        numOrder_field.setEditable(false);
        custommer_field.setEditable(false);
        mdte_field.setEditable(false);
        price_field.setEditable(false);
    }
}