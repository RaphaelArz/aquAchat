package org.example.aquachat.controller;

import org.example.aquachat.entity.Ouvrier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AquAchatBDDController {
    ResultSet rs;
    PreparedStatement ps;
    Connection cnxAquachat;

    public AquAchatBDDController() {
       try{
           ConnexionAquadimBDD.initialize();
           cnxAquachat = ConnexionAquadimBDD.getCnx();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public void addMail(String Nom, String Mail){
        try{
            ps = cnxAquachat.prepareStatement("insert into aquachat (UserId,email,role) values(?,?,?)");
            ps.setString(1, Nom);
            ps.setString(2, Mail);
            ps.setInt(3, 3);
            ps.executeUpdate();
            System.out.println("New eMail inserted successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Ouvrier> getMail(){
        ArrayList<Ouvrier> lstMail = new ArrayList<>();
        try{
            ps=cnxAquachat.prepareStatement("select * from aquachat where role = 3");
            rs=ps.executeQuery();
            while(rs.next()){
                String email = rs.getString("email");
                String nom = rs.getString("UserId");
                Ouvrier ouvrier = new Ouvrier("",nom,"","",0,email,"bureau",0);
                lstMail.add(ouvrier);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lstMail;
    }

    public void DeleteMail(String email){
        try{
            ps = cnxAquachat.prepareStatement("delete aquachat where aquachat.email like ? and aquachat.role = 3");
            ps.setString(1, email);
            ps.executeUpdate();
            System.out.println("Mail deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existMail(String email){
        boolean exist = false;
        try{
            ps= cnxAquachat.prepareStatement("select email from aquachat where aquachat.email = ? and role = 3");
            ps.setString(1, email);
            rs=ps.executeQuery();
            if(rs.next()){
                exist = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exist;
    }

    public void setBudget (int budget, String id){
        try{
            ps = cnxAquachat.prepareStatement("update aquachat set seuil = ? where UserId = ?");
            ps.setInt(1, budget);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBudget (String id){
        try {
            ps=cnxAquachat.prepareStatement("select seuil from aquachat where UserId = ?");
            ps.setString(1, id);
            rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString("seuil");
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
