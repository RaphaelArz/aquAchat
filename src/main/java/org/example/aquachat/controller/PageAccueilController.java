package org.example.aquachat.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.aquachat.entity.Ouvrier;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PageAccueilController implements Initializable {
    @javafx.fxml.FXML
    private Button btnTbList;
    @javafx.fxml.FXML
    private AnchorPane ancPrincipal;
    @javafx.fxml.FXML
    private AnchorPane ancStat;
    @javafx.fxml.FXML
    private AnchorPane ancList;
    @javafx.fxml.FXML
    private AnchorPane ancTop;
    @javafx.fxml.FXML
    private BorderPane bpPrincipal;
    @javafx.fxml.FXML
    private Button btnTbStat;
    @javafx.fxml.FXML
    private TableView<Ouvrier> tblOuvriers;
    @javafx.fxml.FXML
    private Button btnTbParam;
    @javafx.fxml.FXML
    private TableColumn<Ouvrier, String> tcNom;
    @javafx.fxml.FXML
    private TableColumn<Ouvrier, String> tcAchat;
    @javafx.fxml.FXML
    private TableColumn<Ouvrier, String> tcPrenom;
    @javafx.fxml.FXML
    private TableColumn<Ouvrier, String> tcAgence;
    @javafx.fxml.FXML
    private TableColumn<Ouvrier, String> tcId;
    OuvrierController ouvrierController;
    ObservableList<Ouvrier> listOuvriers;
    @javafx.fxml.FXML
    private ImageView imgSettings;
    @javafx.fxml.FXML
    private DatePicker dpFin;
    @javafx.fxml.FXML
    private DatePicker dpDebut;

    String premierJour = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    String dernierJour = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    @javafx.fxml.FXML
    private AnchorPane ancSettings;
    @javafx.fxml.FXML
    private TableView tblEmail;
    @javafx.fxml.FXML
    private TableColumn tcMail;
    @javafx.fxml.FXML
    private Button btnAjouterMail;
    @javafx.fxml.FXML
    private TableColumn tcNomMail;
    @javafx.fxml.FXML
    private TextField txtRecherche;

    AquAchatBDDController aquAchatBDDController;


    @javafx.fxml.FXML
    public void btnTbListClicked(Event event) {
        ancList.setVisible(true);
        ancList.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ancList.toFront();
        dpDebut.setValue(LocalDate.now().withDayOfMonth(1));
        dpFin.setValue(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
        Image image = new Image(getClass().getResource("/image/settings.png").toExternalForm());
        imgSettings.setImage(image);




        try {
            ouvrierController = new OuvrierController();
            ouvrierController.updateAquachat();
            listOuvriers = FXCollections.observableArrayList();
            listOuvriers.addAll(ouvrierController.getOuvrier(premierJour, dernierJour));

            // Initialisation des colonnes
            tcAchat.setCellValueFactory(new PropertyValueFactory<>("achat"));
            tcNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            tcPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcAgence.setCellValueFactory(new PropertyValueFactory<>("agence"));

            search();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void btnTbStatClicked(Event event) {
        ancStat.setVisible(true);
        ancStat.toFront();
    }

    @javafx.fxml.FXML
    public void tblOuvriersClicked(Event event) throws IOException {
        if (!(tblOuvriers.getSelectionModel().getSelectedItem() == null)) {
            Ouvrier ouvrierSelectionne = tblOuvriers.getSelectionModel().getSelectedItem();

            if (ouvrierSelectionne != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/aquachat/pageOuvrier-view.fxml"));
                Parent root = fxmlLoader.load();
                PageOuvrierController pageOuvrierController = fxmlLoader.getController();
                pageOuvrierController.initOuvrier(ouvrierSelectionne, dpDebut.getValue(), dpFin.getValue());
                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setTitle("Détails de l'ouvrier");
                stage.setScene(scene);
                stage.show();

                tblOuvriers.getSelectionModel().clearSelection();
            } else {
                System.out.println("Aucun ouvrier sélectionné !");
            }
        }
    }

    @javafx.fxml.FXML
    public void actualiserTblOuvriers(Event event) {
        try {
            String dateDebut = dpDebut.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String dateFin = dpFin.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (!dateDebut.equals(premierJour) || !dateFin.equals(dernierJour)) {
                premierJour = dateDebut;
                dernierJour = dateFin;
                ouvrierController = new OuvrierController();
                listOuvriers = FXCollections.observableArrayList();
                listOuvriers.addAll(ouvrierController.getOuvrier(dateDebut, dateFin));
                tcAchat.setCellValueFactory(new PropertyValueFactory<>("achat"));
                tcNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
                tcPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
                tcAgence.setCellValueFactory(new PropertyValueFactory<>("agence"));

                search();


            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @javafx.fxml.FXML
    public void btnTbSettingsClicked(Event event) {
        ancSettings.toFront();
        ancSettings.setVisible(true);

        aquAchatBDDController = new AquAchatBDDController();
        ObservableList<Ouvrier> listMail = FXCollections.observableArrayList();
        listMail.addAll(aquAchatBDDController.getMail());

        // Initialisation des colonnes
        tcMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcNomMail.setCellValueFactory(new PropertyValueFactory<>("nom"));

        tblEmail.setItems(listMail);
        tblEmail.setFixedCellSize(40);
    }



    public void search(){
        // Création d'une liste filtrée
        FilteredList<Ouvrier> filteredData = new FilteredList<>(listOuvriers, p -> true);

        // Ajout d'un listener sur le champ de recherche
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ouvrier -> {
                // Si la recherche est vide, tout afficher
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparer le texte saisi avec les propriétés de l'ouvrier
                if (ouvrier.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance avec le nom
                } else if (ouvrier.getPrenom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance avec le prénom
                } else if (ouvrier.getAgence().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance avec l'agence
                } else if (String.valueOf(ouvrier.getAchat()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance avec le montant des achats
                }else if (String.valueOf(ouvrier.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance avec le montant des achats
                }
                return false; // Pas de correspondance
            });
        });

        // Lier la liste filtrée au tableau
        tblOuvriers.setItems(filteredData);
        tblOuvriers.setFixedCellSize(40);
    }

    @javafx.fxml.FXML
    public void actualiserTblMail(Event event) {
       aquAchatBDDController = new AquAchatBDDController();
        ObservableList<Ouvrier> listMail = FXCollections.observableArrayList();
        listMail.addAll(aquAchatBDDController.getMail());

        // Initialisation des colonnes
        tcMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcNomMail.setCellValueFactory(new PropertyValueFactory<>("nom"));

        tblEmail.setItems(listMail);
        tblEmail.setFixedCellSize(40);

    }

    @javafx.fxml.FXML
    public void btnaddMailClicked(Event event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/aquachat/ajoutMail-view.fxml"));
            Parent root = fxmlLoader.load();
            PageAjoutMailController pageAjoutMailController = fxmlLoader.getController();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("AjouterMail");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @javafx.fxml.FXML
    public void tblMailClicked(Event event) {

        if (tblEmail.getSelectionModel().getSelectedItem() != null) {

            Ouvrier ouvrier = (Ouvrier) tblEmail.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez vous supprimer l'adresse mail suivante ? \n \n " + ouvrier.getEmail());

            // Afficher l'alerte et attendre la réponse
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    aquAchatBDDController = new AquAchatBDDController();
                    aquAchatBDDController.DeleteMail(ouvrier.getEmail());

                } else {
                    alert.close();
                }
            });
        }
    }
}
