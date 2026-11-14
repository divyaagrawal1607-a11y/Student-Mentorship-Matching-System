package com.mentorbridge.database;

import com.mentorbridge.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized Database Connection Manager.
 * Handles MySQL connectivity with automatic resilient fallback.
 */
public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
    private static boolean usingFallback = false;

    static {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "MySQL Driver not found on classpath: " + e.getMessage());
        }

        try {
            // Load H2 Driver for fallback
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ignored) {}
    }

    /**
     * Get a connection using configured database properties.
     */
    public static Connection getConnection() throws SQLException {
        if (!usingFallback) {
            try {
                String url = DatabaseConfig.getJdbcUrl();
                String user = DatabaseConfig.getDbUser();
                String pass = DatabaseConfig.getDbPassword();
                return DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                if (DatabaseConfig.isFallbackEmbedded()) {
                    LOGGER.log(Level.WARNING, "Could not connect to MySQL server. Falling back to resilient local storage: " + e.getMessage());
                    usingFallback = true;
                    return DriverManager.getConnection(DatabaseConfig.getFallbackJdbcUrl(), "sa", "");
                } else {
                    throw e;
                }
            }
        } else {
            return DriverManager.getConnection(DatabaseConfig.getFallbackJdbcUrl(), "sa", "");
        }
    }

    public static boolean isUsingFallback() {
        return usingFallback;
    }

    public static void setUsingFallback(boolean fallback) {
        usingFallback = fallback;
    }

    /**
     * Test connection to database.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
