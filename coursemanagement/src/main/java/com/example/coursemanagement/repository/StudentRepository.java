package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByUsername(String username);

    List<Student> findByCourses_CourseId(Long courseId);
}
