package com.mentorbridge.ui;

import com.mentorbridge.models.Student;
import com.mentorbridge.services.StudentService;
import com.mentorbridge.utils.CsvExporter;
import com.mentorbridge.utils.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Management panel for Student CRUD operations.
 */
public class StudentManagementPanel extends JPanel {

    private final StudentService studentService = new StudentService();
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;

    public StudentManagementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadStudents();
    }

    private void initUI() {
        // Top Toolbar
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Student Management");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG_PAGE);

        tfSearch = UIHelper.createTextField(16);
        tfSearch.putClientProperty("JTextField.placeholderText", "Search name, skills, dept...");
        JButton btnSearch = UIHelper.createSecondaryButton("🔍 Search");
        btnSearch.addActionListener(e -> filterStudents());

        JButton btnAdd = UIHelper.createPrimaryButton("+ Add Student");
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

        // Center Table Card
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());

        String[] cols = {"ID", "Full Name", "Email", "Department", "Year", "Skills & Interests", "Career Goal", "Contact"};
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

    public void loadStudents() {
        model.setRowCount(0);
        List<Student> list = studentService.getAllStudents();
        for (Student s : list) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getFullName(),
                    s.getEmail(),
                    s.getDepartment(),
                    "Year " + s.getStudyYear(),
                    s.getSkillsInterests(),
                    s.getCareerGoal(),
                    s.getContactNumber()
            });
        }
    }

    private void filterStudents() {
        String q = tfSearch.getText().trim();
        model.setRowCount(0);
        List<Student> list = studentService.searchStudents(q);
        for (Student s : list) {
            model.addRow(new Object[]{
                    s.getId(),
                    s.getFullName(),
                    s.getEmail(),
                    s.getDepartment(),
                    "Year " + s.getStudyYear(),
                    s.getSkillsInterests(),
                    s.getCareerGoal(),
                    s.getContactNumber()
            });
        }
    }

    private void showAddDialog() {
        JTextField name = UIHelper.createTextField(20);
        JTextField email = UIHelper.createTextField(20);
        JTextField dept = UIHelper.createTextField(20);
        JTextField year = UIHelper.createTextField(5);
        year.setText("3");
        JTextField skills = UIHelper.createTextField(20);
        JTextField goal = UIHelper.createTextField(20);
        JTextField phone = UIHelper.createTextField(20);

        JPanel p = new JPanel(new GridLayout(7, 2, 8, 8));
        p.add(new JLabel("Full Name:")); p.add(name);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Department:")); p.add(dept);
        p.add(new JLabel("Year (1-6):")); p.add(year);
        p.add(new JLabel("Skills & Interests:")); p.add(skills);
        p.add(new JLabel("Career Goal:")); p.add(goal);
        p.add(new JLabel("Contact Phone:")); p.add(phone);

        int res = JOptionPane.showConfirmDialog(this, p, "Add New Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int y = Integer.parseInt(year.getText().trim());
                Student s = new Student();
                s.setFullName(name.getText().trim());
                s.setEmail(email.getText().trim());
                s.setDepartment(dept.getText().trim());
                s.setStudyYear(y);
                s.setSkillsInterests(skills.getText().trim());
                s.setCareerGoal(goal.getText().trim());
                s.setContactNumber(phone.getText().trim());

                studentService.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Student s = studentService.getStudentById(id);
        if (s == null) return;

        JTextField name = UIHelper.createTextField(20); name.setText(s.getFullName());
        JTextField email = UIHelper.createTextField(20); email.setText(s.getEmail());
        JTextField dept = UIHelper.createTextField(20); dept.setText(s.getDepartment());
        JTextField year = UIHelper.createTextField(5); year.setText(String.valueOf(s.getStudyYear()));
        JTextField skills = UIHelper.createTextField(20); skills.setText(s.getSkillsInterests());
        JTextField goal = UIHelper.createTextField(20); goal.setText(s.getCareerGoal());
        JTextField phone = UIHelper.createTextField(20); phone.setText(s.getContactNumber());

        JPanel p = new JPanel(new GridLayout(7, 2, 8, 8));
        p.add(new JLabel("Full Name:")); p.add(name);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Department:")); p.add(dept);
        p.add(new JLabel("Year (1-6):")); p.add(year);
        p.add(new JLabel("Skills & Interests:")); p.add(skills);
        p.add(new JLabel("Career Goal:")); p.add(goal);
        p.add(new JLabel("Contact Phone:")); p.add(phone);

        int res = JOptionPane.showConfirmDialog(this, p, "Edit Student #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int y = Integer.parseInt(year.getText().trim());
                s.setFullName(name.getText().trim());
                s.setEmail(email.getText().trim());
                s.setDepartment(dept.getText().trim());
                s.setStudyYear(y);
                s.setSkillsInterests(skills.getText().trim());
                s.setCareerGoal(goal.getText().trim());
                s.setContactNumber(phone.getText().trim());

                studentService.updateStudent(s);
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student: " + name + " (ID: #" + id + ")?\nAll associated requests and sessions will also be removed.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                studentService.deleteStudent(id);
                JOptionPane.showMessageDialog(this, "Student record deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("students_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean ok = CsvExporter.exportTableToCsv(table, fc.getSelectedFile());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Students exported to CSV successfully!", "Export Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error writing CSV file.", "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
