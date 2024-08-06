//
//
//
//package com.example.coursemanagement.controller;
//
//import com.example.coursemanagement.model.StudentCourse;
//import com.example.coursemanagement.service.StudentCourseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/student-course")
//public class StudentCourseController {
//
//    @Autowired
//    private StudentCourseService studentCourseService;
//
//    @GetMapping("/all")
//    public List<StudentCourse> getAllStudentCourses() {
//        return studentCourseService.getAllStudentCourses();
//    }
//
//    @GetMapping("/student/{studentId}")
//    public List<StudentCourse> getStudentCoursesByStudentId(@PathVariable Long studentId) {
//        return studentCourseService.getStudentCoursesByStudentId(studentId);
//    }
//
//    @GetMapping("/course/{courseId}")
//    public List<StudentCourse> getStudentCoursesByCourseId(@PathVariable Long courseId) {
//        return studentCourseService.getStudentCoursesByCourseId(courseId);
//    }
//
//    @PostMapping("/enroll")
//    public StudentCourse enrollStudentToCourse(@RequestBody EnrollRequest request) {
//        return studentCourseService.enrollStudent(request.getStudentId(), request.getCourseId());
//    }
//
//    @DeleteMapping("/unenroll")
//    public void unenrollStudentFromCourse(@RequestBody EnrollRequest request) {
//        studentCourseService.unenrollStudent(request.getStudentId(), request.getCourseId());
//    }
//
//    // Inner class for the request body
//    public static class EnrollRequest {
//        private Long studentId;
//        private Long courseId;
//
//        // Getters and setters
//        public Long getStudentId() {
//            return studentId;
//        }
//
//        public void setStudentId(Long studentId) {
//            this.studentId = studentId;
//        }
//
//        public Long getCourseId() {
//            return courseId;
//        }
//
//        public void setCourseId(Long courseId) {
//            this.courseId = courseId;
//        }
//    }
//}
//
