package org.example.aquachat.controller;

import org.example.aquachat.entity.Commande;
import org.example.aquachat.entity.Ouvrier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CommandeController {

    Connection cnx;
    PreparedStatement ps;
    ResultSet rs;

    public CommandeController() throws SQLException, ClassNotFoundException {
        ConnexionBDD.initialize();
         cnx = ConnexionBDD.getCnx();
    }

    public ArrayList<Commande> getCommandesOuvrier(Ouvrier ouvrier, String dateDebut, String dateFin) {
        ArrayList<Commande> commandes = new ArrayList<>();

        try {
            ps=cnx.prepareStatement("select AmountVatExcludedWithDiscount as montant, " +
                    "reference as aquastore, " +
                    "sysCreatedDate as date, " +
                    "DocumentNumber as ebp " +
                    "from PurchaseDocument " +
                    "where reference like 'AQUA%' " +
                    "and ColleagueId = ? " +
                    "and DocumentDate between ? AND ? " +
                    "order by sysCreatedDate desc");
            ps.setString(1,ouvrier.getId());
            ps.setString(2,dateDebut);
            ps.setString(3,dateFin);
            rs=ps.executeQuery();

            while(rs.next()) {




                String date = rs.getString("date");
                String ebp = rs.getString("ebp");
                Double montant = Double.parseDouble( rs.getString("montant"));
                String aquastore = rs.getString("aquastore");

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

                LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = dateTime.format(outputFormatter);

                Commande uneCommande = new Commande(ebp,aquastore,montant,formattedDate);
                commandes.add(uneCommande);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return commandes;
    }

}
