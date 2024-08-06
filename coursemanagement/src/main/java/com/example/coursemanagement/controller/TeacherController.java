package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.Teacher;
import com.example.coursemanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/all")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{teacherId}")
    public Teacher getTeacherById(@PathVariable Long teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @PostMapping("/add")
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.saveTeacher(teacher);
    }

    @PatchMapping("/update/{teacherId}")
    public Teacher updateTeacher(@PathVariable Long teacherId, @RequestBody Teacher teacher) {
        return teacherService.partialUpdateTeacher(teacherId, teacher);
    }

    @DeleteMapping("/{teacherId}")
    public void deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
    }

}
