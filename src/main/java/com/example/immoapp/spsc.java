package com.example.immoapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class spsc implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Pane panetop;
    @FXML
    private Label labelclose;
    @FXML
    private Pane btnclose;
    @FXML
    private Pane btnmin;
    @FXML
    private Label labelmin;

    @FXML
    private ProgressBar pgbar;

    protected void onHelloButtonClick() {
        System.out.println("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pgbar.setProgress(0.0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int finalI = i;
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pgbar.setProgress(finalI / 100.0);
                }
            }
        }).start();

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

}