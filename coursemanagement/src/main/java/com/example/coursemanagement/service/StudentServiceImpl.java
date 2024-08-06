
package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new ValidationException("Invalid student ID: " + studentId);
        }
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        return studentOptional.orElse(null);
    }

    @Override
    public Student saveStudent(Student student) {
        List<String> errors = validateStudent(student);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        if (student.getStudentId() != null) {
            return updateStudent(student.getStudentId(), student);
        } else {
            return studentRepository.save(student);
        }
    }

    @Override
    public void deleteStudent(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new ValidationException("Invalid student ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public List<Course> getCoursesByStudentId(Long studentId) {
        Student student = getStudentById(studentId);
        return List.copyOf(student.getCourses());
    }

    @Override
    public void enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ValidationException("Course not found with ID: " + courseId));

        if (student.getCourses().contains(course)) {
            throw new ValidationException("Student is already enrolled in this course.");
        }

        student.getCourses().add(course);
        course.getStudents().add(student); // Add student to course's student list
        studentRepository.save(student);
        courseRepository.save(course);
    }

    @Override
    public void unenrollStudentFromCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ValidationException("Course not found with ID: " + courseId));

        if (!student.getCourses().contains(course)) {
            throw new ValidationException("Student is not enrolled in this course.");
        }

        student.getCourses().remove(course);
        course.getStudents().remove(student); // Remove student from course's student list
        studentRepository.save(student);
        courseRepository.save(course);
    }

    @Override
    public Student updateStudent(Long studentId, Map<String, Object> updates) {
        Student existingStudent = getStudentById(studentId);

        if (existingStudent == null) {
            throw new ValidationException("Student not found.");
        }

        List<String> errors = new ArrayList<>();

        if (updates.containsKey("username")) {
            String username = (String) updates.get("username");
            if (username == null || username.trim().isEmpty()) {
                errors.add("Username cannot be null or empty.");
            } else {
                existingStudent.setUsername(username);
            }
        }

        if (updates.containsKey("password")) {
            String password = (String) updates.get("password");
            if (password == null || password.trim().isEmpty()) {
                errors.add("Password cannot be null or empty.");
            } else if (!isValidPassword(password)) {
                errors.add("Invalid password format.");
            } else {
                existingStudent.setPassword(password);
            }
        }

        if (updates.containsKey("firstName")) {
            String firstName = (String) updates.get("firstName");
            if (firstName == null || firstName.trim().isEmpty()) {
                errors.add("First name cannot be null or empty.");
            } else {
                existingStudent.setFirstName(firstName);
            }
        }

        if (updates.containsKey("lastName")) {
            String lastName = (String) updates.get("lastName");
            if (lastName == null || lastName.trim().isEmpty()) {
                errors.add("Last name cannot be null or empty.");
            } else {
                existingStudent.setLastName(lastName);
            }
        }

        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (email == null || email.trim().isEmpty()) {
                errors.add("Email cannot be null or empty.");
            } else if (!isValidEmail(email)) {
                errors.add("Invalid email format.");
            } else {
                existingStudent.setEmail(email);
            }
        }

        if (updates.containsKey("mobileNo")) {
            String mobileNo = (String) updates.get("mobileNo");
            if (mobileNo != null && mobileNo.length() != 10) {
                errors.add("Mobile number should be exactly 10 digits.");
            } else {
                existingStudent.setMobileNo(mobileNo);
            }
        }

        if (updates.containsKey("address")) {
            existingStudent.setAddress((String) updates.get("address"));
        }

        if (updates.containsKey("classDivision")) {
            existingStudent.setClassDivision((String) updates.get("classDivision"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return studentRepository.save(existingStudent);
    }

    private Student updateStudent(Long studentId, Student student) {
        if (!studentRepository.existsById(studentId)) {
            throw new ValidationException("Student not found.");
        }
        return studentRepository.save(student);
    }


    private List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();

        if (student == null) {
            errors.add("Student cannot be null.");
        }
        if (student.getUsername() == null || student.getUsername().trim().isEmpty()) {
            errors.add("Username is required.");
        } else if (studentRepository.findByUsername(student.getUsername()) != null) {
            errors.add("Username already exists.");
        }

        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (!isValidPassword(student.getPassword())) {
            errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character.");
        }

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            errors.add("First name is required.");
        }

        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            errors.add("Last name is required.");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!isValidEmail(student.getEmail())) {
            errors.add("Email is invalid.");
        } else if (studentRepository.findByUsername(student.getEmail()) != null) {
            errors.add("Email already exists.");
        }

        if (student.getMobileNo() != null && student.getMobileNo().length() != 10) {
            errors.add("Mobile number should be exactly 10 digits.");
        }

        return errors;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
