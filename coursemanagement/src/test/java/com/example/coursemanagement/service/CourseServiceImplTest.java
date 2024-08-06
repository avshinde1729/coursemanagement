
package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = new Teacher();
        teacher.setTeacherId(1L);

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Test Course");
        course.setTeacher(teacher);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = Collections.singletonList(course);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Course", result.get(0).getCourseName());
    }

    @Test
    void testGetCourseById_ValidId() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Test Course", result.getCourseName());
    }

    @Test
    void testGetCourseById_InvalidId() {
        assertThrows(ValidationException.class, () -> courseService.getCourseById(-1L));
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Course result = courseService.getCourseById(1L);

        assertNull(result);
    }

    @Test
    void testSaveCourse_Valid() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(teacherService.getTeacherById(anyLong())).thenReturn(teacher);

        Course result = courseService.saveCourse(course);

        assertNotNull(result);
        assertEquals("Test Course", result.getCourseName());
    }

    @Test
    void testSaveCourse_Invalid() {
        course.setCourseName(null);
        assertThrows(ValidationException.class, () -> courseService.saveCourse(course));
    }

    @Test
    void testDeleteCourse_ValidId() {
        when(courseRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(courseRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourse_InvalidId() {
        assertThrows(ValidationException.class, () -> courseService.deleteCourse(-1L));
    }

    @Test
    void testDeleteCourse_NotFound() {
        when(courseRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidationException.class, () -> courseService.deleteCourse(1L));
    }

    @Test
    void testUpdateCourse_Valid() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(teacherService.getTeacherById(anyLong())).thenReturn(teacher);

        Map<String, Object> updates = new HashMap<>();
        updates.put("courseName", "Updated Course");

        Course result = courseService.updateCourse(1L, updates);

        assertNotNull(result);
        assertEquals("Updated Course", result.getCourseName());
    }

    @Test
    void testUpdateCourse_InvalidField() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Map<String, Object> updates = new HashMap<>();
        updates.put("invalidField", "Invalid");

        assertThrows(ValidationException.class, () -> courseService.updateCourse(1L, updates));
    }

    @Test
    void testUpdateCourse_NotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Map<String, Object> updates = new HashMap<>();
        updates.put("courseName", "Updated Course");

        assertThrows(ValidationException.class, () -> courseService.updateCourse(1L, updates));
    }
}