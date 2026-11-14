package com.mentorbridge.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Reusable UI component styling helper for MentorBridge.
 */
public class UIHelper {

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.BLUE_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.BLUE_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.BLUE_PRIMARY);
            }
        });
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY_BOLD);
        btn.setForeground(Theme.NAVY_DARK);
        btn.setBackground(Theme.BORDER);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(203, 213, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.BORDER);
            }
        });
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.DANGER);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.SUCCESS);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    public static JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        new LineBorder(Theme.BORDER, 1),
                        new EmptyBorder(14, 18, 14, 18)
                )
        ));

        JLabel titleLbl = new JLabel(title.toUpperCase());
        titleLbl.setFont(Theme.FONT_SMALL);
        titleLbl.setForeground(Theme.TEXT_MUTED);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(Theme.FONT_STAT_VAL);
        valLbl.setForeground(Theme.NAVY_DARK);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);
        return card;
    }

    public static void styleTable(JTable table) {
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(36);
        table.setGridColor(Theme.BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(Theme.BLUE_LIGHT);
        table.setSelectionForeground(Theme.BLUE_PRIMARY);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_BODY_BOLD);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(Theme.NAVY_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        }
    }

    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(Theme.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(Theme.FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    public static JLabel createBadge(String text, Color fg, Color bg) {
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(fg, 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        return lbl;
    }
}
