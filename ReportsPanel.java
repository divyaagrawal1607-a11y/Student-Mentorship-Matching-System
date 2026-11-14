package com.mentorbridge.ui;

import com.mentorbridge.models.User;
import com.mentorbridge.services.*;
import com.mentorbridge.utils.CsvExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Reports and Data Export Panel.
 */
public class ReportsPanel extends JPanel {

    private final DashboardService dashboardService = new DashboardService();
    private final StudentService studentService = new StudentService();
    private final MentorService mentorService = new MentorService();
    private final RequestService requestService = new RequestService();
    private final SessionService sessionService = new SessionService();
    private final FeedbackService feedbackService = new FeedbackService();
    private final User currentUser;

    private JTextArea taLog;

    public ReportsPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
    }

    private void initUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("System Reports & Data Exports");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 15, 15));
        center.setBackground(Theme.BG_PAGE);

        // Export Actions Card
        JPanel cardExport = UIHelper.createCardPanel();
        cardExport.setLayout(new BorderLayout(10, 10));

        JLabel lblSec = new JLabel("Quick Report Exports (CSV)");
        lblSec.setFont(Theme.FONT_SUBTITLE);
        lblSec.setForeground(Theme.NAVY_DARK);
        cardExport.add(lblSec, BorderLayout.NORTH);

        JPanel btnGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        btnGrid.setBackground(Theme.BG_CARD);

        JButton btnExportStudents = UIHelper.createPrimaryButton("👥 Export Students CSV");
        btnExportStudents.addActionListener(e -> exportStudents());

        JButton btnExportMentors = UIHelper.createPrimaryButton("🎓 Export Mentors CSV");
        btnExportMentors.addActionListener(e -> exportMentors());

        JButton btnExportRequests = UIHelper.createPrimaryButton("📋 Export Requests CSV");
        btnExportRequests.addActionListener(e -> exportRequests());

        JButton btnExportSessions = UIHelper.createPrimaryButton("📅 Export Sessions CSV");
        btnExportSessions.addActionListener(e -> exportSessions());

        JButton btnExportFeedback = UIHelper.createPrimaryButton("⭐ Export Feedback CSV");
        btnExportFeedback.addActionListener(e -> exportFeedback());

        JButton btnExportAll = UIHelper.createSuccessButton("📦 Export Complete Database Backup");
        btnExportAll.addActionListener(e -> exportAll());

        btnGrid.add(btnExportStudents);
        btnGrid.add(btnExportMentors);
        btnGrid.add(btnExportRequests);
        btnGrid.add(btnExportSessions);
        btnGrid.add(btnExportFeedback);
        btnGrid.add(btnExportAll);

        cardExport.add(btnGrid, BorderLayout.CENTER);
        center.add(cardExport);

        // System Log Card
        JPanel cardLog = UIHelper.createCardPanel();
        cardLog.setLayout(new BorderLayout(8, 8));

        JLabel lblLog = new JLabel("Audit & Export Activity Log");
        lblLog.setFont(Theme.FONT_SUBTITLE);
        lblLog.setForeground(Theme.NAVY_DARK);
        cardLog.add(lblLog, BorderLayout.NORTH);

        taLog = new JTextArea();
        taLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        taLog.setEditable(false);
        taLog.setBackground(new Color(248, 250, 252));
        taLog.setText("MentorBridge Data Export Engine initialized.\nReady to generate reports.\n");

        JScrollPane sp = new JScrollPane(taLog);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        cardLog.add(sp, BorderLayout.CENTER);

        center.add(cardLog);
        add(center, BorderLayout.CENTER);
    }

    private void log(String msg) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        taLog.append("[" + ts + "] " + msg + "\n");
    }

    private void exportStudents() {
        File f = new File("reports_students.csv");
        String[] cols = {"ID", "Name", "Email", "Department", "Year", "Skills", "Career Goal", "Phone"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        studentService.getAllStudents().forEach(s -> tm.addRow(new Object[]{
                s.getId(), s.getFullName(), s.getEmail(), s.getDepartment(), s.getStudyYear(), s.getSkillsInterests(), s.getCareerGoal(), s.getContactNumber()
        }));
        JTable t = new JTable(tm);
        if (CsvExporter.exportTableToCsv(t, f)) {
            log("Successfully exported " + tm.getRowCount() + " students to " + f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Exported: " + f.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportMentors() {
        File f = new File("reports_mentors.csv");
        String[] cols = {"ID", "Name", "Email", "Expertise", "Experience", "Availability", "Bio"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        mentorService.getAllMentors().forEach(m -> tm.addRow(new Object[]{
                m.getId(), m.getFullName(), m.getEmail(), m.getExpertiseSkills(), m.getExperienceYears(), m.getAvailability(), m.getBiography()
        }));
        JTable t = new JTable(tm);
        if (CsvExporter.exportTableToCsv(t, f)) {
            log("Successfully exported " + tm.getRowCount() + " mentors to " + f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Exported: " + f.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportRequests() {
        File f = new File("reports_requests.csv");
        String[] cols = {"ID", "Student", "Mentor", "Message", "Status", "Timestamp"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        requestService.getAllRequests().forEach(r -> tm.addRow(new Object[]{
                r.getId(), r.getStudentName(), r.getMentorName(), r.getRequestMessage(), r.getRequestStatus(), r.getRequestTimestamp()
        }));
        JTable t = new JTable(tm);
        if (CsvExporter.exportTableToCsv(t, f)) {
            log("Successfully exported " + tm.getRowCount() + " mentorship requests to " + f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Exported: " + f.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportSessions() {
        File f = new File("reports_sessions.csv");
        String[] cols = {"ID", "RequestID", "Student", "Mentor", "DateTime", "Mode", "Location", "Status", "Notes"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        sessionService.getAllSessions().forEach(s -> tm.addRow(new Object[]{
                s.getId(), s.getRequestId(), s.getStudentName(), s.getMentorName(), s.getSessionDateTime(), s.getMeetingMode(), s.getMeetingLinkLocation(), s.getSessionStatus(), s.getNotes()
        }));
        JTable t = new JTable(tm);
        if (CsvExporter.exportTableToCsv(t, f)) {
            log("Successfully exported " + tm.getRowCount() + " sessions to " + f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Exported: " + f.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportFeedback() {
        File f = new File("reports_feedback.csv");
        String[] cols = {"ID", "SessionID", "Student", "Mentor", "Rating", "Comments", "Timestamp"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        feedbackService.getAllFeedback().forEach(fb -> tm.addRow(new Object[]{
                fb.getId(), fb.getSessionId(), fb.getStudentName(), fb.getMentorName(), fb.getRating(), fb.getComments(), fb.getCreatedAt()
        }));
        JTable t = new JTable(tm);
        if (CsvExporter.exportTableToCsv(t, f)) {
            log("Successfully exported " + tm.getRowCount() + " feedback reviews to " + f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Exported: " + f.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportAll() {
        exportStudents();
        exportMentors();
        exportRequests();
        exportSessions();
        exportFeedback();
        log("Full database CSV package exported successfully.");
        JOptionPane.showMessageDialog(this, "Complete database CSV export generated successfully!", "Full Export Complete", JOptionPane.INFORMATION_MESSAGE);
    }
}
