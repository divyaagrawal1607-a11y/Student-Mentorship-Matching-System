package com.mentorbridge;

import com.mentorbridge.database.DatabaseInitializer;
import com.mentorbridge.models.MatchResult;
import com.mentorbridge.models.Student;
import com.mentorbridge.services.MatchingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchingServiceTest {

    @BeforeAll
    public static void setup() {
        DatabaseInitializer.initialize();
    }

    @Test
    public void testMatchingAlgorithm() {
        MatchingService service = new MatchingService();

        Student student = new Student();
        student.setFullName("Divya Agarwal");
        student.setSkillsInterests("Java, Spring Boot, MySQL, Cloud Computing");
        student.setCareerGoal("Software Architect / Enterprise Java Developer");

        List<MatchResult> matches = service.findMatches(student);
        assertNotNull(matches);
        assertFalse(matches.isEmpty(), "Matches should not be empty with seeded mentors");

        // The top match for Java & Cloud Computing should be Dr. Rajesh Khanna
        MatchResult topMatch = matches.get(0);
        assertTrue(topMatch.getMatchPercentage() > 0.0);
        assertNotNull(topMatch.getExplanation());
        assertNotNull(topMatch.getMatchedSkills());
        assertTrue(topMatch.getMatchedSkills().contains("Java") || topMatch.getMatchedSkills().contains("Spring"));
    }
}
