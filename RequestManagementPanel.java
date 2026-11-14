package com.mentorbridge.ui;

import com.mentorbridge.models.MentorshipRequest;
import com.mentorbridge.models.User;
import com.mentorbridge.services.RequestService;
import com.mentorbridge.utils.DateTimeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel to manage and review mentorship requests.
 */
public class RequestManagementPanel extends JPanel {

    private final RequestService requestService = new RequestService();
    private final User currentUser;
    private final MainFrame mainFrame;

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbFilterStatus;
    private List<MentorshipRequest> allRequests = new ArrayList<>();

    public RequestManagementPanel(User currentUser, MainFrame mainFrame) {
        this.currentUser = currentUser;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadRequests();
    }

    private void initUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Mentorship Requests");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG_PAGE);

        JLabel lblFilter = new JLabel("Status Filter: ");
        lblFilter.setFont(Theme.FONT_BODY_BOLD);
        actions.add(lblFilter);

        cbFilterStatus = new JComboBox<>(new String[]{"ALL", "PENDING", "ACCEPTED", "REJECTED", "CANCELLED"});
        cbFilterStatus.setFont(Theme.FONT_BODY);
        cbFilterStatus.addActionListener(e -> applyFilter());
        actions.add(cbFilterStatus);

        JButton btnAccept = UIHelper.createSuccessButton("✓ Accept Request");
        btnAccept.addActionListener(e -> updateStatus("ACCEPTED"));

        JButton btnReject = UIHelper.createDangerButton("✕ Reject Request");
        btnReject.addActionListener(e -> updateStatus("REJECTED"));

        JButton btnSchedule = UIHelper.createPrimaryButton("📅 Schedule Meeting");
        btnSchedule.addActionListener(e -> scheduleSessionForSelected());

        JButton btnRefresh = UIHelper.createSecondaryButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> loadRequests());

        actions.add(btnAccept);
        actions.add(btnReject);
        actions.add(btnSchedule);
        actions.add(btnRefresh);

        top.add(actions, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());

        String[] cols = {"ID", "Student Name", "Student Email", "Mentor Name", "Mentor Email", "Message", "Status", "Requested At"};
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

    public void loadRequests() {
        allRequests = requestService.getAllRequests();
        applyFilter();
    }

    private void applyFilter() {
        String filter = (String) cbFilterStatus.getSelectedItem();
        model.setRowCount(0);

        for (MentorshipRequest r : allRequests) {
            if ("ALL".equalsIgnoreCase(filter) || r.getRequestStatus().equalsIgnoreCase(filter)) {
                model.addRow(new Object[]{
                        r.getId(),
                        r.getStudentName(),
                        r.getStudentEmail(),
                        r.getMentorName(),
                        r.getMentorEmail(),
                        r.getRequestMessage(),
                        r.getRequestStatus(),
                        DateTimeUtil.format(r.getRequestTimestamp())
                });
            }
        }
    }

    private void updateStatus(String newStatus) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String currentStatus = (String) model.getValueAt(row, 6);

        if ("CANCELLED".equalsIgnoreCase(currentStatus) || "REJECTED".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Cannot modify a request that is already " + currentStatus + ".", "Invalid Transition", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if ("ACCEPTED".equalsIgnoreCase(newStatus)) {
                requestService.acceptRequest(id);
                JOptionPane.showMessageDialog(this, "Request marked as ACCEPTED.\nYou can now schedule mentorship sessions.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                requestService.rejectRequest(id);
                JOptionPane.showMessageDialog(this, "Request marked as REJECTED.", "Updated", JOptionPane.INFORMATION_MESSAGE);
            }
            loadRequests();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void scheduleSessionForSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an ACCEPTED request to schedule a session.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String currentStatus = (String) model.getValueAt(row, 6);

        if (!"ACCEPTED".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Only ACCEPTED requests can have mentorship meetings scheduled.", "Status Restriction", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mainFrame.showSessionSchedulerForRequest(id);
    }
}
