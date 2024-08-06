package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.service.CourseService;
import com.example.coursemanagement.service.TeacherService;
import com.example.coursemanagement.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public Course getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId);
    }

    @PostMapping("/add")
    public Course addCourse(@RequestBody @Valid Course course) {
        return courseService.saveCourse(course);
    }

    @PatchMapping("/update/{courseId}")
    public Course updateCourse(@PathVariable Long courseId, @RequestBody Map<String, Object> updates) {
        try {
            return courseService.updateCourse(courseId, updates);
        } catch (ValidationException e) {
            throw e; // Let global exception handler handle this
        } catch (EntityNotFoundException e) {
            throw e; // Let global exception handler handle this
        }
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }
}
