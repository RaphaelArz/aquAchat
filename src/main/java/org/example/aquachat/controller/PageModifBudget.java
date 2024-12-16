package org.example.aquachat.controller;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.aquachat.entity.Ouvrier;

import static com.microsoft.sqlserver.jdbc.StringUtils.isNumeric;



public class PageModifBudget {
    AquAchatBDDController  aquAchatBDDController;
    Ouvrier ouvrier;
    @javafx.fxml.FXML
    private TextField txtBudget;
    @javafx.fxml.FXML
    private Button btnModifier;

    public void initOuvier(Ouvrier ouvrier) {
        this.ouvrier = ouvrier;
    }


    @javafx.fxml.FXML
    public void btnModifierClicked(Event event) {
        if (txtBudget.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir le budget.");
            alert.showAndWait();
        }if (!isNumeric(txtBudget.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Format non valide.");
            alert.showAndWait();
        }else {
            aquAchatBDDController = new AquAchatBDDController();
            aquAchatBDDController.setBudget(Integer.parseInt(txtBudget.getText()), ouvrier.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("le budget a été modifié avec succès");
            alert.showAndWait();
            Stage stage = (Stage) this.btnModifier.getScene().getWindow();
            stage.close();
        }
    }
}
