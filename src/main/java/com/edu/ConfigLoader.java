package com.edu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();

    static {
        try (FileInputStream inputStream = new FileInputStream("target/classes/application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserEmail() {
        return properties.getProperty("user.email");
    }

    public static String getUserPassword() {
        return properties.getProperty("user.password");
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBasePath() {
        return properties.getProperty("base.path");
    }

}
