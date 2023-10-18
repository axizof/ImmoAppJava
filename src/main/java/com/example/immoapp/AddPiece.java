package com.example.immoapp;

import java.awt.image.BufferedImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

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
    private TextField libelle;
    @FXML
    private Spinner surface;
    @FXML
    private ImageView ImagePreview;
    private ArrayList<String> ImagesBase64 = new ArrayList<String>();
    private String encodedString = "";
    private String CurrentBLob64 = "";

    String jdbcUrl = "jdbc:mysql://172.19.0.32:3306/immoAPP";
    String username = "mysqluser";
    String password = "0550002D";

    public void addPiece(String libelle, String surface ) {
        int CheckInfo;
        try{
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            String sql = "INSERT INTO Piece (libelle, surface, id_Logement) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, libelle);
            stmt.setString(2, surface);
            stmt.setString(3, cp);
            stmt.executeUpdate();
            sql = "SELECT ID FROM Logement WHERE libelle = ? AND adresse = ? AND cp = ? AND ville = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql);
            stmt2.setString(1, libelle);
            stmt2.setString(2, adresselog);
            stmt2.setString(3, cp);
            stmt2.setString(4, ville);
            ResultSet rs = stmt2.executeQuery();
            int idlogInd = rs.getInt("ID");
            for (int i = 0; i < ImagesBase64.size(); i++) {
                sql = "INSERT INTO Image (id_Logement, image) " +
                        "VALUES (?, ?)";
                PreparedStatement stmt3 = conn.prepareStatement(sql);
                stmt3.setInt(1, idlogInd);
                stmt3.setString(2, ImagesBase64.get(i));
                stmt3.executeUpdate();
            }
            stmt.close();
            stmt2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void BtnAjoutClick(ActionEvent actionEvent) {
        addPiece(libelle.getText(), (String) surface.getValue());
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
