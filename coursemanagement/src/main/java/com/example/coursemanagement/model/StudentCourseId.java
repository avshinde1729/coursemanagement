//package com.example.coursemanagement.model;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class StudentCourseId implements Serializable {
//
//    private Long student;
//    private Long course;
//
//    // Getters and setters
//    public Long getStudent() {
//        return student;
//    }
//
//    public void setStudent(Long student) {
//        this.student = student;
//    }
//
//    public Long getCourse() {
//        return course;
//    }
//
//    public void setCourse(Long course) {
//        this.course = course;
//    }
//
//    // equals and hashCode methods (generated automatically by IDE or manually implemented)
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        StudentCourseId that = (StudentCourseId) o;
//        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(student, course);
//    }
//}
