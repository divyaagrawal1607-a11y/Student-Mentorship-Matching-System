package com.mentorbridge.ui;

import com.mentorbridge.models.Mentor;
import com.mentorbridge.services.MentorService;
import com.mentorbridge.utils.CsvExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Management panel for Mentor CRUD operations.
 */
public class MentorManagementPanel extends JPanel {

    private final MentorService mentorService = new MentorService();
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;

    public MentorManagementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadMentors();
    }

    private void initUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Mentor Management");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG_PAGE);

        tfSearch = UIHelper.createTextField(16);
        tfSearch.putClientProperty("JTextField.placeholderText", "Search expertise, name...");
        JButton btnSearch = UIHelper.createSecondaryButton("🔍 Search");
        btnSearch.addActionListener(e -> filterMentors());

        JButton btnAdd = UIHelper.createPrimaryButton("+ Add Mentor");
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnEdit = UIHelper.createSecondaryButton("✏️ Edit");
        btnEdit.addActionListener(e -> showEditDialog());

        JButton btnDelete = UIHelper.createDangerButton("🗑️ Delete");
        btnDelete.addActionListener(e -> deleteSelected());

        JButton btnExport = UIHelper.createSecondaryButton("📥 Export CSV");
        btnExport.addActionListener(e -> exportCsv());

        actions.add(tfSearch);
        actions.add(btnSearch);
        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnExport);

        top.add(actions, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());

        String[] cols = {"ID", "Full Name", "Email", "Expertise / Skills", "Experience", "Availability", "Biography"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIHelper.styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        card.add(sp, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    public void loadMentors() {
        model.setRowCount(0);
        List<Mentor> list = mentorService.getAllMentors();
        for (Mentor m : list) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getFullName(),
                    m.getEmail(),
                    m.getExpertiseSkills(),
                    m.getExperienceYears() + " yrs",
                    m.getAvailability(),
                    m.getBiography()
            });
        }
    }

    private void filterMentors() {
        String q = tfSearch.getText().trim();
        model.setRowCount(0);
        List<Mentor> list = mentorService.searchMentors(q);
        for (Mentor m : list) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getFullName(),
                    m.getEmail(),
                    m.getExpertiseSkills(),
                    m.getExperienceYears() + " yrs",
                    m.getAvailability(),
                    m.getBiography()
            });
        }
    }

    private void showAddDialog() {
        JTextField name = UIHelper.createTextField(20);
        JTextField email = UIHelper.createTextField(20);
        JTextField expertise = UIHelper.createTextField(20);
        JTextField exp = UIHelper.createTextField(5); exp.setText("5");
        JTextField avail = UIHelper.createTextField(20);
        JTextArea bio = new JTextArea(3, 20);
        bio.setLineWrap(true); bio.setWrapStyleWord(true);

        JPanel p = new JPanel(new GridLayout(6, 2, 8, 8));
        p.add(new JLabel("Full Name:")); p.add(name);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Expertise & Skills:")); p.add(expertise);
        p.add(new JLabel("Experience (Years):")); p.add(exp);
        p.add(new JLabel("Availability:")); p.add(avail);
        p.add(new JLabel("Biography:")); p.add(new JScrollPane(bio));

        int res = JOptionPane.showConfirmDialog(this, p, "Add New Mentor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int y = Integer.parseInt(exp.getText().trim());
                Mentor m = new Mentor();
                m.setFullName(name.getText().trim());
                m.setEmail(email.getText().trim());
                m.setExpertiseSkills(expertise.getText().trim());
                m.setExperienceYears(y);
                m.setAvailability(avail.getText().trim());
                m.setBiography(bio.getText().trim());

                mentorService.addMentor(m);
                JOptionPane.showMessageDialog(this, "Mentor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMentors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a mentor to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Mentor m = mentorService.getMentorById(id);
        if (m == null) return;

        JTextField name = UIHelper.createTextField(20); name.setText(m.getFullName());
        JTextField email = UIHelper.createTextField(20); email.setText(m.getEmail());
        JTextField expertise = UIHelper.createTextField(20); expertise.setText(m.getExpertiseSkills());
        JTextField exp = UIHelper.createTextField(5); exp.setText(String.valueOf(m.getExperienceYears()));
        JTextField avail = UIHelper.createTextField(20); avail.setText(m.getAvailability());
        JTextArea bio = new JTextArea(3, 20);
        bio.setText(m.getBiography()); bio.setLineWrap(true); bio.setWrapStyleWord(true);

        JPanel p = new JPanel(new GridLayout(6, 2, 8, 8));
        p.add(new JLabel("Full Name:")); p.add(name);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Expertise & Skills:")); p.add(expertise);
        p.add(new JLabel("Experience (Years):")); p.add(exp);
        p.add(new JLabel("Availability:")); p.add(avail);
        p.add(new JLabel("Biography:")); p.add(new JScrollPane(bio));

        int res = JOptionPane.showConfirmDialog(this, p, "Edit Mentor #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int y = Integer.parseInt(exp.getText().trim());
                m.setFullName(name.getText().trim());
                m.setEmail(email.getText().trim());
                m.setExpertiseSkills(expertise.getText().trim());
                m.setExperienceYears(y);
                m.setAvailability(avail.getText().trim());
                m.setBiography(bio.getText().trim());

                mentorService.updateMentor(m);
                JOptionPane.showMessageDialog(this, "Mentor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMentors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a mentor to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete mentor: " + name + " (ID: #" + id + ")?\nAll associated requests will also be removed.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                mentorService.deleteMentor(id);
                JOptionPane.showMessageDialog(this, "Mentor record deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                loadMentors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete mentor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("mentors_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean ok = CsvExporter.exportTableToCsv(table, fc.getSelectedFile());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Mentors exported to CSV successfully!", "Export Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error writing CSV file.", "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
