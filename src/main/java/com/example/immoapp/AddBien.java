package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    @FXML
    private ImageView ImagePreview;
    private ArrayList<String> ImagesBase64 = new ArrayList<String>();
    private String encodedString = "";
    private String CurrentBLob64 = "";

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
    public void CheckVerif(ActionEvent actionEvent){
        if(actionEvent.getSource().toString().contains("CheckAppart")){
            CheckMaison.setSelected(false);
        }
        else{
            CheckAppart.setSelected(false);
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
            if (ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) - 1) != null){
                CurrentBLob64 = ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BtnRightPreview(ActionEvent actionEvent) {
        try {
            if (ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) + 1) != null){
                CurrentBLob64 = ImagesBase64.get(ImagesBase64.indexOf(CurrentBLob64) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
