package com.example.immoapp;

import java.awt.image.BufferedImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class AddPiece {
    @FXML
    private TextField surface;
    @FXML TextField libelle;
    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    public void initialize() {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();
    }
    @FXML
    private ImageView ImagePreview;
    private ArrayList<String> ImagesBase64 = new ArrayList<String>();
    private String encodedString = "";
    private String CurrentBLob64 = "";
    private int idLogement;

    public boolean containsOnlyNumbers(String text) {
        return text.matches("\\d+"); // Matches one or more digits (0-9)
    }

    public void setIdLogement(int idLogement) {
        this.idLogement = idLogement;
    }
    public void addPiece(String libelle, String surface, int id_Logement) {
        int CheckInfo;
        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO Piece (libelle, surface, id_Logement) " +
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
    public void BtnAjoutClick(ActionEvent actionEvent) {
        String surfaceText = surface.getText();
        if (containsOnlyNumbers(surfaceText)) {
            addPiece(libelle.getText(), surfaceText, idLogement);
        } else {
            System.out.println("Surface must contain only numbers.");
        }
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

