package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Admin;
import com.example.coursemanagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(Long adminId) {
        if (adminId == null || adminId <= 0) {
            throw new ValidationException("Invalid admin ID: " + adminId);
        }
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + adminId));
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        List<String> errors = validateAdmin(admin);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Admin existingAdmin = adminRepository.findByUsername(admin.getUsername());
        if (existingAdmin != null && !existingAdmin.getAdminId().equals(admin.getAdminId())) {
            throw new ValidationException("Username already exists");
        }
        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(Long adminId) {
        if (adminId == null || adminId <= 0) {
            throw new ValidationException("Invalid admin ID: " + adminId);
        }
        if (!adminRepository.existsById(adminId)) {
            throw new ValidationException("Admin not found with ID: " + adminId);
        }
        adminRepository.deleteById(adminId);
    }

    @Override
    public Admin updateAdmin(Long adminId, Admin admin) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ValidationException("Insert valid adminId"));

        if (admin.getUsername() != null) {
            Admin existingUsernameAdmin = adminRepository.findByUsername(admin.getUsername());
            if (existingUsernameAdmin != null && !existingUsernameAdmin.getAdminId().equals(adminId)) {
                throw new ValidationException("Username already exists");
            }
            existingAdmin.setUsername(admin.getUsername());
        }
        if (admin.getPassword() != null) {
            if (!isValidPassword(admin.getPassword())) {
                throw new ValidationException("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character");
            }
            existingAdmin.setPassword(admin.getPassword());
        }

        List<String> errors = validateAdmin(existingAdmin);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return adminRepository.save(existingAdmin);
    }

    private List<String> validateAdmin(Admin admin) {
        List<String> errors = new ArrayList<>();

        if (admin == null) {
            errors.add("Admin cannot be null");
        }

        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            errors.add("Username is mandatory");
        } else if (admin.getUsername().length() < 3 || admin.getUsername().length() > 50) {
            errors.add("Username must be between 3 and 50 characters");
        }

        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            errors.add("Password is mandatory");
        } else if (!isValidPassword(admin.getPassword())) {
            errors.add("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character");
        }

        return errors;
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
