package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddBien {
    @FXML
    private TextField titre;
    @FXML
    private TextField adresse;
    @FXML
    private TextField ville;
    @FXML
    private TextField cp;
    @FXML
    private CheckBox CheckAppart;
    @FXML
    private CheckBox CheckMaison;

    String jdbcUrl = "jdbc:mysql://172.19.0.32:3306/immoAPP";
    String username = "mysqluser";
    String password = "0550002D";

    public void addBien(String libelle, int nbPieces, String cp, String ville, int idlog, String adresselog) {
        int CheckInfo;
        try {
            if (CheckAppart.isSelected()) {
                CheckInfo = 1;
            }
            else {
                CheckInfo = 2;
            }
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO Logement (libelle, adresse, cp, ville, dateAjout, id_Type) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, libelle);
            stmt.setString(2, adresselog);
            stmt.setString(3, cp);
            stmt.setString(4, ville);
            stmt.setDate(5, new Date(System.currentTimeMillis()));
            stmt.setInt(6, CheckInfo);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void BtnAjoutClick(ActionEvent actionEvent) {
        addBien(titre.getText(), 0, cp.getText(), ville.getText(), 0, adresse.getText());
    }
}
