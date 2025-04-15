package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config config;
    private Properties properties;
    private static final String CONFIG_PROPERTIES = "config.properties";

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) {
                System.err.println("File " + fileName + " not found in the classpath");
                return;
            }
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("Error while reading the file " + fileName);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        String value = getConfig().properties.getProperty(key);
        System.out.println("Property " + key + ": " + value);  // Добавьте логирование
        return value;
    }
}