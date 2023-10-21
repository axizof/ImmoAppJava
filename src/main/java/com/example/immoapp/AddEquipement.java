package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddEquipement {
    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    @FXML TextField libelle;

    public void addPiece(String libelle, String surface, int id_Logement) {
        int CheckInfo;
        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO equipement (libelle, surface, id_Logement) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, libelle);
            stmt.setString(2, surface);
            stmt.setInt(3, id_Logement);
            stmt.executeUpdate();
            sql = "SELECT id FROM Piece WHERE libelle = ? AND surface = ? AND id_Logement = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setString(1, libelle);
            stmt2.setString(2, surface);
            stmt2.setInt(3, id_Logement);
            ResultSet rs = stmt2.executeQuery();
            int idPieceInd = -1;
            if (rs.next()) {
                idPieceInd = rs.getInt("ID");
            }
            for (int i = 0; i < ImagesBase64.size(); i++) {
                sql = "INSERT INTO Photo (id_Piece, photo) " +
                        "VALUES (?, ?)";
                PreparedStatement stmt3 = conn.prepareStatement(sql);
                stmt3.setInt(1, idPieceInd);
                stmt3.setString(2, ImagesBase64.get(i));
                stmt3.executeUpdate();
            }
            stmt.close();
            stmt2.close();

            // Show a confirmation dialog
            showConfirmationDialog();

            // Close the window
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void BtnAjoutClick(ActionEvent actionEvent) {

    }
}
