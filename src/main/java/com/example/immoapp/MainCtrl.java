package com.example.immoapp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {
    @FXML
    private TableView<BienImmobilier> tableView;
    @FXML
    private TableColumn<BienImmobilier, String> nomColumn;
    @FXML
    private TableColumn<BienImmobilier, Integer> nbPiecesColumn;
    @FXML
    private TableColumn<BienImmobilier, String> codePostalColumn;
    @FXML
    private TableColumn<BienImmobilier, String> villeColumn;
    @FXML
    private TableColumn<BienImmobilier, Integer> idColumn;
    @FXML
    private TableColumn<BienImmobilier, String> adresselog;
    @FXML
    private TextField filterTextField;

    private ObservableList<BienImmobilier> biens = FXCollections.observableArrayList();
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
    private Button EditBtn;
    @FXML
    private Button AddBtnLog;
    @FXML
    private Label libelleinfo;

    @FXML
    private ImageView ImageView;

    private List<String> images = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        panetop.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        EditBtn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditLog.fxml"));
            Parent root;
            try {
                root = loader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        // Initialisation de la TableView avec les données des biens
        tableView.setItems(biens);
        // Association des colonnes de la TableView aux propriétés du modèle BienImmobilier
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        nbPiecesColumn.setCellValueFactory(cellData -> cellData.getValue().nbPiecesProperty().asObject());
        codePostalColumn.setCellValueFactory(cellData -> cellData.getValue().codePostalProperty());
        villeColumn.setCellValueFactory(cellData -> cellData.getValue().villeProperty());
        adresselog.setCellValueFactory(cellData -> cellData.getValue().addresseProperty());

        // Ajout d'un Listener pour le filtre par code postal
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterByPostalCode(newValue);
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                libelleinfo.setText(newValue.getNom());
            } else {
                libelleinfo.setText("");
            }
        });

        AddBtnLog.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBien.fxml"));
            Parent root;
            try {
                root = loader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Chargement des logements depuis la base de données
        loadLogementsFromDatabase();
    }



    // Méthode pour filtrer les biens par code postal


    private void filterByPostalCode(String postalCode) {
        if (postalCode == null || postalCode.isEmpty()) {
            // Si le champ est vide on affiche tous les biens
            tableView.setItems(biens);
        } else {
            // Filtre de la liste des biens en fonction du code postal
            ObservableList<BienImmobilier> filteredList = FXCollections.observableArrayList();
            for (BienImmobilier bien : biens) {
                if (bien.getCodePostal().contains(postalCode)) {
                    filteredList.add(bien);
                }
            }
            // Mettre à jour la TableView suivant les résultats du filtre
            tableView.setItems(filteredList);
        }
    }
    // chargement des logements depuis la base de données
    private void loadLogementsFromDatabase() {
        String jdbcUrl = "jdbc:mysql://172.19.0.32:3306/immoAPP";
        String username = "mysqluser";
        String password = "0550002D";

        try {
            // connexion à la base de données
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);

            // requête SQL pour sélectionner les logements
            String sql = "SELECT L.libelle, COUNT(P.id_Logement) AS nbPieces, L.cp, L.ville, L.id, L.adresse\n" +
                    "FROM Logement L\n" +
                    "LEFT JOIN Piece P ON L.id = P.id_Logement\n" +
                    "GROUP BY L.libelle, L.cp, L.ville, L.id, L.adresse";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Exécution de la requête SQL
            ResultSet rs = stmt.executeQuery();

            // Parcour les résultats et ajouter les logements à la liste
            while (rs.next()) {
                String libelle = rs.getString("libelle");
                int nbPieces = rs.getInt("nbPieces");
                String cp = rs.getString("cp");
                String ville = rs.getString("ville");
                int idlog = rs.getInt("id");
                String adresselog = rs.getString("adresse");

                // Créez un objet BienImmobilier et ajout dans à la liste
                BienImmobilier bien = new BienImmobilier(libelle, nbPieces, cp, ville, idlog, adresselog);
                biens.add(bien);
            }

            // Fermer les truc ouvert
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Modèle BienImmobilier
    public static class BienImmobilier {
        private final StringProperty nom = new SimpleStringProperty();
        private final IntegerProperty nbPieces = new SimpleIntegerProperty();
        private final StringProperty codePostal = new SimpleStringProperty();
        private final StringProperty ville = new SimpleStringProperty();
        private final IntegerProperty idlog = new SimpleIntegerProperty();
        private final StringProperty adresselog = new SimpleStringProperty();

        public BienImmobilier(String nom, int nbPieces, String codePostal, String ville,int idlog, String adresselog) {
            this.nom.set(nom);
            this.nbPieces.set(nbPieces);
            this.codePostal.set(codePostal);
            this.ville.set(ville);
            this.idlog.set(idlog);
            this.adresselog.set(adresselog);
        }

        // Getters
        public String getNom() {
            return nom.get();
        }

        public StringProperty nomProperty() {
            return nom;
        }

        public int getNbPieces() {
            return nbPieces.get();
        }

        public IntegerProperty nbPiecesProperty() {
            return nbPieces;
        }

        public String getCodePostal() {
            return codePostal.get();
        }

        public StringProperty codePostalProperty() {
            return codePostal;
        }

        public String getVille() {
            return ville.get();
        }

        public StringProperty villeProperty() {
            return ville;
        }

        public String getAddresse() {
            return adresselog.get();
        }

        public StringProperty addresseProperty() {
            return adresselog;
        }
        public int getIdlog() {
            return idlog.get();
        }

        public IntegerProperty idProperty() {
            return idlog;
        }
    }
}
