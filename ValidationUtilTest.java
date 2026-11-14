package com.mentorbridge;

import com.mentorbridge.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    public void testValidEmails() {
        assertTrue(ValidationUtil.isValidEmail("divya.agarwal@student.edu"));
        assertTrue(ValidationUtil.isValidEmail("admin@mentorbridge.edu"));
        assertTrue(ValidationUtil.isValidEmail("test_user+1@domain.co.in"));
    }

    @Test
    public void testInvalidEmails() {
        assertFalse(ValidationUtil.isValidEmail("plainaddress"));
        assertFalse(ValidationUtil.isValidEmail("@missingusername.com"));
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
    }

    @Test
    public void testStudyYear() {
        assertTrue(ValidationUtil.isValidStudyYear(1));
        assertTrue(ValidationUtil.isValidStudyYear(4));
        assertFalse(ValidationUtil.isValidStudyYear(0));
        assertFalse(ValidationUtil.isValidStudyYear(7));
    }

    @Test
    public void testRating() {
        assertTrue(ValidationUtil.isValidRating(1));
        assertTrue(ValidationUtil.isValidRating(5));
        assertFalse(ValidationUtil.isValidRating(0));
        assertFalse(ValidationUtil.isValidRating(6));
    }

    @Test
    public void testPhoneNumber() {
        assertTrue(ValidationUtil.isValidPhone("+91 98765 43210"));
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
        assertFalse(ValidationUtil.isValidPhone("abc"));
    }
}
