package com.mentorbridge.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads database configuration from .env file or environment variables.
 * Keeps credentials protected and out of hard-coded sources.
 */
public class DatabaseConfig {

    private static String dbType = "mysql";
    private static String dbHost = "localhost";
    private static int dbPort = 3306;
    private static String dbName = "mentorship_db";
    private static String dbUser = "root";
    private static String dbPassword = "";
    private static boolean fallbackEmbedded = true;

    static {
        loadConfiguration();
    }

    public static void loadConfiguration() {
        Properties props = new Properties();

        // 1. Try to load from local .env file
        File envFile = new File(".env");
        if (!envFile.exists()) {
            envFile = new File("StudentMentorshipMatchingSystem/.env");
        }

        if (envFile.exists()) {
            try (FileInputStream fis = new FileInputStream(envFile)) {
                props.load(fis);
            } catch (IOException ignored) {}
        }

        // 2. Read properties with fallback to Environment Variables then defaults
        dbType = getVal(props, "DB_TYPE", System.getenv("DB_TYPE"), "mysql");
        dbHost = getVal(props, "DB_HOST", System.getenv("DB_HOST"), "localhost");
        
        String portStr = getVal(props, "DB_PORT", System.getenv("DB_PORT"), "3306");
        try {
            dbPort = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            dbPort = 3306;
        }

        dbName = getVal(props, "DB_NAME", System.getenv("DB_NAME"), "mentorship_db");
        dbUser = getVal(props, "DB_USER", System.getenv("DB_USER"), "root");
        dbPassword = getVal(props, "DB_PASSWORD", System.getenv("DB_PASSWORD"), "");
        
        String fallbackStr = getVal(props, "DB_FALLBACK_EMBEDDED", System.getenv("DB_FALLBACK_EMBEDDED"), "true");
        fallbackEmbedded = Boolean.parseBoolean(fallbackStr);
    }

    private static String getVal(Properties props, String key, String envVal, String defaultVal) {
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String p = props.getProperty(key);
        if (p != null && !p.trim().isEmpty()) {
            return p.trim();
        }
        return defaultVal;
    }

    public static String getJdbcUrl() {
        if ("h2".equalsIgnoreCase(dbType)) {
            return "jdbc:h2:file:./mentorship_data;MODE=MySQL;DATABASE_TO_LOWER=TRUE;NON_KEYWORDS=USER,KEY";
        }
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true",
                dbHost, dbPort, dbName);
    }

    public static String getFallbackJdbcUrl() {
        return "jdbc:h2:file:./mentorship_data;MODE=MySQL;DATABASE_TO_LOWER=TRUE;NON_KEYWORDS=USER,KEY";
    }

    public static String getDbType() { return dbType; }
    public static String getDbUser() { return dbUser; }
    public static String getDbPassword() { return dbPassword; }
    public static boolean isFallbackEmbedded() { return fallbackEmbedded; }

    public static void setCredentials(String user, String pass) {
        dbUser = user;
        dbPassword = pass;
    }
}
