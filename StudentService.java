package com.mentorbridge.services;

import com.mentorbridge.dao.StudentDAO;
import com.mentorbridge.models.Student;
import com.mentorbridge.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Business service for Student management.
 */
public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    public List<Student> searchStudents(String query) {
        if (!ValidationUtil.isNotEmpty(query)) {
            return getAllStudents();
        }
        return studentDAO.search(query);
    }

    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    public Student getStudentByUserId(int userId) {
        return studentDAO.findByUserId(userId);
    }

    public boolean addStudent(Student student) throws Exception {
        validateStudent(student);
        if (studentDAO.findByEmail(student.getEmail()) != null) {
            throw new IllegalArgumentException("A student with this email address already exists.");
        }
        return studentDAO.create(student) > 0;
    }

    public boolean updateStudent(Student student) throws Exception {
        validateStudent(student);
        Student existing = studentDAO.findByEmail(student.getEmail());
        if (existing != null && existing.getId() != student.getId()) {
            throw new IllegalArgumentException("Another student already has this email address.");
        }
        return studentDAO.update(student);
    }

    public boolean deleteStudent(int studentId) throws SQLException {
        return studentDAO.delete(studentId);
    }

    public int getTotalCount() {
        return studentDAO.countTotal();
    }

    private void validateStudent(Student s) {
        if (!ValidationUtil.isNotEmpty(s.getFullName())) throw new IllegalArgumentException("Full name cannot be empty.");
        if (!ValidationUtil.isValidEmail(s.getEmail())) throw new IllegalArgumentException("A valid email address is required.");
        if (!ValidationUtil.isNotEmpty(s.getDepartment())) throw new IllegalArgumentException("Department cannot be empty.");
        if (!ValidationUtil.isValidStudyYear(s.getStudyYear())) throw new IllegalArgumentException("Year of study must be between 1 and 6.");
        if (!ValidationUtil.isNotEmpty(s.getSkillsInterests())) throw new IllegalArgumentException("Skills & interests cannot be empty.");
        if (!ValidationUtil.isNotEmpty(s.getCareerGoal())) throw new IllegalArgumentException("Career goal cannot be empty.");
        if (!ValidationUtil.isValidPhone(s.getContactNumber())) throw new IllegalArgumentException("Valid contact phone number is required.");
    }
}
