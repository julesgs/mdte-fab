package mdte.fab;

import entite.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import service.Modele;
import Util.VueManager;

import java.util.Arrays;
import java.util.List;

public class ApplicationController {

    @FXML
    private Button RefreshButton;
    @FXML
    private Label selectOrder_label, fabricationOrder_label, option_1_label, option_2_label, option_3_label, error_label;
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
        showOrders();
        showStocks();
    }

    @FXML
    protected void onSelectOrder() {

        options_listView.getItems().clear();
        vueManager.deleteError(error_label);

        String selectedItem = orders_listView.getSelectionModel().getSelectedItem();

        try {
            Order selectedOrder = modele.getOrderByID(selectedItem);

            fillOrderFields(selectedOrder);
            showOptionPannel(selectedOrder);
            showFabricationPannel(selectedOrder);

        } catch (Exception e) {
            String message = "Impossible de récupèrer les détails de la commande " + selectedItem + " : " + e.getMessage();
            vueManager.showError(error_label, message);
        }
    }



    /* ############################
    ||   Fonctions d'affichage    ||
    ############################# */

    private void showOptionsLabels(boolean show) {
        option_1_label.setVisible(show);
        option_2_label.setVisible(show);
        option_3_label.setVisible(show);
        rectangle.setVisible(show);
    }

    private void setNoEditableFields() {
        numOrder_field.setEditable(false);
        custommer_field.setEditable(false);
        mdte_field.setEditable(false);
        price_field.setEditable(false);
    }

    private void fillOrderFields(Order o) {
        showClientName(o);
        showMDTEName(o);
        numOrder_field.setText(o.getID());
        price_field.setText(o.getTotalPrice() + " €");
    }

    private void showOrders() {
        try {
            vueManager.deleteError(error_label);
            List<Order> orders = modele.getOrders();
            orders_listView.getItems().clear();
            for (Order o : orders) {
                orders_listView.getItems().add(o.getID());
            }
        } catch (Exception e) {
            String message = "Une erreur s'est produite lors du chargement des commandes :" + e.getMessage();
            vueManager.showError(error_label, message);
        }
    }

    private void showStocks() {
        try {
            vueManager.deleteError(error_label);
            List<Stock> stocks = modele.getStocks();
            stocks_listView.getItems().clear();
            for (Stock s : stocks) {
                stocks_listView.getItems().add(s.getID() + "    =>    " + s.getQuantity() + " pièce(s) disponibles");
            }
        } catch (Exception e) {
            String message = "Une erreur s'est produite lors du chargement des stocks :" + e.getMessage();
            vueManager.showError(error_label, message);
        }
    }

    private void showOptionPannel(Order o) {
        List<String> lesOptions = o.getOptions();
        for (String opt : lesOptions) {
            Options option = modele.getOptionByID(opt);
            try {
                String optionName = option.getName();
                options_listView.getItems().add(optionName);
            } catch (Exception e) {
                options_listView.getItems().add("No name for option id = " + opt);
            }
        }
    }

    private void showFabricationPannel(Order o) {
        try {
            selectOrder_label.setVisible(false);
            fabricationOrder_label.setVisible(true);
            fabricationOrder_label.setText("Fabrication de la commande " + o.getID());
            showOptionsLabels(true);

            List<String> lesOpts = o.getOptions();
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
        } catch (Exception e) {
            vueManager.showErrorStocks(option_1_label, o);

            showOptionsLabels(false);
            option_1_label.setVisible(true);
        }
    }

    private void showClientName(Order o) {
        try {
            String clientID = o.getClientID();
            Custommer client = modele.getCustommerByID(clientID);
            String clientName = client.getFirstName() + " " + client.getLastName();
            vueManager.showValueInField(custommer_field, clientName);

        } catch (Exception e) {
            vueManager.setFieldError(custommer_field);
            String message = "Impossible de récupérer le nom du client";
            vueManager.showError(error_label, message);
        }
    }

    private void showMDTEName(Order o) {
        try {
            String mdteID = o.getMdteID();
            String mdteName = modele.getMDTEByID(mdteID).getName();

            vueManager.showValueInField(mdte_field, mdteName);

        } catch (Exception e) {
            vueManager.setFieldError(mdte_field);
            String message = "Impossible de récupérer le nom du MDTE";
            vueManager.showError(error_label, message);
        }
    }

}