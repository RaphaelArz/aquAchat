package org.example.aquachat.controller;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PageAjoutMailController {
    @javafx.fxml.FXML
    private TextField txtEmail;
    @javafx.fxml.FXML
    private TextField txtNom;
    @javafx.fxml.FXML
    private Button btnAjouterMail;
    AquAchatBDDController aquAchatBDDController;

    @javafx.fxml.FXML
    public void btnaddMailClicked(Event event) {
        if(txtEmail.getText().equals("") || txtNom.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
        }else{
            aquAchatBDDController = new AquAchatBDDController();
            if (aquAchatBDDController.existMail(txtEmail.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Email déjà renseigné");
                alert.showAndWait();
            }else {
                aquAchatBDDController.addMail(txtNom.getText(),txtEmail.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Email ajouté avec succès");
                alert.showAndWait();
                Stage stage = (Stage) this.btnAjouterMail.getScene().getWindow();
                stage.close();
            }
        }
    }
}
