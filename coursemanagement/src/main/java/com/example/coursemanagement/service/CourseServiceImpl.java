package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherService teacherService;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new ValidationException("Invalid course ID: " + courseId);
        }
        return courseRepository.findById(courseId).orElse(null);
    }

    @Override
    public Course saveCourse(Course course) {
        validateCourse(course);
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new ValidationException("Invalid course ID: " + courseId);
        }
        if (!courseRepository.existsById(courseId)) {
            throw new ValidationException("Course not found with ID: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }

    public Course updateCourse(Long courseId, Map<String, Object> updates) {
        Course existingCourse = getCourseById(courseId);
        if (existingCourse == null) {
            throw new ValidationException("Course not found with ID: " + courseId);
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "courseName":
                    existingCourse.setCourseName((String) value);
                    break;
                case "description":
                    existingCourse.setDescription((String) value);
                    break;
                case "durationInMonth":
                    existingCourse.setDurationInMonth((String) value);
                    break;
                case "assignedDate":
                    existingCourse.setAssignedDate((Date) value);
                    break;
                case "endDate":
                    existingCourse.setEndDate((Date) value);
                    break;
                case "teacher":
                    Teacher teacher = teacherService.getTeacherById(((Teacher) value).getTeacherId());
                    if (teacher != null) {
                        existingCourse.setTeacher(teacher);
                    } else {
                        throw new ValidationException("Teacher not found with ID: " + ((Teacher) value).getTeacherId());
                    }
                    break;
                default:
                    throw new ValidationException("Invalid field: " + key);
            }
        });

        return saveCourse(existingCourse);
    }

    private void validateCourse(Course course) {
        if (course == null) {
            throw new ValidationException("Course cannot be null");
        }
        if (course.getCourseName() == null || course.getCourseName().isEmpty()) {
            throw new ValidationException("Course name cannot be null or empty");
        }
        if (course.getTeacher() == null || course.getTeacher().getTeacherId() == null) {
            throw new ValidationException("Teacher information is required");
        }

        Teacher teacher = teacherService.getTeacherById(course.getTeacher().getTeacherId());
        if (teacher == null) {
            throw new ValidationException("Teacher not found with ID: " + course.getTeacher().getTeacherId());
        }
        course.setTeacher(teacher);
    }
}
