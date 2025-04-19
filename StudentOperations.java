/*
 * StudentOperations.java
 * Description: This file contains methods to perform operations on an ArrayList of Student objects.
 */

import java.util.ArrayList;

public class StudentOperations {
    // ArrayList to store Student objects
    private ArrayList<Student> students;

    // Constructor to initialize the ArrayList
    public StudentOperations() {
        students = new ArrayList<>();
    }

    // Validation methods
    private void validatePRN(String prn) throws StudentExceptions.InvalidPRNException {
        if (prn == null || prn.trim().isEmpty() || !prn.matches("\\d{11}")) {
            throw new StudentExceptions.InvalidPRNException(prn);
        }
    }

    private void validateMarks(double marks) throws StudentExceptions.InvalidMarksException {
        if (marks < 0 || marks > 100) {
            throw new StudentExceptions.InvalidMarksException(marks);
        }
    }

    public void addStudent(Student student) throws StudentExceptions.DuplicatePRNException, 
                                                 StudentExceptions.InvalidStudentDataException {
        // Validate student data
        try {
            validatePRN(student.getPrn());
            validateMarks(student.getMarks());
        } catch (StudentExceptions.InvalidPRNException | StudentExceptions.InvalidMarksException e) {
            throw new StudentExceptions.InvalidStudentDataException(e.getMessage());
        }

        // Check for duplicate PRN
        try {
            if (searchByPrn(student.getPrn()) != null) {
                throw new StudentExceptions.DuplicatePRNException(student.getPrn());
            }
        } catch (StudentExceptions.InvalidSearchCriteriaException | StudentExceptions.StudentNotFoundException e) {
            // If StudentNotFoundException is caught, it means the PRN is not a duplicate, so we can proceed.
        }

        students.add(student);
        System.out.println("Student added successfully.");
    }

    public void displayStudents() throws StudentExceptions.EmptyStudentListException {
        if (students.isEmpty()) {
            throw new StudentExceptions.EmptyStudentListException();
        }

        for (int i = 0; i < students.size(); i++) {
            System.out.println("Position " + i + ": " + students.get(i));
        }
    }

    public Student searchByPrn(String prn) throws StudentExceptions.InvalidSearchCriteriaException, 
                                                 StudentExceptions.StudentNotFoundException {
        try {
            validatePRN(prn);
        } catch (StudentExceptions.InvalidPRNException e) {
            throw new StudentExceptions.InvalidSearchCriteriaException(e.getMessage());
        }

        for (Student student : students) {
            if (student.getPrn().equals(prn)) {
                return student;
            }
        }
        throw new StudentExceptions.StudentNotFoundException("PRN: " + prn);
    }

    public Student searchByName(String name) throws StudentExceptions.InvalidSearchCriteriaException, 
                                                  StudentExceptions.StudentNotFoundException {
        if (name == null || name.trim().isEmpty()) {
            throw new StudentExceptions.InvalidSearchCriteriaException("Name cannot be empty");
        }

        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        throw new StudentExceptions.StudentNotFoundException("Name: " + name);
    }

    public Student searchByPosition(int position) throws StudentExceptions.InvalidSearchCriteriaException, 
                                                       StudentExceptions.StudentNotFoundException {
        if (position < 0) {
            throw new StudentExceptions.InvalidSearchCriteriaException("Position cannot be negative");
        }

        if (position >= students.size()) {
            throw new StudentExceptions.StudentNotFoundException("Position: " + position);
        }

        return students.get(position);
    }

    public void updateStudent(String prn, Student updatedStudent) throws StudentExceptions.StudentUpdateException, 
                                                                       StudentExceptions.InvalidUpdateDataException {
        try {
            validatePRN(prn);
            validatePRN(updatedStudent.getPrn());
            validateMarks(updatedStudent.getMarks());
        } catch (StudentExceptions.InvalidPRNException | StudentExceptions.InvalidMarksException e) {
            throw new StudentExceptions.InvalidUpdateDataException(e.getMessage());
        }

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getPrn().equals(prn)) {
                students.set(i, updatedStudent);
                return;
            }
        }
        throw new StudentExceptions.StudentUpdateException("Student with PRN " + prn + " not found");
    }

    public void deleteStudent(String prn) throws StudentExceptions.StudentDeletionException, 
                                               StudentExceptions.InvalidPRNException {
        validatePRN(prn);

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getPrn().equals(prn)) {
                students.remove(i);
                return;
            }
        }
        throw new StudentExceptions.StudentDeletionException("Student with PRN " + prn + " not found");
    }
}
