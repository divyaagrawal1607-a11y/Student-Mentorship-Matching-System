package com.mentorbridge.ui;

import com.mentorbridge.models.Feedback;
import com.mentorbridge.models.MentorshipSession;
import com.mentorbridge.models.User;
import com.mentorbridge.services.FeedbackService;
import com.mentorbridge.utils.CsvExporter;
import com.mentorbridge.utils.DateTimeUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Panel to review and submit student mentorship feedback.
 */
public class FeedbackPanel extends JPanel {

    private final FeedbackService feedbackService = new FeedbackService();
    private final User currentUser;

    private JTable table;
    private DefaultTableModel model;

    public FeedbackPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_PAGE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initUI();
        loadFeedback();
    }

    private void initUI() {
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(Theme.BG_PAGE);

        JLabel title = new JLabel("Mentorship Feedback & Reviews");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.NAVY_DARK);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setBackground(Theme.BG_PAGE);

        JButton btnExport = UIHelper.createSecondaryButton("📥 Export CSV");
        btnExport.addActionListener(e -> exportCsv());

        JButton btnRefresh = UIHelper.createSecondaryButton("🔄 Refresh");
        btnRefresh.addActionListener(e -> loadFeedback());

        actions.add(btnExport);
        actions.add(btnRefresh);

        top.add(actions, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());

        String[] cols = {"Feedback ID", "Session ID", "Student Name", "Mentor Name", "Rating", "Comments", "Submitted At"};
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

    public void loadFeedback() {
        model.setRowCount(0);
        List<Feedback> list = feedbackService.getAllFeedback();
        for (Feedback f : list) {
            String stars = "⭐".repeat(f.getRating()) + " (" + f.getRating() + "/5)";
            model.addRow(new Object[]{
                    "#" + f.getId(),
                    "#" + f.getSessionId(),
                    f.getStudentName(),
                    f.getMentorName(),
                    stars,
                    f.getComments(),
                    DateTimeUtil.format(f.getCreatedAt())
            });
        }
    }

    public void showSubmitFeedbackDialog(MentorshipSession s) {
        JComboBox<Integer> cbRating = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
        JTextArea taComments = new JTextArea(4, 25);
        taComments.setFont(Theme.FONT_BODY);
        taComments.setLineWrap(true);
        taComments.setWrapStyleWord(true);
        taComments.setText("Great session! Highly actionable guidance provided.");

        JPanel p = new JPanel(new GridLayout(3, 2, 8, 8));
        p.add(new JLabel("Session:"));
        p.add(new JLabel("#" + s.getId() + " - " + s.getStudentName() + " with " + s.getMentorName()));
        p.add(new JLabel("Rating (1-5 Stars):"));
        p.add(cbRating);
        p.add(new JLabel("Review Comments:"));
        p.add(new JScrollPane(taComments));

        int res = JOptionPane.showConfirmDialog(this, p, "Submit Mentorship Feedback", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int rating = (int) cbRating.getSelectedItem();
                String comments = taComments.getText().trim();
                feedbackService.submitFeedback(s.getId(), s.getStudentId(), s.getMentorId(), rating, comments);
                JOptionPane.showMessageDialog(this, "Thank you! Your feedback has been recorded.", "Feedback Recorded", JOptionPane.INFORMATION_MESSAGE);
                loadFeedback();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("feedback_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean ok = CsvExporter.exportTableToCsv(table, fc.getSelectedFile());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Feedback exported to CSV successfully!", "Export Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error writing CSV file.", "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
