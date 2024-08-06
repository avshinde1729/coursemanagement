package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.Admin;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.service.AdminService;
import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/add")
    public ResponseEntity<Admin> addAdmin(@Valid @RequestBody Admin admin) {
        Admin savedAdmin = adminService.saveAdmin(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long adminId, @RequestBody Admin admin) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(adminId, admin);
            return ResponseEntity.ok(updatedAdmin);
        } catch (ValidationException e) {
            // Exception handler will return custom error message
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable Long studentId) {
        List<Course> courses = studentService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/student/{studentId}/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollStudentInCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        try {
            studentService.enrollStudentInCourse(studentId, courseId);
            return ResponseEntity.ok("Student enrolled in course successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/student/{studentId}/courses/{courseId}/unenroll")
    public ResponseEntity<String> unenrollStudentFromCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        try {
            studentService.unenrollStudentFromCourse(studentId, courseId);
            return ResponseEntity.ok("Student unenrolled from course successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
