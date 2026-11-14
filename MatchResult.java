package com.mentorbridge.models;

import java.util.List;

/**
 * Model representing the output of the Intelligent Mentor Matching algorithm.
 */
public class MatchResult {
    private Mentor mentor;
    private double matchPercentage;
    private List<String> matchedSkills;
    private String explanation;
    private boolean available;

    public MatchResult() {}

    public MatchResult(Mentor mentor, double matchPercentage, List<String> matchedSkills, String explanation, boolean available) {
        this.mentor = mentor;
        this.matchPercentage = matchPercentage;
        this.matchedSkills = matchedSkills;
        this.explanation = explanation;
        this.available = available;
    }

    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
