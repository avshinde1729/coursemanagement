
package com.example.coursemanagement.service;

import com.example.coursemanagement.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long teacherId);

    Teacher saveTeacher(Teacher teacher);

    void deleteTeacher(Long teacherId);

    Teacher partialUpdateTeacher(Long teacherId, Teacher updatedTeacher);

}

