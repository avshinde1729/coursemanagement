package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.service.StudentService;
import com.example.coursemanagement.service.CourseService;
import com.example.coursemanagement.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(savedStudent);
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{studentId}/update")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody Map<String, Object> updates) {
        try {
            Student updatedStudent = studentService.updateStudent(studentId, updates);
            return ResponseEntity.ok(updatedStudent);
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getErrors());
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable Long studentId) {
        List<Course> courses = studentService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }
}
