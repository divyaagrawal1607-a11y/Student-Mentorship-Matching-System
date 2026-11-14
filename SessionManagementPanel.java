package com.mentorbridge.ui;

import com.mentorbridge.models.MentorshipRequest;
import com.mentorbridge.models.MentorshipSession;
import com.mentorbridge.models.User;
import com.mentorbridge.services.RequestService;
import com.mentorbridge.services.SessionService;
import com.mentorbridge.utils.DateTimeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Panel to schedule, view, and manage mentorship meeting sessions.
 */
public class SessionManagementPanel extends JPanel {

    private final SessionService sessionService = new SessionService();
    private final RequestService requestService = new RequestService();
    private final User currentUser;
    private final MainFrame mainFrame;

    private JTable table;
    private DefaultTableModel model;
    private List<MentorshipSession> sessionList;

    public SessionManagementPanel(User currentUser, MainFrame mainFrame) {
        this.currentUser = currentUser;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadSessions();
    }

    private void initUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Mentorship Sessions");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG_PAGE);

        JButton btnSchedule = UIHelper.createPrimaryButton("+ Schedule New Session");
        btnSchedule.addActionListener(e -> showScheduleDialog(0));

        JButton btnComplete = UIHelper.createSuccessButton("✓ Mark Completed");
        btnComplete.addActionListener(e -> markCompleted());

        JButton btnCancel = UIHelper.createDangerButton("✕ Cancel Session");
        btnCancel.addActionListener(e -> cancelSelected());

        JButton btnFeedback = UIHelper.createSecondaryButton("⭐ Leave Feedback");
        btnFeedback.addActionListener(e -> openFeedbackForSelected());

        JButton btnRefresh = UIHelper.createSecondaryButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> loadSessions());

        actions.add(btnSchedule);
        actions.add(btnComplete);
        actions.add(btnCancel);
        actions.add(btnFeedback);
        actions.add(btnRefresh);

        top.add(actions, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());

        String[] cols = {"Session ID", "Request ID", "Student Name", "Mentor Name", "Date & Time", "Mode", "Link / Location", "Status", "Feedback", "Notes"};
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

    public void loadSessions() {
        model.setRowCount(0);
        sessionList = sessionService.getAllSessions();
        for (MentorshipSession s : sessionList) {
            model.addRow(new Object[]{
                    "#" + s.getId(),
                    "#" + s.getRequestId(),
                    s.getStudentName(),
                    s.getMentorName(),
                    DateTimeUtil.format(s.getSessionDateTime()),
                    s.getMeetingMode(),
                    s.getMeetingLinkLocation(),
                    s.getSessionStatus(),
                    s.isFeedbackSubmitted() ? "✓ Submitted" : "Pending",
                    s.getNotes()
            });
        }
    }

    public void showScheduleDialog(int preselectedRequestId) {
        List<MentorshipRequest> reqs = requestService.getAllRequests();
        List<MentorshipRequest> accepted = reqs.stream()
                .filter(r -> "ACCEPTED".equalsIgnoreCase(r.getRequestStatus()))
                .toList();

        if (accepted.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No accepted mentorship requests available. Please accept a request first before scheduling.",
                    "No Eligible Requests", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<RequestComboItem> cb = new JComboBox<>();
        int selectedIndex = 0;
        for (int i = 0; i < accepted.size(); i++) {
            MentorshipRequest r = accepted.get(i);
            cb.addItem(new RequestComboItem(r));
            if (r.getId() == preselectedRequestId) {
                selectedIndex = i;
            }
        }
        cb.setSelectedIndex(selectedIndex);

        JTextField tfDate = UIHelper.createTextField(20);
        tfDate.setText(DateTimeUtil.format(LocalDateTime.now().plusDays(2).withHour(16).withMinute(0)));

        JComboBox<String> cbMode = new JComboBox<>(new String[]{"ONLINE", "IN_PERSON", "HYBRID"});
        JTextField tfLocation = UIHelper.createTextField(20);
        tfLocation.setText("https://meet.google.com/mentor-bridge-session");

        JTextField tfNotes = UIHelper.createTextField(20);
        tfNotes.setText("Initial mentorship milestone and roadmap discussion.");

        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("Accepted Request:")); p.add(cb);
        p.add(new JLabel("Date & Time (yyyy-MM-dd HH:mm):")); p.add(tfDate);
        p.add(new JLabel("Meeting Mode:")); p.add(cbMode);
        p.add(new JLabel("Meeting Link / Location:")); p.add(tfLocation);
        p.add(new JLabel("Agenda / Notes:")); p.add(tfNotes);

        int res = JOptionPane.showConfirmDialog(this, p, "Schedule Mentorship Session", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                RequestComboItem selectedReq = (RequestComboItem) cb.getSelectedItem();
                LocalDateTime dt = DateTimeUtil.parse(tfDate.getText().trim());
                if (dt == null) throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd HH:mm'");

                sessionService.scheduleSession(
                        selectedReq.req.getId(),
                        dt,
                        (String) cbMode.getSelectedItem(),
                        tfLocation.getText().trim(),
                        tfNotes.getText().trim()
                );

                JOptionPane.showMessageDialog(this, "Mentorship session scheduled successfully!", "Session Scheduled", JOptionPane.INFORMATION_MESSAGE);
                loadSessions();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Scheduling Session", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void markCompleted() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a session first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MentorshipSession s = sessionList.get(row);
        if ("COMPLETED".equalsIgnoreCase(s.getSessionStatus())) {
            JOptionPane.showMessageDialog(this, "This session is already marked as COMPLETED.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            sessionService.completeSession(s.getId());
            JOptionPane.showMessageDialog(this, "Session marked as COMPLETED!\nStudents can now submit session feedback.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSessions();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a session to cancel.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MentorshipSession s = sessionList.get(row);
        int conf = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel session #" + s.getId() + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                sessionService.cancelSession(s.getId());
                JOptionPane.showMessageDialog(this, "Session cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                loadSessions();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error cancelling session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openFeedbackForSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a session to submit feedback for.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MentorshipSession s = sessionList.get(row);
        if (!"COMPLETED".equalsIgnoreCase(s.getSessionStatus())) {
            JOptionPane.showMessageDialog(this, "Feedback can only be submitted after a session has been marked as COMPLETED.", "Session Not Completed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (s.isFeedbackSubmitted()) {
            JOptionPane.showMessageDialog(this, "Feedback has already been submitted for this session.", "Feedback Exists", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mainFrame.showFeedbackDialogForSession(s);
    }

    private static class RequestComboItem {
        final MentorshipRequest req;
        RequestComboItem(MentorshipRequest req) { this.req = req; }
        public String toString() {
            return "#" + req.getId() + " - " + req.getStudentName() + " with " + req.getMentorName();
        }
    }
}
