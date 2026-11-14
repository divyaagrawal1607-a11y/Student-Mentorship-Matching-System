package com.mentorbridge.services;

import com.mentorbridge.dao.FeedbackDAO;
import com.mentorbridge.models.Feedback;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Service for submitting and reviewing session feedback.
 */
public class FeedbackService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    public List<Feedback> getAllFeedback() {
        return feedbackDAO.findAll();
    }

    public List<Feedback> getFeedbackForMentor(int mentorId) {
        return feedbackDAO.findByMentorId(mentorId);
    }

    public boolean submitFeedback(int sessionId, int studentId, int mentorId, int rating, String comments) throws Exception {
        if (!ValidationUtil.isValidRating(rating)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (feedbackDAO.hasFeedback(sessionId)) {
            throw new IllegalStateException("Feedback has already been submitted for this session.");
        }

        Feedback f = new Feedback();
        f.setSessionId(sessionId);
        f.setStudentId(studentId);
        f.setMentorId(mentorId);
        f.setRating(rating);
        f.setComments(comments);

        return feedbackDAO.create(f) > 0;
    }

    public double getAverageRating(int mentorId) {
        return feedbackDAO.getAverageRatingForMentor(mentorId);
    }
}
