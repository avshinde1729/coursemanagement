package com.example.coursemanagement.service;

import com.example.coursemanagement.model.Admin;

import java.util.List;

public interface AdminService {

    List<Admin> getAllAdmins();

    Admin getAdminById(Long adminId);

    Admin saveAdmin(Admin admin);

    void deleteAdmin(Long adminId);

    Admin updateAdmin(Long adminId, Admin admin);

}
