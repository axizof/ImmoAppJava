package com.example.immoapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class auth {

    @FXML
    private PasswordField mdp;

    @FXML
    private TextField login;
    @FXML
    private Button connexion;

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
                    if(resultat.getString(2).equals(enteredPassword)) {
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