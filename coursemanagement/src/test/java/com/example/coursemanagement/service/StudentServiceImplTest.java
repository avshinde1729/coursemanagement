package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.StudentRepository;
import com.example.coursemanagement.service.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Student createStudent(Long id, String username, String password, String firstName, String lastName, String email, String mobileNo) {
        Student student = new Student();
        student.setStudentId(id);
        student.setUsername(username);
        student.setPassword(password);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setMobileNo(mobileNo);
        student.setCourses(new HashSet<>()); // Initialize courses as an empty set
        return student;
    }

    private Course createCourse(Long id, String courseName) {
        Course course = new Course();
        course.setCourseId(id);
        course.setCourseName(courseName);
        course.setStudents(new HashSet<>()); // Initialize students as an empty set
        return course;
    }

    @Test
    void testSaveStudent_Create() {
        Student student = new Student();
        student.setUsername("testUser");
        student.setPassword("Test@1234");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }



    @Test
    public void testSaveStudent_ValidationErrors() {
        Student student = createStudent(1L, "", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            studentService.saveStudent(student);
        });

        assertTrue(thrown.getErrors().containsValue("Username is required."));
    }

    @Test
    public void testUpdateStudent_Success() {
        Student existingStudent = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Jane");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        Student result = studentService.updateStudent(1L, updates);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
    }


    @Test
    public void testUpdateStudent_ValidationErrors() {
        Student existingStudent = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "invalid-email");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            studentService.updateStudent(1L, updates);
        });

        assertTrue(thrown.getErrors().containsValue("Invalid email format."));
    }


    @Test
    public void testDeleteStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testDeleteStudent_InvalidId() {
        Long invalidStudentId = -1L;

        // Expect a ValidationException to be thrown
        assertThrows(ValidationException.class, () -> {
            studentService.deleteStudent(invalidStudentId);
        });

        // Verify that the repository's deleteById method was not called
        verify(studentRepository, never()).deleteById(anyLong());
    }



    @Test
    public void testEnrollStudentInCourse_Success() {
        Student student = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Course course = createCourse(1L, "Math");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.save(course)).thenReturn(course);

        studentService.enrollStudentInCourse(1L, 1L);

        assertTrue(student.getCourses().contains(course));
        assertTrue(course.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testEnrollStudentInCourse_AlreadyEnrolled() {
        Student student = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Course course = createCourse(1L, "Math");
        student.getCourses().add(course);
        course.getStudents().add(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            studentService.enrollStudentInCourse(1L, 1L);
        });

        assertEquals("Student is already enrolled in this course.", thrown.getMessage());
    }


    @Test
    public void testUnenrollStudentFromCourse_Success() {
        Student student = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Course course = createCourse(1L, "Math");
        student.getCourses().add(course);
        course.getStudents().add(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.save(course)).thenReturn(course);

        studentService.unenrollStudentFromCourse(1L, 1L);

        assertFalse(student.getCourses().contains(course));
        assertFalse(course.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testUnenrollStudentFromCourse_NotEnrolled() {
        Student student = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Course course = createCourse(1L, "Math");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            studentService.unenrollStudentFromCourse(1L, 1L);
        });

        assertEquals("Student is not enrolled in this course.", thrown.getMessage());
    }


    @Test
    public void testGetCoursesByStudentId_Success() {
        Student student = createStudent(1L, "john_doe", "Password1!", "John", "Doe", "john.doe@example.com", "1234567890");
        Course course = createCourse(1L, "Math");
        student.getCourses().add(course);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Course> courses = studentService.getCoursesByStudentId(1L);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertTrue(courses.contains(course));
    }

    @Test
    public void testGetCoursesByStudentId_NotFound() {
        // Mock the repository to return an empty Optional when the student is not found
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect a ValidationException to be thrown
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            studentService.getCoursesByStudentId(1L);
        });

        // Verify the exception message
        assertEquals("Invalid student ID: 1", thrown.getMessage());
    }

}






