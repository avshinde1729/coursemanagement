
package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new ValidationException("Invalid teacher ID: " + teacherId);
        }
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        return teacherOptional.orElse(null);
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        if (teacher.getTeacherId() != null) {
            // Handle update case
            return updateTeacher(teacher);
        } else {
            // Handle create case
            List<String> errors = validateTeacherForCreate(teacher);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
            return teacherRepository.save(teacher);
        }
    }

    @Override
    public void deleteTeacher(Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new ValidationException("Invalid teacher ID: " + teacherId);
        }
        if (!teacherRepository.existsById(teacherId)) {
            throw new ValidationException("Teacher not found with ID: " + teacherId);
        }
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public Teacher partialUpdateTeacher(Long teacherId, Teacher updatedTeacher) {
        Teacher existingTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ValidationException("Teacher not found."));

        List<String> errors = new ArrayList<>();

        if (updatedTeacher.getUsername() != null && !updatedTeacher.getUsername().equals(existingTeacher.getUsername())) {
            if (teacherRepository.findByUsername(updatedTeacher.getUsername()).isPresent()) {
                errors.add("Username already exists.");
            }
        }

        if (updatedTeacher.getEmail() != null && !updatedTeacher.getEmail().equals(existingTeacher.getEmail())) {
            if (!isValidEmail(updatedTeacher.getEmail())) {
                errors.add("Email is invalid.");
            } else if (teacherRepository.findByEmail(updatedTeacher.getEmail()).isPresent()) {
                errors.add("Email already exists.");
            }
        }

        if (updatedTeacher.getPassword() != null && !isValidPassword(updatedTeacher.getPassword())) {
            errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character.");
        }

        if (updatedTeacher.getMobileNo() != null && updatedTeacher.getMobileNo().length() != 10) {
            errors.add("Mobile number should be exactly 10 digits.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Apply changes
        if (updatedTeacher.getUsername() != null) existingTeacher.setUsername(updatedTeacher.getUsername());
        if (updatedTeacher.getPassword() != null) existingTeacher.setPassword(updatedTeacher.getPassword());
        if (updatedTeacher.getFirstName() != null) existingTeacher.setFirstName(updatedTeacher.getFirstName());
        if (updatedTeacher.getLastName() != null) existingTeacher.setLastName(updatedTeacher.getLastName());
        if (updatedTeacher.getEmail() != null) existingTeacher.setEmail(updatedTeacher.getEmail());
        if (updatedTeacher.getMobileNo() != null) existingTeacher.setMobileNo(updatedTeacher.getMobileNo());
        if (updatedTeacher.getAddress() != null) existingTeacher.setAddress(updatedTeacher.getAddress());

        return teacherRepository.save(existingTeacher);
    }

    private Teacher updateTeacher(Teacher teacher) {
        if (!teacherRepository.existsById(teacher.getTeacherId())) {
            throw new ValidationException("Teacher not found.");
        }

        List<String> errors = validateTeacherForUpdate(teacher);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return teacherRepository.save(teacher);
    }

    private List<String> validateTeacherForCreate(Teacher teacher) {
        List<String> errors = new ArrayList<>();

        if (teacher == null) {
            errors.add("Teacher cannot be null.");
        }
        if (teacher.getUsername() == null || teacher.getUsername().trim().isEmpty()) {
            errors.add("Username is required.");
        } else if (teacher.getUsername().length() < 3 || teacher.getUsername().length() > 50) {
            errors.add("Username must be between 3 and 50 characters.");
        } else if (teacherRepository.findByUsername(teacher.getUsername()).isPresent()) {
            errors.add("Username already exists.");
        }

        if (teacher.getPassword() == null || teacher.getPassword().trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (!isValidPassword(teacher.getPassword())) {
            errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character.");
        }

        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty()) {
            errors.add("First name is required.");
        }

        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty()) {
            errors.add("Last name is required.");
        }

        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!isValidEmail(teacher.getEmail())) {
            errors.add("Email is invalid.");
        } else if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            errors.add("Email already exists.");
        }

        if (teacher.getMobileNo() != null && teacher.getMobileNo().length() != 10) {
            errors.add("Mobile number should be exactly 10 digits.");
        }

        return errors;
    }

    private List<String> validateTeacherForUpdate(Teacher teacher) {
        List<String> errors = new ArrayList<>();

        if (teacher == null) {
            errors.add("Teacher cannot be null.");
        }

        Teacher existingTeacher = teacherRepository.findById(teacher.getTeacherId())
                .orElseThrow(() -> new ValidationException("Teacher not found."));

        // Validate username only if it's changed
        if (teacher.getUsername() != null && !teacher.getUsername().equals(existingTeacher.getUsername())) {
            if (teacherRepository.findByUsername(teacher.getUsername()).isPresent()) {
                errors.add("Username already exists.");
            }
        }

        // Validate email only if it's changed
        if (teacher.getEmail() != null && !teacher.getEmail().equals(existingTeacher.getEmail())) {
            if (!isValidEmail(teacher.getEmail())) {
                errors.add("Email is invalid.");
            } else if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
                errors.add("Email already exists.");
            }
        }

        // Validate password only if it's changed
        if (teacher.getPassword() != null && !teacher.getPassword().equals(existingTeacher.getPassword())) {
            if (!isValidPassword(teacher.getPassword())) {
                errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character.");
            }
        }

        // Validate mobile number only if it's changed
        if (teacher.getMobileNo() != null && !teacher.getMobileNo().equals(existingTeacher.getMobileNo())) {
            if (teacher.getMobileNo().length() != 10) {
                errors.add("Mobile number should be exactly 10 digits.");
            }
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






