package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherServiceImplTest {

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a Teacher object

    private Teacher createTeacher(Long id) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(id);
        teacher.setUsername("username");
        teacher.setEmail("email@example.com");
        teacher.setPassword("Password1!");
        teacher.setFirstName("First");
        teacher.setLastName("Last");
        teacher.setMobileNo("1234567890");
        return teacher;
    }


    @Test
    public void testGetAllTeachers() {
        List<Teacher> teachers = Arrays.asList(createTeacher(1L), createTeacher(2L));
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getAllTeachers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testGetTeacherById_ValidId() {
        Teacher teacher = createTeacher(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTeacherId());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTeacherById_InvalidId() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            teacherService.getTeacherById(-1L);
        });

        assertTrue(thrown.getMessage().contains("Invalid teacher ID: -1"));
    }

    @Test
    public void testSaveTeacher_ValidCreate() {
        Teacher teacher = createTeacher(null);
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(teacherRepository.findByUsername(teacher.getUsername())).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.empty());

        Teacher result = teacherService.saveTeacher(teacher);

        assertNotNull(result);
        assertEquals(teacher.getUsername(), result.getUsername());
        verify(teacherRepository, times(1)).save(teacher);
    }

//    @Test
//    public void testSaveTeacher_UpdateExistingTeacher() {
//        Teacher existingTeacher = createTeacher(1L);
//
//        when(teacherRepository.existsById(1L)).thenReturn(true);
//        when(teacherRepository.save(any(Teacher.class))).thenReturn(existingTeacher);
//
//        Teacher result = teacherService.saveTeacher(existingTeacher);
//
//        assertNotNull(result);
//        assertEquals("john_doe", result.getUsername());
//        verify(teacherRepository, times(1)).save(existingTeacher);
//    }

    @Test
    public void testSaveTeacher_ValidationFailure() {
        Teacher existingTeacher = createTeacher(1L);

        existingTeacher.setUsername(""); // Invalid username

        assertThrows(ValidationException.class, () -> {
            teacherService.saveTeacher(existingTeacher);
        });

        verify(teacherRepository, never()).save(any(Teacher.class));
    }

//    @Test
//    public void testSaveTeacher_ValidationErrors() {
//        Teacher teacher = createTeacher(null);
//        teacher.setUsername(""); // Invalid username
//        when(teacherRepository.findByUsername(teacher.getUsername())).thenReturn(Optional.empty());
//        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.empty());
//
//        ValidationException thrown = assertThrows(ValidationException.class, () -> {
//            teacherService.saveTeacher(teacher);
//        });
//
//        assertTrue(thrown.getMessage().contains("Username is required."));
////        assertTrue(thrown.getMessage().contains("Email is required."));
////        assertTrue(thrown.getMessage().contains("Password is required."));
//    }


    @Test
    public void testDeleteTeacher_ValidId() {
        when(teacherRepository.existsById(1L)).thenReturn(true);

        teacherService.deleteTeacher(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTeacher_InvalidId() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            teacherService.deleteTeacher(-1L);
        });

        assertTrue(thrown.getMessage().contains("Invalid teacher ID: -1"));
    }

    @Test
    public void testDeleteTeacher_NonexistentTeacher() {
        when(teacherRepository.existsById(1L)).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            teacherService.deleteTeacher(1L);
        });

        assertTrue(thrown.getMessage().contains("Teacher not found with ID: 1"));
    }


    @Test
    public void testPartialUpdateTeacher_ValidUpdates() {
        Teacher existingTeacher = createTeacher(1L);
        Teacher updatedTeacher = createTeacher(1L);
        updatedTeacher.setUsername("new_username");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(existingTeacher));
        when(teacherRepository.findByUsername(updatedTeacher.getUsername())).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(updatedTeacher.getEmail())).thenReturn(Optional.empty());
        when(teacherRepository.save(existingTeacher)).thenReturn(existingTeacher);

        Teacher result = teacherService.partialUpdateTeacher(1L, updatedTeacher);

        assertNotNull(result);
        assertEquals("new_username", result.getUsername());
        verify(teacherRepository, times(1)).save(existingTeacher);
    }

    @Test
    public void testPartialUpdateTeacher_ValidationFailure() {
        Teacher existingTeacher = createTeacher(1L);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(existingTeacher));

        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setEmail("invalid email");

        assertThrows(ValidationException.class, () -> {
            teacherService.partialUpdateTeacher(1L, updatedTeacher);
        });

        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).save(any(Teacher.class));
    }


    @Test
    public void testPartialUpdateTeacher_ValidationErrors() {
        Teacher existingTeacher = createTeacher(1L);
        Teacher updatedTeacher = createTeacher(1L);
        updatedTeacher.setUsername("invalid"); // Assume this triggers a validation error

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(existingTeacher));
        when(teacherRepository.findByUsername(updatedTeacher.getUsername())).thenReturn(Optional.of(existingTeacher)); // Username already exists
        when(teacherRepository.findByEmail(updatedTeacher.getEmail())).thenReturn(Optional.empty());

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            teacherService.partialUpdateTeacher(1L, updatedTeacher);
        });

        assertTrue(thrown.getMessage().contains("Username already exists."));
    }




}
