package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Course;
import java.util.List;
import java.util.Map;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(Long courseId);
    Course saveCourse(Course course) throws ValidationException;
    void deleteCourse(Long courseId);
    Course updateCourse(Long courseId, Map<String, Object> updates) throws ValidationException;
}

