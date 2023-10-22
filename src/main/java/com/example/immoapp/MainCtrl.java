package com.example.immoapp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
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
    private TableColumn<BienImmobilier, String> TypeLog;
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

    @FXML
    private ImageView ImageViewPiece;

    private List<String> imageslog = new ArrayList<>();

    private int idLogement;
    private int idPiece;
    private int nbTypeLog;
    int currentImageIndex = 0;
    String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    String username = "root";
    String password = "";

    private String LibelleLogement;
    private String AdresseLogement;
    private String CpLogement;
    private String VilleLogement;

    private int nbPiece;
    private int currentPieceIndex = 0;
    private int currentImageIndexPiece = 0;


    ObservableList<Piece> pieces = FXCollections.observableArrayList();
    @FXML
    private TableView tableviewpiece;


    private ArrayList<String> imagespiece = new ArrayList<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();

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

        // Initialisation de la TableView avec les données des biens
        tableView.setItems(biens);
        // Association des colonnes de la TableView aux propriétés du modèle BienImmobilier
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        nbPiecesColumn.setCellValueFactory(cellData -> cellData.getValue().nbPiecesProperty().asObject());
        codePostalColumn.setCellValueFactory(cellData -> cellData.getValue().codePostalProperty());
        villeColumn.setCellValueFactory(cellData -> cellData.getValue().villeProperty());
        adresselog.setCellValueFactory(cellData -> cellData.getValue().addresseProperty());
        TypeLog.setCellValueFactory(cellData -> {
            IntegerProperty typeLogProperty = cellData.getValue().nbTypeLog();
            String typeLogString = "";
            if (typeLogProperty.get() == 1) {
                typeLogString = "Appartement";
            } else if (typeLogProperty.get() == 2) {
                typeLogString = "Maison";
            }
            return new SimpleStringProperty(typeLogString);
        });

        // Ajout d'un Listener pour le filtre par code postal
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterByPostalCode(newValue);
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                libelleinfo.setText(newValue.getNom());
                idLogement = newValue.getIdlog();
                imageslog = getImagesForLogement(newValue.getIdlog());
                nbTypeLog = newValue.getTypeLog();
                LibelleLogement = newValue.getNom();
                AdresseLogement = newValue.getAddresse();
                CpLogement = newValue.getCodePostal();
                VilleLogement = newValue.getVille();
                nbPiece = newValue.getNbPieces();
                loadPiecesForLogement(idLogement);
                System.out.println(newValue.getTypeLog() + " " + nbTypeLog + " " + LibelleLogement + " " + newValue.getNom());
                //System.out.println("Chargement des pièces pour le logement avec ID : " + idLogement);
                if (!imageslog.isEmpty()) {
                    ImageView.setImage(new Image(imageslog.get(0)));
                } else {
                    ImageView.setImage(new Image("data:image/webp;base64,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AABkAgAAA6AEAAEAAABkAgAAAAAAAFhNUCByAgAAPD94cGFja2V0IGJlZ2luPSIiIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iR28gWE1QIFNESyAxLjAiPjxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+PHJkZjpEZXNjcmlwdGlvbiB4bWxuczp4bXBSaWdodHM9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9yaWdodHMvIiByZGY6YWJvdXQ9IiIgeG1wUmlnaHRzOldlYlN0YXRlbWVudD0iaHR0cHM6Ly93d3cuaXN0b2NrcGhvdG8uY29tL2xlZ2FsL2xpY2Vuc2UtYWdyZWVtZW50P3V0bV9tZWRpdW09b3JnYW5pYyZhbXA7dXRtX3NvdXJjZT1nb29nbGUmYW1wO3V0bV9jYW1wYWlnbj1pcHRjdXJsIj48L3JkZjpEZXNjcmlwdGlvbj48cmRmOkRlc2NyaXB0aW9uIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgcmRmOmFib3V0PSIiPjxkYzpjcmVhdG9yPjxyZGY6U2VxPjxyZGY6bGk+bWlsa2dob3N0PC9yZGY6bGk+PC9yZGY6U2VxPjwvZGM6Y3JlYXRvcj48L3JkZjpEZXNjcmlwdGlvbj48L3JkZjpSREY+PC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InciPz4="));
                }
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


    public void loadPiecesForLogement(int idLogement) {
        pieces.clear();

        List<Piece> piecesList = getPiecesForLogement(idLogement);

        pieces.addAll(piecesList);

        tableviewpiece.setItems(pieces);
    }



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

        try {
            // connexion à la base de données
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);

            // requête SQL pour sélectionner les logements
            String sql = "SELECT L.libelle, COUNT(P.id_Logement) AS nbPieces, L.cp, L.ville, L.id, L.adresse , L.id_Type\n" +
                    "FROM Logement L\n" +
                    "LEFT JOIN Piece P ON L.id = P.id_Logement\n" +
                    "GROUP BY L.libelle, L.cp, L.ville, L.id, L.adresse, L.id_Type";
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
                int TypeLog = rs.getInt("id_Type");


                // Créez un objet BienImmobilier et ajout dans à la liste
                BienImmobilier bien = new BienImmobilier(libelle, nbPieces, cp, ville, idlog, adresselog, TypeLog);
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


    public void handleAddPieceBtnClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPiece.fxml"));
            Parent root = loader.load();
            AddPiece addPieceController = loader.getController();
            addPieceController.setIdLogement(idLogement); // Passer l'ID du logement
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getImagesForLogement(int logementId) {
        List<String> images = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);

            String sql = "SELECT photo FROM Photo WHERE id_Logement = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, logementId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String imageBytes = rs.getString("photo");
                images.add(imageBytes);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return images;
    }

    public void rigthprev(ActionEvent actionEvent) {
        if (!imageslog.isEmpty()) {
            currentImageIndex++;
            if (currentImageIndex >= imageslog.size()) {
                currentImageIndex = 0;
            }
            ImageView.setImage(new Image(imageslog.get(currentImageIndex)));
        }
    }

    public void leftprev(ActionEvent actionEvent) {
        if (!imageslog.isEmpty()) {
            currentImageIndex--;
            if (currentImageIndex < 0) {
                currentImageIndex = imageslog.size() - 1;
            }
            ImageView.setImage(new Image(imageslog.get(currentImageIndex)));
        }
    }
    public List<Piece> getPiecesForLogement(int idLogement) {
        List<Piece> pieces = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sql = "SELECT P.id, P.libelle, P.surface, P.id_Logement, PH.photo " +
                    "FROM Piece P " +
                    "LEFT JOIN Photo PH ON P.id = PH.id_Piece " +
                    "WHERE P.id_Logement = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idLogement);

            ResultSet rs = stmt.executeQuery();

            Piece currentPiece = null;
            int currentPieceId = -1;

            while (rs.next()) {
                int id = rs.getInt("id");
                String libelle = rs.getString("libelle");
                int surface = rs.getInt("surface");
                int id_Logement = rs.getInt("id_Logement");
                String image = rs.getString("photo");

                if (id != currentPieceId) {
                    if (currentPiece != null) {
                        pieces.add(currentPiece);
                    }
                    currentPiece = new Piece(id, libelle, surface, id_Logement);
                    currentPieceId = id;
                }

                if (image != null) {
                    currentPiece.addImage(image);
                }
            }

            if (currentPiece != null) {
                pieces.add(currentPiece);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pieces;
    }

    public List<String> getImagesForPiece(int idPiece) {
        List<String> images = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sql = "SELECT photo FROM Photo WHERE id_Piece = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPiece);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String imageBytes = rs.getString("photo");
                images.add(imageBytes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        displayImageAtIndex(0);
        return images;
    }
    private void displayImageAtIndex(int index) {
        if (currentImageIndex >= 0 && currentImageIndex < pieces.size()) {
            List<String> images = pieces.get(currentImageIndex).getImages();

            if (!images.isEmpty()) {
                int imageIndex = index % images.size();
                String image = images.get(imageIndex);

                if (image != null) {
                    ImageViewPiece.setImage(new Image(image));
                } else {
                    ImageViewPiece.setImage(null);
                }
            } else {
                ImageViewPiece.setImage(null);
            }
        } else {
            ImageViewPiece.setImage(null);
        }
    }
    public void rigthprevpiece(ActionEvent actionEvent) {
        if (currentImageIndex < pieces.size() - 1) {
            currentImageIndex++;
            displayImageAtIndex(currentImageIndex);
        }
    }

    public void leftprevpiece(ActionEvent actionEvent) {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            displayImageAtIndex(currentImageIndex);
        }
    }

    public void handleAddEquipementBtnClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEquipement.fxml"));
            Parent root = loader.load();
            AddEquipement addPieceController = loader.getController();
            addPieceController.setPiece(idPiece);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEditBtnClick(ActionEvent actionEvent) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditLog.fxml"));
            Parent root;
            try {
                root = loader.load();
                EditLog EditLogController = loader.getController();
                EditLogController.setIdLogement(idLogement);
                EditLogController.setLibelleLogement(LibelleLogement);
                EditLogController.setAdresseLogement(AdresseLogement);
                EditLogController.setCpLogement(CpLogement);
                EditLogController.setVilleLogement(VilleLogement);
                String tempa = "";
                if (nbTypeLog == 1) {
                    tempa = "Appartement";
                } else if (nbTypeLog == 2) {
                    tempa = "Maison";
                }
                EditLogController.setTypeLog(tempa);
                System.out.println(idLogement + LibelleLogement);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void handleRefreshListBtnClick(ActionEvent actionEvent) {
        tableView.refresh();
        tableView.setItems(biens);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        nbPiecesColumn.setCellValueFactory(cellData -> cellData.getValue().nbPiecesProperty().asObject());
        codePostalColumn.setCellValueFactory(cellData -> cellData.getValue().codePostalProperty());
        villeColumn.setCellValueFactory(cellData -> cellData.getValue().villeProperty());
        adresselog.setCellValueFactory(cellData -> cellData.getValue().addresseProperty());
        TypeLog.setCellValueFactory(cellData -> {
            IntegerProperty typeLogProperty = cellData.getValue().nbTypeLog();
            String typeLogString = "";
            if (typeLogProperty.get() == 1) {
                typeLogString = "Appartement";
            } else if (typeLogProperty.get() == 2) {
                typeLogString = "Maison";
            }
            return new SimpleStringProperty(typeLogString);
        });
    }


    public class Piece {
        private int id;
        private String libelle;
        private int surface;
        private int id_Logement;
        private List<String> images;

        public Piece(int id, String libelle, int surface, int id_Logement) {
            this.id = id;
            this.libelle = libelle;
            this.surface = surface;
            this.id_Logement = id_Logement;
            this.images = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public String getLibelle() {
            return libelle;
        }

        public int getSurface() {
            return surface;
        }

        public int getId_Logement() {
            return id_Logement;
        }

        public List<String> getImages() {
            return images;
        }

        public void addImage(String image) {
            images.add(image);
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

        private final IntegerProperty TypeLog = new SimpleIntegerProperty();

        public BienImmobilier(String nom, int nbPieces, String codePostal, String ville,int idlog, String adresselog, int TypeLog) {
            this.nom.set(nom);
            this.nbPieces.set(nbPieces);
            this.codePostal.set(codePostal);
            this.ville.set(ville);
            this.idlog.set(idlog);
            this.adresselog.set(adresselog);
            this.TypeLog.set(TypeLog);
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
        public int getTypeLog() {
            return TypeLog.get();
        }
        public IntegerProperty nbTypeLog() {
            return TypeLog;
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
