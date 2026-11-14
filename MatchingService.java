package com.mentorbridge.services;

import com.mentorbridge.dao.MentorDAO;
import com.mentorbridge.models.MatchResult;
import com.mentorbridge.models.Mentor;
import com.mentorbridge.models.Student;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Intelligent Mentor Matching Service.
 * Implements token-based overlap, semantic career alignment, and availability weighting.
 */
public class MatchingService {

    private final MentorDAO mentorDAO = new MentorDAO();

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "and", "or", "in", "to", "for", "with", "the", "a", "an", "on", "at", "by", "from",
            "as", "into", "through", "during", "before", "after", "above", "below", "between",
            "skills", "experience", "interested", "engineer", "development", "role"
    ));

    /**
     * Recommends ranked mentors for a given student.
     */
    public List<MatchResult> findMatches(Student student) {
        if (student == null) return Collections.emptyList();

        List<Mentor> mentors = mentorDAO.findAll();
        List<MatchResult> results = new ArrayList<>();

        Set<String> studentSkills = extractKeywords(student.getSkillsInterests());
        Set<String> studentGoals = extractKeywords(student.getCareerGoal());

        for (Mentor mentor : mentors) {
            // Check availability filter
            boolean isAvailable = isMentorAvailable(mentor);
            if (!isAvailable) {
                continue; // Exclude completely unavailable mentors
            }

            Set<String> mentorSkills = extractKeywords(mentor.getExpertiseSkills());
            Set<String> mentorBio = extractKeywords(mentor.getBiography());

            // 1. Skill overlap
            List<String> matchedSkills = new ArrayList<>();
            for (String sSkill : studentSkills) {
                for (String mSkill : mentorSkills) {
                    if (sSkill.contains(mSkill) || mSkill.contains(sSkill)) {
                        matchedSkills.add(capitalize(mSkill));
                        break;
                    }
                }
            }
            // Remove duplicates
            matchedSkills = matchedSkills.stream().distinct().collect(Collectors.toList());

            // Skill overlap ratio
            double skillScore = 0.0;
            if (!studentSkills.isEmpty()) {
                skillScore = (double) matchedSkills.size() / (double) studentSkills.size();
                if (skillScore > 1.0) skillScore = 1.0;
            }

            // 2. Career Goal alignment
            long goalMatches = 0;
            for (String gWord : studentGoals) {
                if (mentorSkills.contains(gWord) || mentorBio.contains(gWord)) {
                    goalMatches++;
                }
            }
            double goalScore = studentGoals.isEmpty() ? 0.0 : (double) goalMatches / (double) studentGoals.size();
            if (goalScore > 1.0) goalScore = 1.0;

            // 3. Experience factor (capped at 15 years = 1.0)
            double expFactor = Math.min(1.0, (double) mentor.getExperienceYears() / 15.0);

            // Total weighted score: 60% skills, 25% goals, 15% experience
            double totalScore = (skillScore * 60.0) + (goalScore * 25.0) + (expFactor * 15.0);

            // Round to 1 decimal place
            totalScore = Math.round(totalScore * 10.0) / 10.0;

            // Generate contextual explanation
            StringBuilder explanation = new StringBuilder();
            if (!matchedSkills.isEmpty()) {
                explanation.append("Matched skills: ").append(String.join(", ", matchedSkills)).append(". ");
            } else {
                explanation.append("Broad domain overlap with your target field. ");
            }

            if (mentor.getExperienceYears() > 0) {
                explanation.append("Mentor brings ").append(mentor.getExperienceYears()).append(" yrs experience. ");
            }

            if (goalMatches > 0) {
                explanation.append("Strong career goal synergy. ");
            }

            explanation.append("Availability: ").append(mentor.getAvailability()).append(".");

            results.add(new MatchResult(mentor, totalScore, matchedSkills, explanation.toString(), true));
        }

        // Rank by highest match percentage
        results.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));

        return results;
    }

    private boolean isMentorAvailable(Mentor mentor) {
        if (mentor.getAvailability() == null || mentor.getAvailability().trim().isEmpty()) {
            return false;
        }
        String lower = mentor.getAvailability().toLowerCase();
        return !lower.contains("unavailable") && !lower.contains("none") && !lower.contains("inactive");
    }

    private Set<String> extractKeywords(String text) {
        Set<String> set = new HashSet<>();
        if (text == null) return set;
        String[] tokens = text.toLowerCase().split("[,;/\\s]+");
        for (String t : tokens) {
            String clean = t.replaceAll("[^a-zA-Z0-9#+]", "").trim();
            if (clean.length() >= 2 && !STOP_WORDS.contains(clean)) {
                set.add(clean);
            }
        }
        return set;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
