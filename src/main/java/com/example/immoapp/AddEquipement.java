package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Logger;

public class AddEquipement {
    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    @FXML TextField libelle;

    @FXML
    private ImageView ImagePreview;
    private ArrayList<String> ImagesBase64 = new ArrayList<String>();
    private String encodedString = "";
    private String CurrentBLob64 = "";

    private int idPiece;

    public boolean containsOnlyNumbers(String text) {
        return text.matches("\\d+");
    }
    public void initialize() {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();
    }
    public void addEquipement(String libelle, int id_Piece) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            String checkSql = "SELECT id FROM Equipement WHERE libelle = ? AND id_Piece = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, libelle);
            checkStmt.setInt(2, id_Piece);
            ResultSet checkRs = checkStmt.executeQuery();
            System.out.println(id_Piece);
            if (checkRs.next()) {
                showDuplicateEquipementAlert();
            } else {

                String insertSql = "INSERT INTO Equipement (libelle, id_Piece) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, libelle);
                insertStmt.setInt(2, id_Piece);
                insertStmt.executeUpdate();

                String checkSql2 = "SELECT id FROM Equipement WHERE libelle = ? AND id_Piece = ?";
                PreparedStatement checkStmt2 = conn.prepareStatement(checkSql);
                checkStmt.setString(1, libelle);
                checkStmt.setInt(2, id_Piece);
                ResultSet checkRs2 = checkStmt.executeQuery();
                int ideqInd = 0;
                if (checkRs2.next()) {
                    ideqInd = checkRs2.getInt("id");
                }

                for (int i = 0; i < ImagesBase64.size(); i++) {
                    String photoSql = "INSERT INTO Photo (id_Equipement, photo) VALUES (?, ?)";
                    PreparedStatement photoStmt = conn.prepareStatement(photoSql);
                    photoStmt.setInt(1, ideqInd);
                    photoStmt.setString(2, ImagesBase64.get(i));
                    photoStmt.executeUpdate();
                }

                showConfirmationDialog();

                closeWindow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPiece(int idPiece) {
        this.idPiece = idPiece;
    }
    private void showDuplicateEquipementAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Équipement en double");
        alert.setContentText("Un équipement avec le même libellé existe déjà. Le nom doit être unique.");
        alert.showAndWait();
    }
    public void BtnAjoutClick(ActionEvent actionEvent) {
        addEquipement(libelle.getText(), idPiece);
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Piece added successfully.");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) libelle.getScene().getWindow();
        stage.close();
    }

    public void BtnAjoutImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            WritableImage image = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);

            FileInputStream fin = new FileInputStream(file);


            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fin.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            byte[] person_image = bos.toByteArray();
            encodedString = "data:image/png;base64," + Base64.getEncoder().encodeToString(person_image);
            ImagesBase64.add(encodedString);
            CurrentBLob64 = encodedString;
            ImagePreview.setImage(new Image(ImagesBase64.get(ImagesBase64.size() -1)));

            //System.out.println(encodedString);

        } catch (IOException ex) {
            Logger.getLogger("ss");
        }
    }
    public void BtnSupprImage(ActionEvent actionEvent) {
        try {
            ImagesBase64.remove(ImagesBase64.indexOf(CurrentBLob64));
            if (ImagesBase64.size() > 0){
                ImagePreview.setImage(new Image(ImagesBase64.get(0)));
            }
            else{
                ImagePreview.setImage(null);
            }
        }
        catch (Exception e){
            ImagePreview.setImage(null);
        }
    }
    public void BtnLeftPreview(ActionEvent actionEvent) {
        try {
            int IndCurrentBLob64 = ImagesBase64.indexOf(CurrentBLob64);
            if (IndCurrentBLob64 != -1 && IndCurrentBLob64 > 0){
                CurrentBLob64 = ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) - 1);
                ImagePreview.setImage(new Image(CurrentBLob64));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void BtnRightPreview(ActionEvent actionEvent) {
        try {
            int IndCurrentBLob64 = ImagesBase64.indexOf(CurrentBLob64);
            if (IndCurrentBLob64 != -1 && IndCurrentBLob64 < ImagesBase64.size() - 1){
                CurrentBLob64 = ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) + 1);
                ImagePreview.setImage(new Image(CurrentBLob64));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
