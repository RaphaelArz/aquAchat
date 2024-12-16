module org.example.aquachat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires mssql.jdbc;

    opens org.example.aquachat to javafx.fxml;
    exports org.example.aquachat;
    exports org.example.aquachat.controller;
    opens org.example.aquachat.controller to javafx.fxml;
    opens org.example.aquachat.entity to javafx.base;  // Cette ligne permet à javafx.base d'accéder aux classes du package entity

}