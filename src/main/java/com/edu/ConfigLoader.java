package com.edu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ConfigLoader {
    private static Properties properties = new Properties();

    static {
        try(FileInputStream inputStream = new FileInputStream("target/classes/application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUrl(){
        return properties.getProperty("base.url");
    }

    public static String getBasePath(){
        return properties.getProperty("base.path");
    }

    public static Map<String,String> getBaseHeader() {
        return Map.of("requestSpec.baseHeaderName", "requestSpec.baseHeaderValue");
    }
}
