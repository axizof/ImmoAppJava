package com.example.immoapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Bdd {
    private String URL = "jdbc:mysql://localhost:3306/immoAPP";
    private String login = "phpmyadmin";
    private String password = "0550002D";

    public void getConnexion(){
        try{
            Connection connexion = DriverManager.getConnection(URL,login,password);
            System.out.println("Connexion à la base réussi");
        }catch(SQLException e){

        }
    }
}
