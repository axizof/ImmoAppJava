package com.example.immoapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseManager {

    public static void logError(SQLLogException sqlLogException) {
        String jdbcUrl = "jdbc:mysql://172.19.0.32:3306/ImmoAppErr";
        String username = "mysqluser";
        String password = "0550002D";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String sql = "INSERT INTO error_log (error_type, error_message) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sqlLogException.getErrorType());
            preparedStatement.setString(2, sqlLogException.getErrorMessage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
