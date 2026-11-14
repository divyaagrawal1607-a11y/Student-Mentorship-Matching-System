package com.mentorbridge.ui;

import com.mentorbridge.models.MentorshipSession;
import com.mentorbridge.models.User;
import com.mentorbridge.services.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Application Window with Navy & Blue Modern Styling and Responsive Sidebar.
 */
public class MainFrame extends JFrame {

    private final User currentUser;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerPanel = new JPanel(cardLayout);

    private final Map<String, JButton> navButtons = new HashMap<>();

    private DashboardPanel dashboardPanel;
    private StudentManagementPanel studentPanel;
    private MentorManagementPanel mentorPanel;
    private MentorMatchingPanel matchingPanel;
    private RequestManagementPanel requestPanel;
    private SessionManagementPanel sessionPanel;
    private FeedbackPanel feedbackPanel;
    private ReportsPanel reportsPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("MentorBridge - Student Mentorship Matching System | " + user.getName() + " (" + user.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 780);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_PAGE);

        // 1. Top Bar
        JPanel topBar = createTopBar();
        root.add(topBar, BorderLayout.NORTH);

        // 2. Left Sidebar
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // 3. Center Screen Panels (CardLayout)
        dashboardPanel = new DashboardPanel(currentUser, this);
        studentPanel = new StudentManagementPanel();
        mentorPanel = new MentorManagementPanel();
        matchingPanel = new MentorMatchingPanel(currentUser);
        requestPanel = new RequestManagementPanel(currentUser, this);
        sessionPanel = new SessionManagementPanel(currentUser, this);
        feedbackPanel = new FeedbackPanel(currentUser);
        reportsPanel = new ReportsPanel(currentUser);

        centerPanel.add(dashboardPanel, "DASHBOARD");
        centerPanel.add(studentPanel, "STUDENTS");
        centerPanel.add(mentorPanel, "MENTORS");
        centerPanel.add(matchingPanel, "MATCHING");
        centerPanel.add(requestPanel, "REQUESTS");
        centerPanel.add(sessionPanel, "SESSIONS");
        centerPanel.add(feedbackPanel, "FEEDBACK");
        centerPanel.add(reportsPanel, "REPORTS");

        root.add(centerPanel, BorderLayout.CENTER);

        setContentPane(root);
        showScreen("DASHBOARD");
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.NAVY_DARK);
        bar.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setBackground(Theme.NAVY_DARK);

        JLabel logo = new JLabel("🎓 MentorBridge");
        logo.setFont(Theme.FONT_SUBTITLE);
        logo.setForeground(Color.WHITE);
        brand.add(logo);

        JLabel subtitle = new JLabel("| NSP Java Mini Project • Divya Agarwal");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(new Color(148, 163, 184));
        brand.add(subtitle);

        bar.add(brand, BorderLayout.WEST);

        // User info & Logout
        JPanel userBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        userBox.setBackground(Theme.NAVY_DARK);

        JLabel userLbl = new JLabel(currentUser.getName());
        userLbl.setFont(Theme.FONT_BODY_BOLD);
        userLbl.setForeground(Color.WHITE);
        userBox.add(userLbl);

        JLabel roleBadge = UIHelper.createBadge(currentUser.getRole(), Theme.BLUE_PRIMARY, Theme.BLUE_LIGHT);
        userBox.add(roleBadge);

        JButton btnLogout = new JButton("Sign Out");
        btnLogout.setFont(Theme.FONT_SMALL);
        btnLogout.setForeground(new Color(248, 113, 113));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());
        userBox.add(btnLogout);

        bar.add(userBox, BorderLayout.EAST);
        return bar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(Theme.NAVY_MEDIUM);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setBackground(Theme.NAVY_MEDIUM);
        navList.setBorder(new EmptyBorder(15, 12, 15, 12));

        addNavButton(navList, "DASHBOARD", "📊 Dashboard");
        addNavButton(navList, "MATCHING", "⚡ Mentor Matching");
        addNavButton(navList, "STUDENTS", "👥 Students");
        addNavButton(navList, "MENTORS", "🎓 Mentors");
        addNavButton(navList, "REQUESTS", "📋 Requests");
        addNavButton(navList, "SESSIONS", "📅 Sessions");
        addNavButton(navList, "FEEDBACK", "⭐ Feedback");
        addNavButton(navList, "REPORTS", "📥 Reports & CSV");

        sidebar.add(navList, BorderLayout.NORTH);

        // Sidebar Footer
        JPanel foot = new JPanel(new GridLayout(2, 1));
        foot.setBackground(Theme.NAVY_MEDIUM);
        foot.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel ver = new JLabel("Version 1.0.0");
        ver.setFont(Theme.FONT_SMALL);
        ver.setForeground(new Color(148, 163, 184));

        JLabel copy = new JLabel("Divya Agarwal © 2026");
        copy.setFont(Theme.FONT_SMALL);
        copy.setForeground(new Color(148, 163, 184));

        foot.add(ver);
        foot.add(copy);
        sidebar.add(foot, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavButton(JPanel container, String screenKey, String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(Theme.NAVY_MEDIUM);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> showScreen(screenKey));

        navButtons.put(screenKey, btn);
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(0, 4)));
    }

    public void showScreen(String key) {
        cardLayout.show(centerPanel, key);

        // Update active nav button state
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton b = entry.getValue();
            if (entry.getKey().equals(key)) {
                b.setBackground(Theme.BLUE_PRIMARY);
                b.setForeground(Color.WHITE);
                b.setFont(Theme.FONT_BODY_BOLD);
            } else {
                b.setBackground(Theme.NAVY_MEDIUM);
                b.setForeground(new Color(226, 232, 240));
                b.setFont(Theme.FONT_BODY);
            }
        }

        // Refresh panels on switch
        if ("DASHBOARD".equals(key)) dashboardPanel.refreshData();
        else if ("STUDENTS".equals(key)) studentPanel.loadStudents();
        else if ("MENTORS".equals(key)) mentorPanel.loadMentors();
        else if ("MATCHING".equals(key)) matchingPanel.loadStudents();
        else if ("REQUESTS".equals(key)) requestPanel.loadRequests();
        else if ("SESSIONS".equals(key)) sessionPanel.loadSessions();
        else if ("FEEDBACK".equals(key)) feedbackPanel.loadFeedback();
    }

    public void showSessionSchedulerForRequest(int requestId) {
        showScreen("SESSIONS");
        sessionPanel.showScheduleDialog(requestId);
    }

    public void showFeedbackDialogForSession(MentorshipSession s) {
        showScreen("FEEDBACK");
        feedbackPanel.showSubmitFeedbackDialog(s);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out of MentorBridge?",
                "Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AuthService.logout();
            SwingUtilities.invokeLater(() -> {
                LoginFrame lf = new LoginFrame();
                lf.setVisible(true);
                dispose();
            });
        }
    }
}
