package org.example.aquachat.entity;

public class Ouvrier {
    private String id;
    private String nom;
    private String prenom;
    private String agence;
    private double achat;
    private String email;
    private String role;
    private double budget;


    public Ouvrier(String id, String nom, String prenom, String agence, double achat, String email, String role, double budget) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.agence = agence;
        this.achat = achat;
        this.email = email;
        this.role = role;
        this.budget = budget;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public double getAchat() {
        return achat;
    }

    public void setAchat(double achat) {
        this.achat = achat;
    }
}
