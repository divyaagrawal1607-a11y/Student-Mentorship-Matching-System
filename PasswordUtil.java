package com.mentorbridge.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Secure password hashing and verification utility using BCrypt.
 */
public class PasswordUtil {

    private static final int LOG_ROUNDS = 12;

    /**
     * Hash a plain-text password with a secure salt.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verify a plain-text password against a stored BCrypt hash.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || storedHash.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
