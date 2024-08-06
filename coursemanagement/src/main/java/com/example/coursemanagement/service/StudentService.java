package com.example.coursemanagement.service;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long studentId);

    Student saveStudent(Student student);

    void deleteStudent(Long studentId);

    void unenrollStudentFromCourse(Long studentId, Long courseId);

    List<Course> getCoursesByStudentId(Long studentId);

    void enrollStudentInCourse(Long studentId, Long courseId);

    Student updateStudent(Long studentId, Map<String, Object> updates);
}
