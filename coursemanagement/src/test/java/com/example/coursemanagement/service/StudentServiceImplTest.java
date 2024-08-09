package com.example.coursemanagement.service;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Student;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.StudentRepository;
import com.example.coursemanagement.service.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student();
        student.setUsername("testUser");
        student.setPassword("Test@1234");
        student.setFirstName("yuvi");
        student.setLastName("dada");
        student.setEmail("yuvi.dada@example.com");
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Success() {
        Student student = new Student();
        student.setStudentId(1L);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        verify(studentRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveStudent_Create() {

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testSaveStudent_Update() {

        when(studentRepository.existsById(anyLong())).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent_Success() {
        doNothing().when(studentRepository).deleteById(anyLong());

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testEnrollStudentInCourse_Success() {
        Student student = new Student();
        student.setCourses(new HashSet<>());
        Course course = new Course();
        course.setStudents(new HashSet<>());

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        studentService.enrollStudentInCourse(1L, 1L);

        assertTrue(student.getCourses().contains(course));
        assertTrue(course.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUnenrollStudentFromCourse_Success() {
        Student student = new Student();
        Course course = new Course();
        student.setCourses(new HashSet<>(Collections.singleton(course)));
        course.setStudents(new HashSet<>(Collections.singleton(student)));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        studentService.unenrollStudentFromCourse(1L, 1L);

        assertFalse(student.getCourses().contains(course));
        assertFalse(course.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateStudent_Success() {
        Student existingStudent = new Student();
        existingStudent.setStudentId(1L);
        existingStudent.setUsername("existingUser");
        existingStudent.setCourses(new HashSet<>());
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", "newUser");

        Student result = studentService.updateStudent(1L, updates);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    void testGetCoursesByStudentId() {
        Student student = new Student();
        Course course1 = new Course();
        course1.setCourseId(1L);
        course1.setCourseName("Mathematics");

        Course course2 = new Course();
        course2.setCourseId(2L);
        course2.setCourseName("Science");

        student.setCourses(new HashSet<>(Arrays.asList(course1, course2)));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        List<Course> courses = studentService.getCoursesByStudentId(1L);

        assertNotNull(courses);
        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(course -> course.getCourseName().equals("Mathematics")));
        assertTrue(courses.stream().anyMatch(course -> course.getCourseName().equals("Science")));
        verify(studentRepository, times(1)).findById(anyLong());
    }
}