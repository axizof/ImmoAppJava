package com.example.immoapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseManager {
    static String jdbcUrl = "jdbc:mysql://localhost:3306/immoapp";
    static String username = "root";
    static String password = "";
    public static void logError(SQLLogException sqlLogException) {
        ConfigReader configReader = new ConfigReader();
        jdbcUrl = configReader.getJdbcUrl();
        username = configReader.getUsername();
        password = configReader.getPassword();
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
