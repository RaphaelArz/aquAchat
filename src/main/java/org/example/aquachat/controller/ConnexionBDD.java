package org.example.aquachat.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    private static Connection cnx;

    /**
     * Initialise la connexion à la base de données.
     * Cette méthode est appelée une seule fois pour configurer la connexion.
     */
    public static void initialize() throws ClassNotFoundException, SQLException {
        if (cnx == null) { // Évite de recréer la connexion si elle existe déjà
            try {
                String pilote = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // Pilote SQL Server
                Class.forName(pilote); // Chargement du pilote

                String url = "jdbc:sqlserver://w2k22sql\\EBP;" +
                        "databaseName=AQUADIMFINALE_0895452f-b7c1-4c00-a316-c6a6d0ea4bf4;" +
                        "encrypt=true;trustServerCertificate=true;" +
                        "user=Journalvente;password=jdv2023;";

                cnx = DriverManager.getConnection(url); // Connexion à la base de données
                System.out.println("Connexion à la base de données établie avec succès !");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Retourne l'objet Connection actuel.
     * @return L'objet Connection actif.
     */
    public static Connection getCnx() {
        if (cnx == null) {
            throw new IllegalStateException("La connexion à la base de données n'a pas été initialisée. Appelez 'initialize()' d'abord.");
        }
        return cnx;
    }
}
