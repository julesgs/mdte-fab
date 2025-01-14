package mdte.fab;

import entite.Custommer;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import service.Modele;

public class CustommerController {

    @FXML
    private TextField idCustommer_field, firstNameCustommer_field, lastNameCustommer_field, emailCustommer_field, adressCustommer_field;

    private String clientId;

    private final Modele modele = new Modele();

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public void initialize() {
        setNoEditableFields();

        /*String c = modele.getCustommerByID(this.clientId);

        firstNameCustommer_field.setText(c);*/
    }

    protected void setNoEditableFields() {
        idCustommer_field.setEditable(false);
        firstNameCustommer_field.setEditable(false);
        lastNameCustommer_field.setEditable(false);
        emailCustommer_field.setEditable(false);
        adressCustommer_field.setEditable(false);
    }



}
