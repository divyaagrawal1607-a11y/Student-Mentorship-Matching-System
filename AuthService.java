package com.mentorbridge.services;

import com.mentorbridge.dao.MentorDAO;
import com.mentorbridge.dao.StudentDAO;
import com.mentorbridge.dao.UserDAO;
import com.mentorbridge.models.Mentor;
import com.mentorbridge.models.Student;
import com.mentorbridge.models.User;
import com.mentorbridge.utils.PasswordUtil;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;

/**
 * Service for user authentication, registration, and session state.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final MentorDAO mentorDAO = new MentorDAO();

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public User login(String email, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            throw new IllegalArgumentException("No account found with this email address.");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getAccountStatus())) {
            throw new IllegalStateException("Your account is currently inactive. Please contact administrator.");
        }

        boolean matches = PasswordUtil.verifyPassword(password, user.getPasswordHash());
        if (!matches) {
            throw new IllegalArgumentException("Invalid password. Please check your credentials.");
        }

        currentUser = user;
        return user;
    }

    public User registerStudent(String fullName, String email, String password,
                                String department, int year, String skills,
                                String careerGoal, String contact) throws Exception {
        if (!ValidationUtil.isNotEmpty(fullName)) throw new IllegalArgumentException("Full name is required.");
        if (!ValidationUtil.isValidEmail(email)) throw new IllegalArgumentException("A valid email is required.");
        if (!ValidationUtil.isNotEmpty(password) || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (!ValidationUtil.isValidStudyYear(year)) throw new IllegalArgumentException("Study year must be between 1 and 6.");
        if (!ValidationUtil.isValidPhone(contact)) throw new IllegalArgumentException("Valid contact number is required.");

        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        String hash = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(fullName);
        user.setEmail(email.toLowerCase().trim());
        user.setPasswordHash(hash);
        user.setRole("STUDENT");
        user.setAccountStatus("ACTIVE");

        int userId = userDAO.create(user);
        user.setId(userId);

        Student student = new Student();
        student.setUserId(userId);
        student.setFullName(fullName);
        student.setEmail(email.toLowerCase().trim());
        student.setDepartment(department);
        student.setStudyYear(year);
        student.setSkillsInterests(skills);
        student.setCareerGoal(careerGoal);
        student.setContactNumber(contact);

        studentDAO.create(student);
        return user;
    }

    public User registerMentor(String fullName, String email, String password,
                               String expertise, int experience, String availability,
                               String biography) throws Exception {
        if (!ValidationUtil.isNotEmpty(fullName)) throw new IllegalArgumentException("Full name is required.");
        if (!ValidationUtil.isValidEmail(email)) throw new IllegalArgumentException("A valid email is required.");
        if (!ValidationUtil.isNotEmpty(password) || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (experience < 0) throw new IllegalArgumentException("Experience years cannot be negative.");
        if (!ValidationUtil.isNotEmpty(availability)) throw new IllegalArgumentException("Availability is required.");

        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        String hash = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setName(fullName);
        user.setEmail(email.toLowerCase().trim());
        user.setPasswordHash(hash);
        user.setRole("MENTOR");
        user.setAccountStatus("ACTIVE");

        int userId = userDAO.create(user);
        user.setId(userId);

        Mentor mentor = new Mentor();
        mentor.setUserId(userId);
        mentor.setFullName(fullName);
        mentor.setEmail(email.toLowerCase().trim());
        mentor.setExpertiseSkills(expertise);
        mentor.setExperienceYears(experience);
        mentor.setAvailability(availability);
        mentor.setBiography(biography);

        mentorDAO.create(mentor);
        return user;
    }
}
