/*
 * Student.java
 * Description: Student entity class with validation
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Student {
	private String prn;
	private String name;
	private LocalDate dob;
	private double marks;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public Student(String prn, String name, LocalDate dob, double marks) {
		this.prn = prn;
		this.name = name;
		this.dob = dob;
		this.marks = marks;
	}

	public Student(String prn, String name, String dobString, double marks) throws IllegalArgumentException {
		this.prn = prn;
		this.name = name;
		try {
			this.dob = LocalDate.parse(dobString, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format. Use DD-MM-YYYY");
		}
		this.marks = marks;
	}

	// Getters and setters
	public String getPrn() { return prn; }
	public void setPrn(String prn) { this.prn = prn; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public LocalDate getDob() { return dob; }
	public void setDob(LocalDate dob) { this.dob = dob; }
	public double getMarks() { return marks; }
	public void setMarks(double marks) { this.marks = marks; }

	@Override
	public String toString() {
		return String.format("PRN: %s, Name: %s, DoB: %s, Marks: %.2f",
			prn, name, dob.format(DATE_FORMATTER), marks);
	}
}
