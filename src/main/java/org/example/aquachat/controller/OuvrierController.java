package org.example.aquachat.controller;

import org.example.aquachat.entity.Ouvrier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
        HashMap<Integer,Integer>budget = getBudget();
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
                String id = rs.getString("id");
                double achat = rs.getDouble("totalCommande");
                String agence = rs.getString("agence");
                String fonction = rs.getString("fonction");
                String email = rs.getString("email");
                double seuil = 0;
                if (budget.containsKey(Integer.parseInt(id))){
                    seuil = budget.get(Integer.parseInt(id));
                }

                Ouvrier unOuvrier = new Ouvrier(id,nom,prenom,agence,achat,email,fonction,seuil);

                ouvriers.add(unOuvrier);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ouvriers;
    }


    public ArrayList<String> getOuvriersId(){

        ArrayList<String>ids = new ArrayList<>();

        try {
            ps = cnx.prepareStatement("select Colleague.Id, Colleague.Contact_Email from Colleague\n" +
                    "where ISNUMERIC(CAST(colleague.id AS VARCHAR)) = 1 ");
            rs = ps.executeQuery();
            while(rs.next()){
                ids.add(rs.getString("Id"));
            }
        }catch (Exception e){

        }
        return ids;
    }


    public  void updateAquachat() throws SQLException, ClassNotFoundException {
        ArrayList<String>ids = getOuvriersId();
        try {
            ArrayList<String> idsExistant = new ArrayList<>();
            ps= cnxAquachat.prepareStatement("select * from aquachat");
            rs = ps.executeQuery();
            while(rs.next()){
                idsExistant.add(rs.getString("UserId"));
            }
            for(String id:ids ){
                if(!idsExistant.contains(id)){
                    ps = cnxAquachat.prepareStatement("insert into aquachat (UserId) values (?)");
                    ps.setString(1,id);
                    ps.executeUpdate();
                    ps.close();
                }

            }
        }
        catch (Exception e){

        }


    }

    public HashMap<Integer,Integer> getBudget(){
        HashMap<Integer,Integer>budget = new HashMap<>();
        try {
            ps = cnxAquachat.prepareStatement("select * from aquachat where role = 1");
            rs = ps.executeQuery();
            while(rs.next()){
                budget.put(rs.getInt("UserId"),rs.getInt("Seuil"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return budget;
    }



}
