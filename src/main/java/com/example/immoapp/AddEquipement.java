package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddEquipement {
    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    @FXML TextField libelle;

    public void addEquipement(String libelle, int id_Piece) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Vérifiez d'abord si un équipement avec le même libellé existe déjà.
            String checkSql = "SELECT id FROM Equipement WHERE libelle = ? AND id_Piece = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, libelle);
            checkStmt.setInt(2, id_Piece);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                // Un équipement avec le même libellé existe déjà, affichez une alerte.
                showDuplicateEquipementAlert();
            } else {
                // L'équipement est unique, vous pouvez l'ajouter.
                String insertSql = "INSERT INTO Equipement (libelle, id_Piece) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, libelle);
                insertStmt.setInt(2, id_Piece);
                insertStmt.executeUpdate();

                // Récupérez l'ID de l'équipement nouvellement ajouté, si nécessaire.

                for (int i = 0; i < ImagesBase64.size(); i++) {
                    String photoSql = "INSERT INTO Photo (id_Equipement, photo) VALUES (?, ?)";
                    PreparedStatement photoStmt = conn.prepareStatement(photoSql);
                    photoStmt.setInt(1, idEquipement); // Assurez-vous d'avoir l'ID de l'équipement
                    photoStmt.setString(2, ImagesBase64.get(i));
                    photoStmt.executeUpdate();
                }

                // Affichez une confirmation de l'ajout.
                showConfirmationDialog();

                // Fermez la fenêtre si nécessaire.
                closeWindow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDuplicateEquipementAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Équipement en double");
        alert.setContentText("Un équipement avec le même libellé existe déjà. Le nom doit être unique.");
        alert.showAndWait();
    }
    public void BtnAjoutClick(ActionEvent actionEvent) {

    }
}
