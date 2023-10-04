package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class auth implements Initializable {

    @FXML
    private PasswordField mdp;

    @FXML
    private TextField login;
    @FXML
    private Button connexion;
    @FXML
    private Pane panetop;

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label labelclose;
    @FXML
    private Pane btnclose;
    @FXML
    private Pane btnmin;
    @FXML
    private Label labelmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Bdd bdd = new Bdd();
        bdd.getConnexion();
        panetop.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        panetop.setOnMouseDragged(event -> {
            Stage stage = (Stage) panetop.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        btnclose.setOnMouseClicked((MouseEvent event) -> {
            System.exit(0);
        });
        labelclose.setOnMouseClicked((MouseEvent event) -> {
            System.exit(0);
        });
        btnmin.setOnMouseClicked((MouseEvent event) -> {
            Stage stage = (Stage) panetop.getScene().getWindow();
            stage.setIconified(true);
        });
        labelmin.setOnMouseClicked((MouseEvent event) -> {
            Stage stage = (Stage) panetop.getScene().getWindow();
            stage.setIconified(true);
        });
    }
    @FXML
    private void handleConnexion(ActionEvent event) throws IOException {
        String enteredPassword = mdp.getText();
        String enteredLogin = login.getText();

        // Check if the entered password is equal to "admin"
        if ("admin".equals(enteredPassword)) {
            // Load the new scene (Main.fxml in this example)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) connexion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }else {
            Bdd bdd = new Bdd();
            bdd.getConnexion();
            try{
                PreparedStatement login = bdd.con.prepareStatement("SELECT login,mdp FROM Employe WHERE login = ?");
                login.setString(1, enteredLogin);
                ResultSet resultat = login.executeQuery();
                if(resultat.next()){
                    if(BCrypt.checkpw(enteredPassword, resultat.getString(2))) {
                        resultat.close();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) connexion.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                    }
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Mauvais login/mot de passe");
                    a.show();
                }
            }catch (SQLException e){
                SQLLogException sqlLogException = new SQLLogException(e);
                DatabaseManager.logError(sqlLogException);
                System.out.println(e);
            }


        }
    }
}