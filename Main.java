/*
 * Name: Neil Cardoz
 * PRN: 23070126079
 * Batch: 23-27
 */

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            choice = getChoice();
            
            try {
                processChoice(choice);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 8);
        
        scanner.close();
    }

    /**
     * Displays the main menu options
     */
    private static void displayMenu() {
        System.out.println("\n===== Student Management System =====");
        System.out.println("1. Add Student");
        System.out.println("2. Display All Students");
        System.out.println("3. Search Student by PRN");
        System.out.println("4. Search Student by Name");
        System.out.println("5. Update Student");
        System.out.println("6. Delete Student");
        System.out.println("7. Display Statistics");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Gets and validates user choice
     */
    private static int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Processes user choice and calls appropriate methods
     */
    private static void processChoice(int choice) throws Exception {
        switch (choice) {
            case 1 -> handleAddStudent();
            case 2 -> handleDisplayStudents();
            case 3 -> handleSearchByPrn();
            case 4 -> handleSearchByName();
            case 5 -> handleUpdateStudent();
            case 6 -> handleDeleteStudent();
            case 7 -> handleDisplayStatistics();
            case 8 -> System.out.println("Exiting program. Goodbye!");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void handleAddStudent() {
        System.out.print("Enter PRN: ");
        String prn = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Date of Birth (DD-MM-YYYY): ");
        String dob = scanner.nextLine();
        System.out.print("Enter Marks: ");
        double marks = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        try {
            Student newStudent = new Student(prn, name, dob, marks);
            studentDAO.addStudent(newStudent);
            System.out.println("Student added successfully.");
        } catch (StudentExceptions.DuplicatePRNException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void handleDisplayStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println(student);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (StudentExceptions.EmptyStudentListException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleSearchByPrn() {
        System.out.print("Enter PRN to search: ");
        String searchPrn = scanner.nextLine();
        try {
            Student foundStudent = studentDAO.findByPrn(searchPrn);
            System.out.println("Student Found: " + foundStudent);
        } catch (StudentExceptions.StudentNotFoundException e) {
            System.out.println("Student with PRN " + searchPrn + " not found.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void handleSearchByName() {
        System.out.print("Enter Name to search: ");
        String searchName = scanner.nextLine();
        try {
            List<Student> foundStudents = studentDAO.findByName(searchName);
            for (Student student : foundStudents) {
                System.out.println(student);
            }
        } catch (StudentExceptions.StudentNotFoundException e) {
            System.out.println("No students found with name: " + searchName);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void handleUpdateStudent() {
        System.out.print("Enter PRN of the student to update: ");
        String updatePrn = scanner.nextLine();
        try {
            Student existingStudent = studentDAO.findByPrn(updatePrn);
            System.out.print("Enter new Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new Date of Birth (DD-MM-YYYY): ");
            String newDob = scanner.nextLine();
            System.out.print("Enter new Marks: ");
            double newMarks = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Student updatedStudent = new Student(updatePrn, newName, newDob, newMarks);
            studentDAO.updateStudent(updatePrn, updatedStudent);
            System.out.println("Student updated successfully.");
        } catch (StudentExceptions.StudentNotFoundException e) {
            System.out.println("Student with PRN " + updatePrn + " not found.");
        } catch (SQLException | StudentExceptions.StudentUpdateException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private static void handleDeleteStudent() {
        System.out.print("Enter PRN of the student to delete: ");
        String deletePrn = scanner.nextLine();
        try {
            studentDAO.deleteStudent(deletePrn);
            System.out.println("Student deleted successfully.");
        } catch (StudentExceptions.StudentDeletionException e) {
            System.out.println("Student with PRN " + deletePrn + " not found.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void handleDisplayStatistics() {
        System.out.println("Displaying statistics...");
        // Implement statistics display logic here
    }
}
