package com.mentorbridge.ui;

import com.mentorbridge.models.User;
import com.mentorbridge.services.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Modern Login Screen for MentorBridge.
 */
public class LoginFrame extends JFrame {

    private final AuthService authService = new AuthService();
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JLabel lblError;

    public LoginFrame() {
        setTitle("MentorBridge - Student Mentorship Matching System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_PAGE);

        // Header Panel (Navy Banner)
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(Theme.NAVY_DARK);
        header.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("MentorBridge", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Student Mentorship Matching System • NSP Java Mini Project", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(new Color(148, 163, 184));

        header.add(title);
        header.add(subtitle);
        root.add(header, BorderLayout.NORTH);

        // Center Card
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.BG_PAGE);
        center.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        int row = 0;

        JLabel lblSignIn = new JLabel("Sign In to Your Account");
        lblSignIn.setFont(Theme.FONT_SUBTITLE);
        lblSignIn.setForeground(Theme.NAVY_DARK);
        gbc.gridy = row++;
        card.add(lblSignIn, gbc);

        lblError = new JLabel(" ");
        lblError.setFont(Theme.FONT_SMALL);
        lblError.setForeground(Theme.DANGER);
        gbc.gridy = row++;
        card.add(lblError, gbc);

        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(Theme.FONT_BODY_BOLD);
        gbc.gridy = row++;
        card.add(lblEmail, gbc);

        tfEmail = UIHelper.createTextField(20);
        tfEmail.setText("admin@mentorbridge.edu");
        gbc.gridy = row++;
        card.add(tfEmail, gbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(Theme.FONT_BODY_BOLD);
        gbc.gridy = row++;
        card.add(lblPass, gbc);

        pfPassword = UIHelper.createPasswordField(20);
        pfPassword.setText("admin123");
        gbc.gridy = row++;
        card.add(pfPassword, gbc);

        JButton btnLogin = UIHelper.createPrimaryButton("Sign In");
        btnLogin.addActionListener(e -> performLogin());
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 6, 6, 6);
        card.add(btnLogin, gbc);

        // Quick Demo Fill Buttons
        JPanel demoBar = new JPanel(new GridLayout(1, 3, 5, 5));
        demoBar.setBackground(Theme.BG_CARD);

        JButton btnDemoAdmin = new JButton("Admin");
        btnDemoAdmin.setFont(Theme.FONT_SMALL);
        btnDemoAdmin.addActionListener(e -> {
            tfEmail.setText("admin@mentorbridge.edu");
            pfPassword.setText("admin123");
        });

        JButton btnDemoStudent = new JButton("Divya (Student)");
        btnDemoStudent.setFont(Theme.FONT_SMALL);
        btnDemoStudent.addActionListener(e -> {
            tfEmail.setText("divya.agarwal@student.edu");
            pfPassword.setText("student123");
        });

        JButton btnDemoMentor = new JButton("Dr. Khanna (Mentor)");
        btnDemoMentor.setFont(Theme.FONT_SMALL);
        btnDemoMentor.addActionListener(e -> {
            tfEmail.setText("rajesh.khanna@faculty.edu");
            pfPassword.setText("mentor123");
        });

        demoBar.add(btnDemoAdmin);
        demoBar.add(btnDemoStudent);
        demoBar.add(btnDemoMentor);

        gbc.gridy = row++;
        gbc.insets = new Insets(8, 6, 4, 6);
        card.add(demoBar, gbc);

        JButton btnRegister = new JButton("New user? Register as Student or Mentor");
        btnRegister.setFont(Theme.FONT_BODY);
        btnRegister.setForeground(Theme.BLUE_PRIMARY);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
            RegisterDialog reg = new RegisterDialog(this);
            reg.setVisible(true);
        });
        gbc.gridy = row++;
        card.add(btnRegister, gbc);

        center.add(card);
        root.add(center, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Student: Divya Agarwal • NSP Java Mini Project • 2026", SwingConstants.CENTER);
        footer.setFont(Theme.FONT_SMALL);
        footer.setForeground(Theme.TEXT_MUTED);
        footer.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(footer, BorderLayout.SOUTH);

        // Enter key triggers login
        getRootPane().setDefaultButton(btnLogin);

        setContentPane(root);
    }

    private void performLogin() {
        lblError.setText(" ");
        String email = tfEmail.getText().trim();
        String pass = new String(pfPassword.getPassword());

        try {
            User user = authService.login(email, pass);
            // Launch MainFrame
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
                dispose();
            });
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
        }
    }
}
