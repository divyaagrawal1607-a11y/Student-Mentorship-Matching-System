package com.mentorbridge.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date and time formatting and parsing utilities.
 */
public class DateTimeUtil {

    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format(LocalDateTime ldt) {
        if (ldt == null) return "N/A";
        return ldt.format(DISPLAY_FORMATTER);
    }

    public static String formatDateOnly(LocalDateTime ldt) {
        if (ldt == null) return "N/A";
        return ldt.format(DATE_ONLY_FORMATTER);
    }

    public static LocalDateTime parse(String str) {
        if (str == null || str.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(str.trim(), DISPLAY_FORMATTER);
        } catch (Exception e) {
            try {
                return LocalDateTime.parse(str.trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
