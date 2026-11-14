package com.mentorbridge.ui;

import com.mentorbridge.models.MatchResult;
import com.mentorbridge.models.Mentor;
import com.mentorbridge.models.Student;
import com.mentorbridge.models.User;
import com.mentorbridge.services.MatchingService;
import com.mentorbridge.services.RequestService;
import com.mentorbridge.services.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Intelligent Mentor Matching Screen.
 * Compares student skills, interests, and career goals against mentor expertise and availability.
 */
public class MentorMatchingPanel extends JPanel {

    private final MatchingService matchingService = new MatchingService();
    private final StudentService studentService = new StudentService();
    private final RequestService requestService = new RequestService();
    private final User currentUser;

    private JComboBox<StudentComboItem> cbStudents;
    private JLabel lblStudentDetails;
    private JTable matchTable;
    private DefaultTableModel matchTableModel;
    private List<MatchResult> currentMatches = new ArrayList<>();

    public MentorMatchingPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadStudents();
    }

    private void initUI() {
        // Top Header
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Intelligent Mentor Matching Engine");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);

        JLabel sub = new JLabel("Matches student interests and career aspirations against mentor expertise using normalized NLP overlap scoring.");
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_MUTED);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setBackground(Theme.BG_PAGE);
        titles.add(title);
        titles.add(sub);
        top.add(titles, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // Center Area
        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setBackground(Theme.BG_PAGE);

        // Selection & Filter Card
        JPanel filterCard = UIHelper.createCardPanel();
        filterCard.setLayout(new BorderLayout(10, 10));

        JPanel selectRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        selectRow.setBackground(Theme.BG_CARD);

        JLabel lblSelect = new JLabel("Select Student: ");
        lblSelect.setFont(Theme.FONT_BODY_BOLD);
        lblSelect.setForeground(Theme.NAVY_DARK);
        selectRow.add(lblSelect);

        cbStudents = new JComboBox<>();
        cbStudents.setFont(Theme.FONT_BODY);
        cbStudents.setPreferredSize(new Dimension(320, 32));
        cbStudents.addActionListener(e -> onStudentSelected());
        selectRow.add(cbStudents);

        JButton btnRunMatch = UIHelper.createPrimaryButton("⚡ Calculate Matches");
        btnRunMatch.addActionListener(e -> runMatching());
        selectRow.add(btnRunMatch);

        filterCard.add(selectRow, BorderLayout.NORTH);

        lblStudentDetails = new JLabel("Select a student above to inspect profile and matching parameters.");
        lblStudentDetails.setFont(Theme.FONT_BODY);
        lblStudentDetails.setForeground(Theme.TEXT_MUTED);
        lblStudentDetails.setBorder(new EmptyBorder(8, 5, 0, 0));
        filterCard.add(lblStudentDetails, BorderLayout.CENTER);

        center.add(filterCard, BorderLayout.NORTH);

        // Results Card
        JPanel resultsCard = UIHelper.createCardPanel();
        resultsCard.setLayout(new BorderLayout(10, 10));

        JPanel resHeader = new JPanel(new BorderLayout());
        resHeader.setBackground(Theme.BG_CARD);

        JLabel resTitle = new JLabel("Ranked Recommended Mentors");
        resTitle.setFont(Theme.FONT_SUBTITLE);
        resTitle.setForeground(Theme.NAVY_DARK);
        resHeader.add(resTitle, BorderLayout.WEST);

        JButton btnSendReq = UIHelper.createSuccessButton("✉️ Request Mentorship with Selected");
        btnSendReq.addActionListener(e -> submitRequestForSelected());
        resHeader.add(btnSendReq, BorderLayout.EAST);

        resultsCard.add(resHeader, BorderLayout.NORTH);

        String[] cols = {"Rank", "Match Score", "Mentor Name", "Key Matched Skills", "Experience", "Availability", "Recommendation Analysis"};
        matchTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        matchTable = new JTable(matchTableModel);
        UIHelper.styleTable(matchTable);
        matchTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        matchTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        matchTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        matchTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        matchTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        matchTable.getColumnModel().getColumn(5).setPreferredWidth(140);
        matchTable.getColumnModel().getColumn(6).setPreferredWidth(350);

        JScrollPane sp = new JScrollPane(matchTable);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        resultsCard.add(sp, BorderLayout.CENTER);

        // Disclaimer Note
        JLabel disclaimer = new JLabel("Note: The compatibility match score is an algorithmic recommendation based on skill and goal overlap, not an absolute guarantee.");
        disclaimer.setFont(Theme.FONT_SMALL);
        disclaimer.setForeground(Theme.TEXT_MUTED);
        disclaimer.setBorder(new EmptyBorder(5, 0, 0, 0));
        resultsCard.add(disclaimer, BorderLayout.SOUTH);

        center.add(resultsCard, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    public void loadStudents() {
        cbStudents.removeAllItems();
        List<Student> students = studentService.getAllStudents();
        for (Student s : students) {
            cbStudents.addItem(new StudentComboItem(s));
        }

        // Auto-select logged in student if applicable
        if (currentUser != null && currentUser.isStudent()) {
            for (int i = 0; i < cbStudents.getItemCount(); i++) {
                StudentComboItem item = cbStudents.getItemAt(i);
                if (item.student.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                    cbStudents.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (cbStudents.getItemCount() > 0) {
            onStudentSelected();
            runMatching();
        }
    }

    private void onStudentSelected() {
        StudentComboItem item = (StudentComboItem) cbStudents.getSelectedItem();
        if (item != null) {
            Student s = item.student;
            lblStudentDetails.setText(String.format("<html><b>Department:</b> %s (Year %d) &nbsp;|&nbsp; <b>Skills:</b> %s &nbsp;|&nbsp; <b>Career Goal:</b> %s</html>",
                    s.getDepartment(), s.getStudyYear(), s.getSkillsInterests(), s.getCareerGoal()));
        }
    }

    private void runMatching() {
        StudentComboItem item = (StudentComboItem) cbStudents.getSelectedItem();
        if (item == null) return;

        matchTableModel.setRowCount(0);
        currentMatches = matchingService.findMatches(item.student);

        int rank = 1;
        for (MatchResult mr : currentMatches) {
            Mentor m = mr.getMentor();
            String matchedStr = mr.getMatchedSkills().isEmpty() ? "Domain synergy" : String.join(", ", mr.getMatchedSkills());
            matchTableModel.addRow(new Object[]{
                    "#" + rank++,
                    String.format("%.1f%% Match", mr.getMatchPercentage()),
                    m.getFullName(),
                    matchedStr,
                    m.getExperienceYears() + " yrs",
                    m.getAvailability(),
                    mr.getExplanation()
            });
        }
    }

    private void submitRequestForSelected() {
        int row = matchTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please click on a recommended mentor from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StudentComboItem item = (StudentComboItem) cbStudents.getSelectedItem();
        if (item == null) return;

        MatchResult mr = currentMatches.get(row);
        Mentor selectedMentor = mr.getMentor();

        String message = JOptionPane.showInputDialog(this,
                "Enter an introductory mentorship request message for " + selectedMentor.getFullName() + ":",
                "Hello " + selectedMentor.getFullName() + ", I would appreciate your mentorship on " + item.student.getCareerGoal() + ".");

        if (message != null && !message.trim().isEmpty()) {
            try {
                requestService.createRequest(item.student.getId(), selectedMentor.getId(), message.trim());
                JOptionPane.showMessageDialog(this,
                        "Mentorship request submitted successfully!\nThe mentor will be notified for review.",
                        "Request Sent", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to submit request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class StudentComboItem {
        final Student student;
        StudentComboItem(Student student) { this.student = student; }
        public String toString() { return student.getFullName() + " (" + student.getDepartment() + ")"; }
    }
}
