package mdte.fab;

import entite.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import service.Modele;
import Util.VueManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ApplicationController {

    @FXML
    private Button RefreshButton, fab_button;
    @FXML
    private Label selectOrder_label, fabricationOrder_label, option_1_label, option_2_label, option_3_label, error_label, label_x, label_x1, label_x2;
    @FXML
    private ListView<String> orders_listView, options_listView, stocks_listView;
    @FXML
    private TextField numOrder_field, custommer_field, mdte_field, price_field, option_1_field, option_2_field, option_3_field;
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

    @FXML
    protected void onFabButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        String selectedItem = orders_listView.getSelectionModel().getSelectedItem();
        Order selectedOrder = modele.getOrderByID(selectedItem);

        StringBuilder s = getStocksAfterFab(selectedOrder);

        alert.setTitle("Confirmer la fabrication ?");
        alert.setHeaderText("Contenu des stocks après fabrication :");
        alert.setContentText(s.toString());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Continuer");
        }
    }



    /*\ ############################
    ||   Fonctions d'affichage    ||
    ############################# \*/

    private void showOptionsLabels(boolean show) {
        option_1_label.setVisible(show);
        option_2_label.setVisible(show);
        option_3_label.setVisible(show);
        rectangle.setVisible(show);
        fab_button.setVisible(show);
        label_x.setVisible(show);
        label_x1.setVisible(show);
        label_x2.setVisible(show);
        option_1_field.setVisible(show);
        option_2_field.setVisible(show);
        option_3_field.setVisible(show);
    }

    private void blockFabrication(){
        fab_button.setDisable(true);
        fab_button.setStyle("-fx-background-color: #c4c4c4");
    }

    private void unlockFabrication(){
        fab_button.setDisable(false);
        fab_button.setStyle("-fx-font-style: normal;");
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
            List<TextField> fields = Arrays.asList(option_1_field, option_2_field, option_3_field);

            unlockFabrication();

            for (int i = 0; i < lesOpts.size(); i++) {
                String opt = lesOpts.get(i);
                Stock s = modele.getStockByRefOption(opt);


                if (i < labels.size()) {
                    Label currentLabel = labels.get(i);
                    TextField currentField = fields.get(i);

                    if (s.getQuantity() > 0) {
                        vueManager.showLabelDisponible(currentLabel, s);
                        vueManager.enableField(currentField);
                    } else {
                        vueManager.showLabelIndisponible(currentLabel, s);
                        vueManager.disableField(currentField);
                        blockFabrication();
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

    private StringBuilder getStocksAfterFab(Order o) {
        List<String> options = o.getOptions();
        StringBuilder s = new StringBuilder();
        for (String opt : options) {
            Stock stock = modele.getStockByRefOption(opt);
            s.append(stock.getID()).append(" => ").append(stock.getQuantity() - 1).append(" unités \n");
        }
        return s;
    }

}