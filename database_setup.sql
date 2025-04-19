-- Create the database
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Create the students table
CREATE TABLE IF NOT EXISTS students (
    prn VARCHAR(11) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    marks DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create index on name for faster search
CREATE INDEX idx_student_name ON students(name);