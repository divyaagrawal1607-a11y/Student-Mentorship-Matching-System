package com.mentorbridge.ui;

import com.mentorbridge.models.User;
import com.mentorbridge.services.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Registration Dialog for new Students and Mentors.
 */
public class RegisterDialog extends JDialog {

    private final AuthService authService = new AuthService();
    private boolean registered = false;

    // Student fields
    private JTextField tfStudentName, tfStudentEmail, tfStudentDept, tfStudentYear, tfStudentSkills, tfStudentGoal, tfStudentPhone;
    private JPasswordField pfStudentPass;

    // Mentor fields
    private JTextField tfMentorName, tfMentorEmail, tfMentorExpertise, tfMentorExp, tfMentorAvail;
    private JTextArea taMentorBio;
    private JPasswordField pfMentorPass;

    public RegisterDialog(JFrame parent) {
        super(parent, "Register New Account - MentorBridge", true);
        setSize(560, 680);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(Theme.BG_PAGE);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Create Your MentorBridge Account");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Theme.NAVY_DARK);
        content.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BODY_BOLD);
        tabs.addTab("Register as Student", createStudentPanel());
        tabs.addTab("Register as Mentor", createMentorPanel());
        content.add(tabs, BorderLayout.CENTER);

        add(content);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG_CARD);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        tfStudentName = UIHelper.createTextField(20);
        tfStudentEmail = UIHelper.createTextField(20);
        pfStudentPass = UIHelper.createPasswordField(20);
        tfStudentDept = UIHelper.createTextField(20);
        tfStudentYear = UIHelper.createTextField(5);
        tfStudentYear.setText("3");
        tfStudentSkills = UIHelper.createTextField(20);
        tfStudentGoal = UIHelper.createTextField(20);
        tfStudentPhone = UIHelper.createTextField(20);

        int row = 0;
        addFormRow(panel, gbc, row++, "Full Name:", tfStudentName);
        addFormRow(panel, gbc, row++, "Email Address:", tfStudentEmail);
        addFormRow(panel, gbc, row++, "Password (min 6 chars):", pfStudentPass);
        addFormRow(panel, gbc, row++, "Department / Major:", tfStudentDept);
        addFormRow(panel, gbc, row++, "Study Year (1-6):", tfStudentYear);
        addFormRow(panel, gbc, row++, "Skills & Interests (comma-sep):", tfStudentSkills);
        addFormRow(panel, gbc, row++, "Career Goal:", tfStudentGoal);
        addFormRow(panel, gbc, row++, "Contact Phone:", tfStudentPhone);

        JButton btnSubmit = UIHelper.createPrimaryButton("Register as Student");
        btnSubmit.addActionListener(e -> registerStudent());
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(btnSubmit, gbc);

        return panel;
    }

    private JPanel createMentorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG_CARD);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        tfMentorName = UIHelper.createTextField(20);
        tfMentorEmail = UIHelper.createTextField(20);
        pfMentorPass = UIHelper.createPasswordField(20);
        tfMentorExpertise = UIHelper.createTextField(20);
        tfMentorExp = UIHelper.createTextField(5);
        tfMentorExp.setText("5");
        tfMentorAvail = UIHelper.createTextField(20);
        taMentorBio = new JTextArea(3, 20);
        taMentorBio.setFont(Theme.FONT_BODY);
        taMentorBio.setLineWrap(true);
        taMentorBio.setWrapStyleWord(true);
        JScrollPane spBio = new JScrollPane(taMentorBio);

        int row = 0;
        addFormRow(panel, gbc, row++, "Full Name:", tfMentorName);
        addFormRow(panel, gbc, row++, "Email Address:", tfMentorEmail);
        addFormRow(panel, gbc, row++, "Password (min 6 chars):", pfMentorPass);
        addFormRow(panel, gbc, row++, "Expertise & Skills:", tfMentorExpertise);
        addFormRow(panel, gbc, row++, "Experience (Years):", tfMentorExp);
        addFormRow(panel, gbc, row++, "Availability (e.g. Weekends):", tfMentorAvail);
        addFormRow(panel, gbc, row++, "Brief Biography:", spBio);

        JButton btnSubmit = UIHelper.createPrimaryButton("Register as Mentor");
        btnSubmit.addActionListener(e -> registerMentor());
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(btnSubmit, gbc);

        return panel;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        p.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(comp, gbc);
    }

    private void registerStudent() {
        try {
            int year = Integer.parseInt(tfStudentYear.getText().trim());
            User u = authService.registerStudent(
                    tfStudentName.getText().trim(),
                    tfStudentEmail.getText().trim(),
                    new String(pfStudentPass.getPassword()),
                    tfStudentDept.getText().trim(),
                    year,
                    tfStudentSkills.getText().trim(),
                    tfStudentGoal.getText().trim(),
                    tfStudentPhone.getText().trim()
            );
            JOptionPane.showMessageDialog(this,
                    "Student registration successful! You can now log in.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            registered = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Study year must be an integer (1-6).", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerMentor() {
        try {
            int exp = Integer.parseInt(tfMentorExp.getText().trim());
            User u = authService.registerMentor(
                    tfMentorName.getText().trim(),
                    tfMentorEmail.getText().trim(),
                    new String(pfMentorPass.getPassword()),
                    tfMentorExpertise.getText().trim(),
                    exp,
                    tfMentorAvail.getText().trim(),
                    taMentorBio.getText().trim()
            );
            JOptionPane.showMessageDialog(this,
                    "Mentor registration successful! You can now log in.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            registered = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Experience must be a valid number of years.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isRegistered() {
        return registered;
    }
}
