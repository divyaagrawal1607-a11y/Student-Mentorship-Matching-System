package com.mentorbridge;

import com.mentorbridge.database.DatabaseInitializer;
import com.mentorbridge.ui.LoginFrame;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Application Entry Point for MentorBridge.
 * Student Mentorship Matching System - NSP Java Mini Project (Divya Agarwal).
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        // Set Swing system properties for crisp modern rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set FlatLaf Look and Feel if available, fallback to System LaF
        setupLookAndFeel();

        // Initialize Database Schema & Seed Data idempotently
        LOGGER.info("Initializing database...");
        DatabaseInitializer.initialize();

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
                LOGGER.info("MentorBridge GUI launched successfully.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to launch MentorBridge UI", e);
                JOptionPane.showMessageDialog(null,
                        "Failed to launch application:\n" + e.getMessage(),
                        "Launch Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void setupLookAndFeel() {
        try {
            // Attempt to load FlatLaf Light
            Class<?> flatLafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            Object instance = flatLafClass.getDeclaredConstructor().newInstance();
            UIManager.setLookAndFeel((LookAndFeel) instance);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
    }
}
