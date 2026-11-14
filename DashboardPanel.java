package com.mentorbridge.ui;

import com.mentorbridge.models.MentorshipRequest;
import com.mentorbridge.models.User;
import com.mentorbridge.services.DashboardService;
import com.mentorbridge.services.RequestService;
import com.mentorbridge.utils.DateTimeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Modern Dashboard Panel displaying live metrics, quick actions, and recent activity.
 */
public class DashboardPanel extends JPanel {

    private final DashboardService dashboardService = new DashboardService();
    private final RequestService requestService = new RequestService();
    private final User currentUser;
    private final MainFrame mainFrame;

    private JPanel statsContainer;
    private JTable activityTable;
    private DefaultTableModel tableModel;

    public DashboardPanel(User currentUser, MainFrame mainFrame) {
        this.currentUser = currentUser;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        refreshData();
    }

    private void initUI() {
        // Top Header
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_PAGE);

        JLabel welcome = new JLabel("Welcome back, " + currentUser.getName() + " 👋");
        welcome.setFont(Theme.FONT_TITLE);
        welcome.setForeground(Theme.NAVY_DARK);

        JLabel sub = new JLabel("Live overview of the Student Mentorship Matching System");
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_MUTED);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setBackground(Theme.BG_PAGE);
        titles.add(welcome);
        titles.add(sub);
        top.add(titles, BorderLayout.WEST);

        JButton btnRefresh = UIHelper.createSecondaryButton("🔄 Refresh Stats");
        btnRefresh.addActionListener(e -> refreshData());
        top.add(btnRefresh, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Center Content
        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setBackground(Theme.BG_PAGE);

        // Stats Cards Row
        statsContainer = new JPanel(new GridLayout(1, 5, 12, 12));
        statsContainer.setBackground(Theme.BG_PAGE);
        statsContainer.setPreferredSize(new Dimension(0, 95));
        center.add(statsContainer, BorderLayout.NORTH);

        // Quick Actions & Recent Activity
        JPanel body = new JPanel(new BorderLayout(15, 15));
        body.setBackground(Theme.BG_PAGE);

        // Quick Actions Bar
        JPanel actionsCard = UIHelper.createCardPanel();
        actionsCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JLabel lblActions = new JLabel("Quick Actions: ");
        lblActions.setFont(Theme.FONT_BODY_BOLD);
        lblActions.setForeground(Theme.NAVY_DARK);
        actionsCard.add(lblActions);

        JButton btnGoMatch = UIHelper.createPrimaryButton("✨ Find Mentor Matches");
        btnGoMatch.addActionListener(e -> mainFrame.showScreen("MATCHING"));
        actionsCard.add(btnGoMatch);

        JButton btnGoRequests = UIHelper.createSecondaryButton("📋 View Requests");
        btnGoRequests.addActionListener(e -> mainFrame.showScreen("REQUESTS"));
        actionsCard.add(btnGoRequests);

        JButton btnGoSessions = UIHelper.createSecondaryButton("📅 View Sessions");
        btnGoSessions.addActionListener(e -> mainFrame.showScreen("SESSIONS"));
        actionsCard.add(btnGoSessions);

        body.add(actionsCard, BorderLayout.NORTH);

        // Recent Requests Table Card
        JPanel tableCard = UIHelper.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JLabel tblTitle = new JLabel("Recent Mentorship Requests");
        tblTitle.setFont(Theme.FONT_SUBTITLE);
        tblTitle.setForeground(Theme.NAVY_DARK);
        tableCard.add(tblTitle, BorderLayout.NORTH);

        String[] cols = {"Req ID", "Student Name", "Mentor Name", "Message", "Status", "Requested At"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        activityTable = new JTable(tableModel);
        UIHelper.styleTable(activityTable);

        JScrollPane sp = new JScrollPane(activityTable);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        tableCard.add(sp, BorderLayout.CENTER);

        body.add(tableCard, BorderLayout.CENTER);
        center.add(body, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    public void refreshData() {
        Map<String, Integer> stats = dashboardService.getStatistics();
        statsContainer.removeAll();

        statsContainer.add(UIHelper.createStatCard("Total Students", String.valueOf(stats.getOrDefault("totalStudents", 0)), Theme.BLUE_PRIMARY));
        statsContainer.add(UIHelper.createStatCard("Total Mentors", String.valueOf(stats.getOrDefault("totalMentors", 0)), Theme.INFO));
        statsContainer.add(UIHelper.createStatCard("Pending Requests", String.valueOf(stats.getOrDefault("pendingRequests", 0)), Theme.WARNING));
        statsContainer.add(UIHelper.createStatCard("Accepted Requests", String.valueOf(stats.getOrDefault("acceptedRequests", 0)), Theme.SUCCESS));
        statsContainer.add(UIHelper.createStatCard("Completed Sessions", String.valueOf(stats.getOrDefault("completedSessions", 0)), new Color(139, 92, 246)));

        statsContainer.revalidate();
        statsContainer.repaint();

        // Update table
        tableModel.setRowCount(0);
        List<MentorshipRequest> reqs = requestService.getAllRequests();
        int max = Math.min(reqs.size(), 10);
        for (int i = 0; i < max; i++) {
            MentorshipRequest r = reqs.get(i);
            tableModel.addRow(new Object[]{
                    "#" + r.getId(),
                    r.getStudentName(),
                    r.getMentorName(),
                    r.getRequestMessage(),
                    r.getRequestStatus(),
                    DateTimeUtil.format(r.getRequestTimestamp())
            });
        }
    }
}
