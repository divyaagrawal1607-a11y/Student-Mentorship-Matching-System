package com.mentorbridge.database;

import com.mentorbridge.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Idempotent Database Schema and Initial Data Initializer.
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void initialize() {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(120) NOT NULL UNIQUE, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL DEFAULT 'STUDENT', " +
                    "account_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 2. Students Table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NULL, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(120) NOT NULL UNIQUE, " +
                    "department VARCHAR(80) NOT NULL, " +
                    "study_year INT NOT NULL, " +
                    "skills_interests TEXT NOT NULL, " +
                    "career_goal VARCHAR(255) NOT NULL, " +
                    "contact_number VARCHAR(20) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 3. Mentors Table
            stmt.execute("CREATE TABLE IF NOT EXISTS mentors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NULL, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(120) NOT NULL UNIQUE, " +
                    "expertise_skills TEXT NOT NULL, " +
                    "experience_years INT NOT NULL, " +
                    "availability VARCHAR(100) NOT NULL, " +
                    "biography TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 4. Mentorship Requests Table
            stmt.execute("CREATE TABLE IF NOT EXISTS mentorship_requests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "mentor_id INT NOT NULL, " +
                    "request_message TEXT, " +
                    "request_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', " +
                    "request_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 5. Mentorship Sessions Table
            stmt.execute("CREATE TABLE IF NOT EXISTS mentorship_sessions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "request_id INT NOT NULL, " +
                    "session_datetime TIMESTAMP NOT NULL, " +
                    "meeting_mode VARCHAR(20) NOT NULL DEFAULT 'ONLINE', " +
                    "meeting_link_location VARCHAR(255) NOT NULL, " +
                    "session_status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', " +
                    "notes TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // 6. Feedback Table
            stmt.execute("CREATE TABLE IF NOT EXISTS feedback (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "session_id INT NOT NULL, " +
                    "student_id INT NOT NULL, " +
                    "mentor_id INT NOT NULL, " +
                    "rating INT NOT NULL, " +
                    "comments TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Check if Admin exists, if not seed default accounts
            seedInitialData(conn);

            LOGGER.info("Database initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database initialization failed: " + e.getMessage(), e);
        }
    }

    private static void seedInitialData(Connection conn) {
        try {
            // Check if users table is empty
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return; // Already seeded
                }
            }

            String defaultHash = PasswordUtil.hashPassword("admin123");

            // Seed Admin User
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (name, email, password_hash, role, account_status) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, "System Administrator");
                ps.setString(2, "admin@mentorbridge.edu");
                ps.setString(3, defaultHash);
                ps.setString(4, "ADMIN");
                ps.setString(5, "ACTIVE");
                ps.executeUpdate();
            }

            // Seed Student Users and Students
            String studentHash = PasswordUtil.hashPassword("student123");
            insertStudent(conn, "Divya Agarwal", "divya.agarwal@student.edu", studentHash,
                    "Computer Science", 3, "Java, Spring Boot, MySQL, Cloud Computing, Full-Stack Development",
                    "Senior Software Engineer / Solutions Architect", "+91 98765 43210");

            insertStudent(conn, "Aarav Sharma", "aarav.sharma@student.edu", studentHash,
                    "Information Technology", 2, "Python, Machine Learning, Data Analytics, Deep Learning, SQL",
                    "Data Scientist / AI Researcher", "+91 98123 45678");

            insertStudent(conn, "Rohan Verma", "rohan.verma@student.edu", studentHash,
                    "Electronics & Comm", 4, "Embedded Systems, IoT, C++, Robotics, RTOS",
                    "Embedded Systems Firmware Engineer", "+91 97234 56789");

            // Seed Mentor Users and Mentors
            String mentorHash = PasswordUtil.hashPassword("mentor123");
            insertMentor(conn, "Dr. Rajesh Khanna", "rajesh.khanna@faculty.edu", mentorHash,
                    "Java, Enterprise Architecture, Distributed Systems, Cloud Computing, Spring Boot", 15,
                    "Weekdays 4:00 PM - 7:00 PM",
                    "Professor of Computer Engineering with 15+ years experience mentoring university students into Tier-1 software roles.");

            insertMentor(conn, "Priya Sunder", "priya.sunder@techcorp.com", mentorHash,
                    "Full-Stack Development, React, Node.js, System Design, Career Mentoring", 8,
                    "Weekends 10:00 AM - 2:00 PM",
                    "Staff Software Engineer at global tech company. Passionate about women in tech and interview coaching.");

            insertMentor(conn, "Vikram Mehta", "vikram.mehta@datasolutions.org", mentorHash,
                    "Python, Machine Learning, NLP, Data Analytics, MLOps, SQL", 10,
                    "Tuesday & Thursday Evenings",
                    "Lead Data Scientist with expertise in predictive modeling and generative AI application architectures.");

            // Seed sample requests, sessions, feedback
            try (Statement st = conn.createStatement()) {
                st.execute("INSERT INTO mentorship_requests (student_id, mentor_id, request_message, request_status) " +
                        "VALUES (1, 1, 'Hello Dr. Khanna, I would love your guidance on enterprise Java architectures and preparing for system design interviews.', 'ACCEPTED')");
                st.execute("INSERT INTO mentorship_requests (student_id, mentor_id, request_message, request_status) " +
                        "VALUES (2, 3, 'Hi Mr. Mehta, I am working on a machine learning project and need guidance on model fine-tuning and data pipelines.', 'ACCEPTED')");
                st.execute("INSERT INTO mentorship_requests (student_id, mentor_id, request_message, request_status) " +
                        "VALUES (3, 2, 'Dear Priya, looking for advice on transitioning to full-stack dashboard development.', 'PENDING')");

                st.execute("INSERT INTO mentorship_sessions (request_id, session_datetime, meeting_mode, meeting_link_location, session_status, notes) " +
                        "VALUES (1, CURRENT_TIMESTAMP, 'ONLINE', 'https://meet.google.com/xyz-mentor-bridge', 'COMPLETED', 'Discussed microservices, Spring Cloud, and graduation project roadmap.')");
                st.execute("INSERT INTO mentorship_sessions (request_id, session_datetime, meeting_mode, meeting_link_location, session_status, notes) " +
                        "VALUES (2, CURRENT_TIMESTAMP, 'ONLINE', 'https://meet.google.com/abc-data-mentor', 'COMPLETED', 'Reviewed CNN architecture and loss function selection.')");

                st.execute("INSERT INTO feedback (session_id, student_id, mentor_id, rating, comments) " +
                        "VALUES (1, 1, 1, 5, 'Exceptional session! Dr. Khanna provided clear actionable architectural guidance for my project.')");
                st.execute("INSERT INTO feedback (session_id, student_id, mentor_id, rating, comments) " +
                        "VALUES (2, 2, 3, 5, 'Very insightful discussion on hyperparameter tuning and model deployment.')");
            }

            LOGGER.info("Sample seed records added successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Seeding failed: " + e.getMessage());
        }
    }

    private static void insertStudent(Connection conn, String name, String email, String passHash,
                                      String dept, int year, String skills, String goal, String phone) throws Exception {
        int userId = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (name, email, password_hash, role, account_status) VALUES (?, ?, ?, 'STUDENT', 'ACTIVE')",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passHash);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) userId = rs.getInt(1);
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (user_id, full_name, email, department, study_year, skills_interests, career_goal, contact_number) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, dept);
            ps.setInt(5, year);
            ps.setString(6, skills);
            ps.setString(7, goal);
            ps.setString(8, phone);
            ps.executeUpdate();
        }
    }

    private static void insertMentor(Connection conn, String name, String email, String passHash,
                                     String skills, int exp, String avail, String bio) throws Exception {
        int userId = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (name, email, password_hash, role, account_status) VALUES (?, ?, ?, 'MENTOR', 'ACTIVE')",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passHash);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) userId = rs.getInt(1);
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO mentors (user_id, full_name, email, expertise_skills, experience_years, availability, biography) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, skills);
            ps.setInt(5, exp);
            ps.setString(6, avail);
            ps.setString(7, bio);
            ps.executeUpdate();
        }
    }
}
