package org.example.aquachat.entity;

public class Commande {
    private String numEBP;
    private String numAquastore;
    private double prix;
    private String date;

    public Commande(String numEBP, String numAquastore, double prix, String date) {
        this.numEBP = numEBP;
        this.numAquastore = numAquastore;
        this.prix = prix;
        this.date = date;
    }

    public String getNumEBP() {
        return numEBP;
    }

    public void setNumEBP(String numEBP) {
        this.numEBP = numEBP;
    }

    public String getNumAquastore() {
        return numAquastore;
    }

    public void setNumAquastore(String numAquastore) {
        this.numAquastore = numAquastore;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
