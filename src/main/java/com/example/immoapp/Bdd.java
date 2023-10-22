package com.example.immoapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Bdd {
    // test
    private String URL;
    private String login;
    private String password;

    public Connection con;

    public Bdd(){
        this.URL = "jdbc:mysql://localhost:3306/immoapp";
        this.login = "root";
        this.password = "";
    }
    public void getConnexion(){
        ConfigReader configReader = new ConfigReader();
        URL = configReader.getJdbcUrl();
        login = configReader.getUsername();
        password = configReader.getPassword();
        try{
            con = DriverManager.getConnection(URL,login,password);
            System.out.println("Connexion à la base réussi");
        } catch(SQLException e){
            System.out.println("ERREUR");
            System.out.println(e.getMessage());
            SQLLogException sqlLogException = new SQLLogException(e);
            DatabaseManager.logError(sqlLogException);
        }
    }
}
