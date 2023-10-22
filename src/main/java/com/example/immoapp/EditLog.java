package com.example.immoapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class EditLog {

    @FXML
    public TextField LibelleTextbox;
    @FXML
    public TextField AdresseTextbox;
    @FXML
    public TextField VilleTextbox;
    @FXML
    public TextField CpInputBox;
    @FXML
    public ChoiceBox TypeChoice;
    @FXML
    public Button EditBtn;
    private int idLogement;
    private String LibelleLogement;
    private String AdresseLogement;
    private String CpLogement;
    private String VilleLogement;
    private String TypeLog;

    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";
    private int NbPieceLogement;


    public boolean containsOnlyNumbers(String text) {
        return text.matches("\\d+"); // Matches one or more digits (0-9)
    }
    public void initialize() {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();

    }
    public void handleEditButtonClick() {
        String libelle = LibelleTextbox.getText();
        String adresse = AdresseTextbox.getText();
        String cp = CpInputBox.getText();
        String ville = VilleTextbox.getText();
        String type = TypeChoice.getValue().toString();

        int idType;
        if (type.equals("Appartement")) {
            idType = 1;
        } else if (type.equals("Maison")) {
            idType = 2;
        } else {
            showAlert("Input Validation Error", "Invalid Type selection.");
            return;
        }

        if (!validateInput(cp)) {
            showAlert("Erreur de validation des entrées", "Cp doit être numérique.");
        } else {
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String updateQuery = "UPDATE logement SET libelle=?, adresse=?, cp=?, ville=?, id_Type=? WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, libelle);
                preparedStatement.setString(2, adresse);
                preparedStatement.setString(3, cp);
                preparedStatement.setString(4, ville);
                preparedStatement.setInt(5, idType);
                preparedStatement.setInt(6, idLogement);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert("Success", "Logement a été mis à jour avec succès.");
                } else {
                    showAlert("Error", "Échec de la mise à jour du Logement.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage());
            }
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean validateInput(String input) {
        return input.matches("\\d+");
    }
    public void setIdLogement(int idLogement) {
        this.idLogement = idLogement;
    }
    public void setLibelleLogement(String LibelleLogement) {
        this.LibelleLogement = LibelleLogement;
        LibelleTextbox.setText(LibelleLogement);
    }
    public void setAdresseLogement(String AdresseLogement) {
        this.AdresseLogement = AdresseLogement;
        AdresseTextbox.setText(AdresseLogement);
    }
    public void setCpLogement(String CpLogement) {
        this.CpLogement = CpLogement;
        CpInputBox.setText(CpLogement);
    }
    public void setVilleLogement(String VilleLogement) {
        this.VilleLogement = VilleLogement;
        VilleTextbox.setText(VilleLogement);
    }
    public void setTypeLog(String TypeLog) {
        this.TypeLog = TypeLog;
        TypeChoice.setValue(TypeLog);
    }
}
