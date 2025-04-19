/*
 * StudentDAO.java
 * Description: Handles all database operations for Student entity
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // SQL queries
    private static final String INSERT_STUDENT = "INSERT INTO students (prn, name, dob, marks) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_STUDENTS = "SELECT * FROM students";
    private static final String SELECT_BY_PRN = "SELECT * FROM students WHERE prn = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM students WHERE name LIKE ?";
    private static final String UPDATE_STUDENT = "UPDATE students SET name = ?, dob = ?, marks = ? WHERE prn = ?";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE prn = ?";

    /**
     * Adds a new student to the database
     */
    public List<Student> getStudentsByName(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE name = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String prn = rs.getString("prn");
                String studentName = rs.getString("name");
                String dob = rs.getString("dob");
                double marks = rs.getDouble("marks");
                students.add(new Student(prn, studentName, dob, marks));
            }
        }
        return students;
    }
    public void addStudent(Student student) throws SQLException, StudentExceptions.DuplicatePRNException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_STUDENT)) {
            
            // Set parameters for the prepared statement
            pstmt.setString(1, student.getPrn());
            pstmt.setString(2, student.getName());
            pstmt.setDate(3, Date.valueOf(student.getDob()));
            pstmt.setDouble(4, student.getMarks());

            try {
                pstmt.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new StudentExceptions.DuplicatePRNException(student.getPrn());
            }
        }
    }

    /**
     * Retrieves all students from the database
     */
    public List<Student> getAllStudents() throws SQLException, StudentExceptions.EmptyStudentListException {
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_STUDENTS)) {

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }

            if (students.isEmpty()) {
                throw new StudentExceptions.EmptyStudentListException();
            }
        }
        return students;
    }

    /**
     * Searches for a student by PRN
     */
    public Student findByPrn(String prn) throws SQLException, StudentExceptions.StudentNotFoundException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_PRN)) {

            pstmt.setString(1, prn);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        }
        throw new StudentExceptions.StudentNotFoundException("PRN: " + prn);
    }

    /**
     * Searches for students by name
     */
    public List<Student> findByName(String name) throws SQLException, StudentExceptions.StudentNotFoundException {
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME)) {

            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }

            if (students.isEmpty()) {
                throw new StudentExceptions.StudentNotFoundException("Name: " + name);
            }
        }
        return students;
    }

    /**
     * Updates an existing student's information
     */
    public void updateStudent(String prn, Student student) throws SQLException, StudentExceptions.StudentUpdateException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_STUDENT)) {

            pstmt.setString(1, student.getName());
            pstmt.setDate(2, Date.valueOf(student.getDob()));
            pstmt.setDouble(3, student.getMarks());
            pstmt.setString(4, prn);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new StudentExceptions.StudentUpdateException("No student found with PRN: " + prn);
            }
        }
    }

    /**
     * Deletes a student by PRN
     */
    public void deleteStudent(String prn) throws SQLException, StudentExceptions.StudentDeletionException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_STUDENT)) {

            pstmt.setString(1, prn);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new StudentExceptions.StudentDeletionException("No student found with PRN: " + prn);
            }
        }
    }

    /**
     * Helper method to extract Student object from ResultSet
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("prn"),
            rs.getString("name"),
            rs.getDate("dob").toLocalDate(),
            rs.getDouble("marks")
        );
    }
}