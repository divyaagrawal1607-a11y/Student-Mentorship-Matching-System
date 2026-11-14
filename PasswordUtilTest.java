package com.mentorbridge;

import com.mentorbridge.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testPasswordHashingAndVerification() {
        String raw = "DivyaSecure2026!";
        String hash = PasswordUtil.hashPassword(raw);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
        assertTrue(PasswordUtil.verifyPassword(raw, hash));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash));
        assertFalse(PasswordUtil.verifyPassword("", hash));
        assertFalse(PasswordUtil.verifyPassword(null, hash));
    }
}
