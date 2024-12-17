package org.example.aquachat.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.aquachat.entity.Commande;
import org.example.aquachat.entity.Ouvrier;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.SimpleFormatter;

import static java.lang.Math.round;


public class PageOuvrierController implements Initializable {
    @javafx.fxml.FXML
    private Label lblsaDepense;
    @javafx.fxml.FXML
    private AnchorPane ancPrincipal;
    @javafx.fxml.FXML
    private TableView tblAchat;
    @javafx.fxml.FXML
    private Label lblSonBudget;
    @javafx.fxml.FXML
    private Label lblBudget;
    @javafx.fxml.FXML
    private Label lblDepense;
    @javafx.fxml.FXML
    private Label lblAgence;

    Ouvrier ouvrier;
    @javafx.fxml.FXML
    private Label lbleMail;
    @javafx.fxml.FXML
    private Label lblId;
    @javafx.fxml.FXML
    private Label lblRole;
    @javafx.fxml.FXML
    private Button btnRetour;
    @javafx.fxml.FXML
    private TableColumn tcAquaStore;
    @javafx.fxml.FXML
    private TableColumn tcEBP;
    @javafx.fxml.FXML
    private TableColumn tcMontant;
    @javafx.fxml.FXML
    private TableColumn tcDate;
    CommandeController commandeController;
    ObservableList<Commande> listCommande;
    @javafx.fxml.FXML
    private DatePicker dpFin;
    @javafx.fxml.FXML
    private DatePicker dpDebut;
    LocalDate dateDebut;
    LocalDate dateFin;
    String premierJour = "";
    String dernierJour = "";
    @javafx.fxml.FXML
    private Label lblTaux;
    @javafx.fxml.FXML
    private Button btnModifBudget;
    AquAchatBDDController  aquAchatBDDController;


    public void initOuvrier(Ouvrier ouvrier, LocalDate dateDebut , LocalDate dateFin) {
        this.ouvrier = ouvrier;
        String agence = ouvrier.getAgence();
        String nom = ouvrier.getNom();
        String prenom = ouvrier.getPrenom();
        String depense = String.valueOf(ouvrier.getAchat());
        String id = ouvrier.getId();
        String role = ouvrier.getRole();
        String mail = ouvrier.getEmail();
        double budget = ouvrier.getBudget();
        double taux = Double.parseDouble(depense)/budget *100;

        lbleMail.setText(mail);
        lblRole.setText(role);
        lblSonBudget.setText(budget+" €");
        lblAgence.setText(agence );
        lblsaDepense.setText(depense+" €");
        lblId.setText(id+" : "+nom+"  "+prenom);
        lblTaux.setText(Math.round(taux) +"%");

        lblDepense.autosize();
        lblsaDepense.autosize();
        lblId.autosize();

        this.dateDebut = dateDebut ;
        this.dateFin = dateFin   ;

        dpDebut.setValue(dateDebut);
        dpFin.setValue(dateFin);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

    @javafx.fxml.FXML
    public void btnRetourClicked(Event event) {
        //close this fxml
         Stage stage = (Stage) this.btnRetour.getScene().getWindow();
         stage.close();
    }

    @javafx.fxml.FXML
    public void actualiserTblCommandes(Event event) {
        try {
            // Récupération des nouvelles dates sélectionnées
            String dateDebut = dpDebut.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String dateFin = dpFin.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            aquAchatBDDController = new AquAchatBDDController();
            this.ouvrier.setBudget(aquAchatBDDController.getBudget(ouvrier.getId().trim()));
            lblSonBudget.setText(ouvrier.getBudget()+" €");
            // Vérifier si les dates ont changé avant d'actualiser le tableau

            double budget = this.ouvrier.getBudget();
            double taux = ouvrier.getAchat()/budget *100;
            lblTaux.setText(Math.round(taux) +"%");

            if (!dateDebut.equals(premierJour) || !dateFin.equals(dernierJour)) {
                // Mettre à jour les dates de comparaison
                premierJour = dateDebut;
                dernierJour = dateFin;

                // Formater les dates dans le format attendu par la requête
                String dateDebutString = dpDebut.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String dateFinString = dpFin.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                // Initialisation de la commandeController et récupération des commandes
                commandeController = new CommandeController();
                listCommande = FXCollections.observableArrayList();
                listCommande.addAll(commandeController.getCommandesOuvrier(ouvrier, dateDebutString, dateFinString));

                // Mise à jour des colonnes du tableau
                tcDate.setCellValueFactory(new PropertyValueFactory<Commande, String>("date"));
                tcMontant.setCellValueFactory(new PropertyValueFactory<Commande, Double>("prix"));
                tcEBP.setCellValueFactory(new PropertyValueFactory<Commande, String>("numEBP"));
                tcAquaStore.setCellValueFactory(new PropertyValueFactory<Commande, String>("numAquastore"));



                // Mettre à jour les éléments du tableau
                tblAchat.setItems(listCommande);
                tblAchat.setFixedCellSize(40);

                // Calcul des dépenses totales et mise à jour du label
                double depense = 0;
                for (Commande commande : listCommande) {
                    depense += commande.getPrix();
                }
                this.ouvrier.setAchat(depense);


                lblsaDepense.setText(String.format("%.2f €", ouvrier.getAchat())); // Affichage formaté des dépenses




            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur de manière appropriée
        }
    }

    @javafx.fxml.FXML
    public void btnModifBudgetClicked(Event event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/aquachat/ModifBudget-view.fxml"));
            Parent root = fxmlLoader.load();
            PageModifBudget pageModifBudget = fxmlLoader.getController();
            pageModifBudget.initOuvier(ouvrier);
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setTitle("Modifier budget");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
