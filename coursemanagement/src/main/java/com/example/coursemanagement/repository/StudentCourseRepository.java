//package com.example.coursemanagement.repository;
//
//import com.example.coursemanagement.model.Student;
//import com.example.coursemanagement.model.StudentCourse;
//import com.example.coursemanagement.model.Course;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
//
//    List<StudentCourse> findAllByStudent(Student student);
//
//    List<StudentCourse> findAllByCourse(Course course);
//
//    @Transactional
//    void deleteByStudentAndCourse(Student student, Course course);
//}
