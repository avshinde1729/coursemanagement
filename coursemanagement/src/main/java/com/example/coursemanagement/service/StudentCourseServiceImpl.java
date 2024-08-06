//
//package com.example.coursemanagement.service;
//
//import com.example.coursemanagement.exception.ValidationException;
//import com.example.coursemanagement.model.Course;
//import com.example.coursemanagement.model.Student;
//import com.example.coursemanagement.model.StudentCourse;
//import com.example.coursemanagement.repository.CourseRepository;
//import com.example.coursemanagement.repository.StudentCourseRepository;
//import com.example.coursemanagement.repository.StudentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class    StudentCourseServiceImpl implements StudentCourseService {
//
//    @Autowired
//    private StudentCourseRepository studentCourseRepository;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//    @Override
//    public List<StudentCourse> getAllStudentCourses() {
//        return studentCourseRepository.findAll();
//    }
//
//    @Override
//    public List<StudentCourse> getStudentCoursesByStudentId(Long studentId) {
//        Optional<Student> studentOptional = studentRepository.findById(studentId);
//        if (studentOptional.isPresent()) {
//            return studentCourseRepository.findAllByStudent(studentOptional.get());
//        } else {
//            throw new ValidationException("Student not found with ID: " + studentId);
//        }
//    }
//
//    @Override
//    public List<StudentCourse> getStudentCoursesByCourseId(Long courseId) {
//        Optional<Course> courseOptional = courseRepository.findById(courseId);
//        if (courseOptional.isPresent()) {
//            return studentCourseRepository.findAllByCourse(courseOptional.get());
//        } else {
//            throw new ValidationException("Course not found with ID: " + courseId);
//        }
//    }
//
//    @Override
//    public StudentCourse enrollStudent(Long studentId, Long courseId) {
//        Optional<Student> studentOptional = studentRepository.findById(studentId);
//        Optional<Course> courseOptional = courseRepository.findById(courseId);
//
//        if (studentOptional.isPresent() && courseOptional.isPresent()) {
//            Student student = studentOptional.get();
//            Course course = courseOptional.get();
//
//            StudentCourse studentCourse = new StudentCourse();
//            studentCourse.setStudent(student);
//            studentCourse.setCourse(course);
//
//            return studentCourseRepository.save(studentCourse);
//        } else {
//            throw new ValidationException("Student or Course not found");
//        }
//    }
//
//    @Override
//    public void unenrollStudent(Long studentId, Long courseId) {
//        Optional<Student> studentOptional = studentRepository.findById(studentId);
//        Optional<Course> courseOptional = courseRepository.findById(courseId);
//
//        if (studentOptional.isPresent() && courseOptional.isPresent()) {
//            Student student = studentOptional.get();
//            Course course = courseOptional.get();
//
//            studentCourseRepository.deleteByStudentAndCourse(student, course);
//        } else {
//            throw new ValidationException("Student or Course not found");
//        }
//    }
//
//    // Other methods as needed
//}
//
