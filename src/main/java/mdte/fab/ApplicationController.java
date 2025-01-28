package mdte.fab;

import Services.DBManager;
import entite.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import Modele.Modele;
import Services.VueManager;

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
    private final DBManager dbManager = new DBManager();


    public void initialize() {
        try {
            setupDataBase();
        } catch (Exception e) {
            vueManager.showError(error_label, e.getMessage());
        }
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
    protected void onFabButtonClick() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        String selectedItem = orders_listView.getSelectionModel().getSelectedItem();
        Order selectedOrder = modele.getOrderByID(selectedItem);

        StringBuilder s = getStocksAfterFab(selectedOrder);

        alert.setTitle("Confirmer la fabrication ?");
        alert.setHeaderText("Contenu des stocks après fabrication :");
        alert.setContentText(s.toString());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Integer qteStock1 = Integer.parseInt(option_1_field.getText());
            Integer qteStock2 = Integer.parseInt(option_2_field.getText());
            Integer qteStock3 = Integer.parseInt(option_3_field.getText());

            modele.validateOrder(selectedOrder, qteStock1, qteStock2, qteStock3);
        }
    }

    private void setupDataBase() throws Exception {
        dbManager.getConnection();
        dbManager.vidage();
        System.out.println("Setup database...");
        onRefreshButtonClick();
        setNoEditableFields();
        setListeners();
        modele.getOptionsForBDD();
        modele.getMDTEsForBDD();
        modele.getCustommersForBDD();
        modele.getStocksForBDD();
        System.out.println("Base de données mise à jour");
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
                dbManager.addOrder(o.getID(), o.getClientID().toString(), o.getMdteID().toString(), o.getTotalPrice(), o.getState(), o.getTrackingNumber());
                if (o.getState() != 6){
                    orders_listView.getItems().add(o.getID());
                }
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
                stocks_listView.getItems().add(s.getIDOption() + "    =>    " + s.getQuantity() + " pièce(s) disponibles");
            }
        } catch (Exception e) {
            String message = "Une erreur s'est produite lors du chargement des stocks :" + e.getMessage();
            vueManager.showError(error_label, message);
        }
    }

    private void showOptionPannel(Order o) throws Exception {
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
            String clientID = o.getClientID().toString();
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
            String mdteID = o.getMdteID().toString();
            String mdteName = modele.getMDTEByID(mdteID).getName();
            vueManager.showValueInField(mdte_field, mdteName);

        } catch (Exception e) {
            vueManager.setFieldError(mdte_field);
            String message = "Impossible de récupérer le nom du MDTE";
            vueManager.showError(error_label, message);
        }
    }

    private StringBuilder getStocksAfterFab(Order o) throws Exception {
        List<String> options = o.getOptions();
        StringBuilder s = new StringBuilder();
        List<TextField> fields = Arrays.asList(option_1_field, option_2_field, option_3_field);
        int i = 0;
        for (String opt : options) {

            Integer qteSaisie = 0;
            try {
                qteSaisie = Integer.parseInt(fields.get(i).getText());
            } catch (NumberFormatException e) {
                vueManager.showError(error_label, "Attention ! Certains champs n'ont pas de quantité renseignée (valeur par défaut : 0)");
            }

            Stock stock = modele.getStockByRefOption(opt);
            s.append(stock.getID()).append(" => ").append(stock.getQuantity() - qteSaisie).append(" unités \n");
            i += 1;
        }
        return s;
    }

    private void setListeners() {
        option_1_field.textProperty().addListener((observable, oldValue, value) -> {
            try {
                controlQte(value, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        option_2_field.textProperty().addListener((observable, oldValue, value) -> {
            try {
                controlQte(value, 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        option_2_field.textProperty().addListener((observable, oldValue, value) -> {
            try {
                controlQte(value, 2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void controlQte(String value, Integer numOption) throws Exception {

        String selectedItem = orders_listView.getSelectionModel().getSelectedItem();
        Order selectedOrder = modele.getOrderByID(selectedItem);
        List<String> lesOptions = selectedOrder.getOptions();

        String selectedOption = lesOptions.get(numOption);
        Stock selectedStock = modele.getStockByRefOption(selectedOption);

        Integer qteMax = selectedStock.getQuantity();

        List<TextField> fields = Arrays.asList(option_1_field, option_2_field, option_3_field);

        try {
            vueManager.deleteError(error_label);
            if (Integer.parseInt(value) > qteMax) {
                blockFabrication();
                vueManager.setFieldError2(fields.get(numOption));
                vueManager.showError(error_label, "Pas assez de pièces pour " + selectedStock.getIDOption());
            } else {
                vueManager.reEnableField(fields.get(numOption));
                unlockFabrication();
            }
        } catch (Exception e) {
            if (fields.get(numOption).getText().isEmpty()) {
            } else {
                blockFabrication();
                vueManager.showError(error_label, "Veuillez saisir une valeur numérique");
            }
        }
    }}