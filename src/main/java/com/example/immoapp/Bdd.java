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
        this.URL = "jdbc:mysql://172.19.0.32:3306/immoAPP";
        this.login = "mysqluser";
        this.password = "0550002D";
    }
    public void getConnexion(){
        try{
            con = DriverManager.getConnection(URL,login,password);
            System.out.println("Connexion à la base réussi");
        }catch(SQLException e){
            System.out.println("ERREUR");
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }
}
