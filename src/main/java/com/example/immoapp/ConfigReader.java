package com.example.immoapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE_PATH = "/com/example/immoapp/config.ini";
    private Properties properties;

    public ConfigReader() {
        properties = loadConfigProperties();
    }

    private Properties loadConfigProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Le fichier config.ini n'a pas été trouvé dans le classpath.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public String getJdbcUrl() {
        return properties.getProperty("jdbcUrl");
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }
}