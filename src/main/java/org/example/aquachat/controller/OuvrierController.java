package org.example.aquachat.controller;

import org.example.aquachat.entity.Ouvrier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OuvrierController {
    Connection cnx;
    ResultSet rs;
    PreparedStatement ps;
    Connection cnxAquachat;

    public OuvrierController() throws SQLException, ClassNotFoundException {
        ConnexionBDD.initialize();
        cnx = ConnexionBDD.getCnx();
        ConnexionAquadimBDD.initialize();
        cnxAquachat = ConnexionAquadimBDD.getCnx();

    }

    public ArrayList<Ouvrier>getOuvrier(String dateDebut,String dateFin){
        ArrayList<Ouvrier>ouvriers = new ArrayList<>();
        HashMap<String,Integer>budget = getBudget();
        try{
            ps = cnx.prepareStatement(
                    "SELECT ClassificationGroup.caption AS agence, " +
                            "       colleague.id AS id, " +
                            "       colleague.Contact_Name AS nom, " +
                            "       Colleague.Contact_FirstName AS prenom, " +
                            "       colleague.Contact_Email AS email, " +
                            "       Colleague.Contact_ColleagueFunction AS fonction, " +
                            "       COALESCE(SUM(PurchaseDocument.AmountVatExcludedWithDiscount), 0) AS totalCommande " +
                            "FROM Colleague " +
                            "LEFT JOIN PurchaseDocument " +
                            "       ON PurchaseDocument.ColleagueId = Colleague.Id " +
                            "      AND PurchaseDocument.reference LIKE 'AQUA%' " +
                            "      AND DocumentDate BETWEEN ? AND ? " +
                            "INNER JOIN ClassificationGroup " +
                            "       ON ClassificationGroup.Id = Colleague.Group1 " +
                            "WHERE ISNUMERIC(CAST(colleague.id AS VARCHAR)) = 1 " +
                            "GROUP BY Colleague.Id, " +
                            "         Colleague.Contact_Name, " +
                            "         Colleague.Contact_FirstName, " +
                            "         ClassificationGroup.Caption, " +
                            "         Colleague.Contact_Email, " +
                            "         Colleague.Contact_ColleagueFunction " +
                            "ORDER BY totalCommande DESC;");

            ps.setString(1, dateDebut);
            ps.setString(2, dateFin);
            rs = ps.executeQuery();

            while(rs.next()){
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String id = rs.getString("id").trim();
                double achat = Math.round( rs.getDouble("totalCommande"));
                String agence = rs.getString("agence");
                String fonction = rs.getString("fonction");
                String email = rs.getString("email");
                double seuil = 0;
                if (budget.containsKey(id.trim())){
                    seuil = budget.get(id.trim());
                }

                Ouvrier unOuvrier = new Ouvrier(id,nom,prenom,agence,achat,email,fonction,seuil);

                ouvriers.add(unOuvrier);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ouvriers;
    }


    public ArrayList<String> getOuvriersIdEBP() {
        ArrayList<String> ids = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("SELECT Colleague.Id FROM Colleague WHERE ISNUMERIC(CAST(Colleague.Id AS VARCHAR)) = 1");
            rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString("Id").trim()); // Normaliser les IDs
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public HashSet<String> getOuvriersIdAquachat() {
        HashSet<String> idsExistantAquachat = new HashSet<>();
        try (PreparedStatement ps = cnxAquachat.prepareStatement("SELECT UserId FROM aquachat");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idsExistantAquachat.add(rs.getString("UserId").trim().toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idsExistantAquachat;
    }

    public ArrayList<String> getIdsInexistant() {
        ArrayList<String> idInexistant = new ArrayList<>();
        HashSet<String> idAquachat = getOuvriersIdAquachat();
        ArrayList<String> idEbp = getOuvriersIdEBP();

        for (String id : idEbp) {
            if (!idAquachat.contains(id.trim().toLowerCase())) {
                idInexistant.add(id);
            }
        }
        return idInexistant;
    }



    public void updateAquachat() throws SQLException, ClassNotFoundException {
        ArrayList<String> ids =getIdsInexistant(); // Récupère les IDs


        // Prépare une seule fois l'insertion
        String insertQuery = "INSERT INTO aquachat (UserId, role, seuil) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnxAquachat.prepareStatement(insertQuery)) {
            for (String id : ids) {
                    ps.setString(1, id.trim());
                    ps.setString(2, "1");
                    ps.setInt(3, 2000);
                    ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Remonte l'erreur pour gestion
        }
    }


    public HashMap<String,Integer> getBudget(){
        HashMap<String,Integer>budget = new HashMap<>();
        try {
            ps = cnxAquachat.prepareStatement("select * from aquachat where role = 1");
            rs = ps.executeQuery();
            while(rs.next()){
                budget.put(rs.getString("UserId").trim(),rs.getInt("Seuil"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return budget;
    }




}
