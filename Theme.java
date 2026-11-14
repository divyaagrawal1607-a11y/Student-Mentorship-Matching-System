package com.mentorbridge.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * Modern Navy & Blue Theme Constants.
 */
public class Theme {
    // Colors
    public static final Color NAVY_DARK = new Color(15, 23, 42);      // #0F172A (Sidebar / Header)
    public static final Color NAVY_MEDIUM = new Color(30, 41, 59);    // #1E293B (Sidebar hover)
    public static final Color BLUE_PRIMARY = new Color(37, 99, 235);   // #2563EB (Brand accent)
    public static final Color BLUE_HOVER = new Color(29, 78, 216);     // #1D4ED8 (Button hover)
    public static final Color BLUE_LIGHT = new Color(239, 246, 255);   // #EFF6FF (Selection / highlight)
    
    public static final Color BG_PAGE = new Color(248, 250, 252);     // #F8FAFC (Body background)
    public static final Color BG_CARD = Color.WHITE;                  // Card background
    public static final Color BORDER = new Color(226, 232, 240);       // #E2E8F0 (Borders)
    
    public static final Color TEXT_MAIN = new Color(15, 23, 42);       // Dark slate
    public static final Color TEXT_MUTED = new Color(100, 116, 139);   // Slate 500
    public static final Color TEXT_LIGHT = new Color(241, 245, 249);   // Off-white
    
    public static final Color SUCCESS = new Color(16, 185, 129);       // Emerald 500
    public static final Color SUCCESS_BG = new Color(236, 253, 245);
    public static final Color WARNING = new Color(245, 158, 11);       // Amber 500
    public static final Color WARNING_BG = new Color(254, 243, 199);
    public static final Color DANGER = new Color(239, 68, 68);         // Red 500
    public static final Color DANGER_BG = new Color(254, 242, 242);
    public static final Color INFO = new Color(59, 130, 246);          // Blue 500
    public static final Color INFO_BG = new Color(239, 246, 255);

    // Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_STAT_VAL = new Font("Segoe UI", Font.BOLD, 28);
}
