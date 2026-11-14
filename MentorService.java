package com.mentorbridge.services;

import com.mentorbridge.dao.MentorDAO;
import com.mentorbridge.models.Mentor;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Business service for Mentor management.
 */
public class MentorService {

    private final MentorDAO mentorDAO = new MentorDAO();

    public List<Mentor> getAllMentors() {
        return mentorDAO.findAll();
    }

    public List<Mentor> searchMentors(String query) {
        if (!ValidationUtil.isNotEmpty(query)) {
            return getAllMentors();
        }
        return mentorDAO.search(query);
    }

    public Mentor getMentorById(int id) {
        return mentorDAO.findById(id);
    }

    public Mentor getMentorByUserId(int userId) {
        return mentorDAO.findByUserId(userId);
    }

    public boolean addMentor(Mentor mentor) throws Exception {
        validateMentor(mentor);
        if (mentorDAO.findByEmail(mentor.getEmail()) != null) {
            throw new IllegalArgumentException("A mentor with this email address already exists.");
        }
        return mentorDAO.create(mentor) > 0;
    }

    public boolean updateMentor(Mentor mentor) throws Exception {
        validateMentor(mentor);
        Mentor existing = mentorDAO.findByEmail(mentor.getEmail());
        if (existing != null && existing.getId() != mentor.getId()) {
            throw new IllegalArgumentException("Another mentor already has this email address.");
        }
        return mentorDAO.update(mentor);
    }

    public boolean deleteMentor(int mentorId) throws SQLException {
        return mentorDAO.delete(mentorId);
    }

    public int getTotalCount() {
        return mentorDAO.countTotal();
    }

    private void validateMentor(Mentor m) {
        if (!ValidationUtil.isNotEmpty(m.getFullName())) throw new IllegalArgumentException("Full name cannot be empty.");
        if (!ValidationUtil.isValidEmail(m.getEmail())) throw new IllegalArgumentException("A valid email address is required.");
        if (!ValidationUtil.isNotEmpty(m.getExpertiseSkills())) throw new IllegalArgumentException("Expertise skills cannot be empty.");
        if (m.getExperienceYears() < 0) throw new IllegalArgumentException("Experience years cannot be negative.");
        if (!ValidationUtil.isNotEmpty(m.getAvailability())) throw new IllegalArgumentException("Availability cannot be empty.");
    }
}
