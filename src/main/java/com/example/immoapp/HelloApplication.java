package com.example.immoapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spsc.fxml"));
        Parent root = loader.load();
        spsc spscController = loader.getController();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
            stage.hide();
            showAuthPage();
        }));
        timeline.setCycleCount(1);
        timeline.play();

        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("spsc.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        //stage.setTitle("immoAPP");
        //stage.setScene(scene);
        //stage.show();
    }

    public void showAuthPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("auth.fxml"));
            Parent authRoot = loader.load();
            Scene authScene = new Scene(authRoot);
            Stage authStage = new Stage();
            authStage.initStyle(StageStyle.UNDECORATED);
            authStage.setScene(authScene);
            authStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch();
    }
}

