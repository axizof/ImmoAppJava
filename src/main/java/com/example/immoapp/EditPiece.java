package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditPiece {
    private int IdPiece;
    private String LibellePiece;
    @FXML
    private TextField libelle;
    private int surfacePiece;

    @FXML
    private TextField surface;

    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    public void initialize() {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();

    }
    public void setIdPiece(int IdPiece) {
        this.IdPiece = IdPiece;
    }

    public void setLibelle(String LibellePiece) {
        this.LibellePiece = LibellePiece;
        libelle.setText(LibellePiece);
    }
    public void setSurface(int surfacePiece) {
        this.surfacePiece = surfacePiece;
        surface.setText(Integer.toString(surfacePiece));
    }

    public void BtnAjoutClick(ActionEvent actionEvent) {
        if (!validateInput(surface.getText())) {
            showAlert("Erreur de validation des entrées", "surface doit être numérique.");
        } else {
            String newLibellePiece = libelle.getText();
            String newsurfacePiece = surface.getText();
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String updateQuery = "UPDATE piece SET libelle=?, surface=? WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, newLibellePiece);
                preparedStatement.setString(2, newsurfacePiece);
                preparedStatement.setString(3, String.valueOf(IdPiece));

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert("Success", "Pièce a été mis à jour avec succès.");
                    connection.close();
                    preparedStatement.close();
                    Stage stage = (Stage) libelle.getScene().getWindow();
                    stage.close();
                } else {
                    showAlert("Error", "Échec de la mise à jour de la pièce.");
                    connection.close();
                    preparedStatement.close();
                    Stage stage = (Stage) libelle.getScene().getWindow();
                    stage.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage());
            }
        }
    }
    private boolean validateInput(String input) {
        return input.matches("\\d+");
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
