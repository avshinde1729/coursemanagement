package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.ValidationException;
import com.example.coursemanagement.model.Admin;
import com.example.coursemanagement.repository.AdminRepository;
import com.example.coursemanagement.service.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a sample admin
    private Admin createAdmin(Long id, String username, String password) {
        Admin admin = new Admin();
        admin.setAdminId(id);
        admin.setUsername(username);
        admin.setPassword(password);
        return admin;
    }


    @Test
    public void testGetAllAdmins() {
        Admin admin1 = createAdmin(1L, "user1", "Password1!");
        Admin admin2 = createAdmin(2L, "user2", "Password2!");

        when(adminRepository.findAll()).thenReturn(List.of(admin1, admin2));

        List<Admin> admins = adminService.getAllAdmins();

        assertNotNull(admins);
        assertEquals(2, admins.size());
        assertEquals("user1", admins.get(0).getUsername());
        assertEquals("user2", admins.get(1).getUsername());
    }

    @Test
    public void testGetAdminById_ValidId() {
        Admin admin = createAdmin(1L, "user1", "Password1!");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin foundAdmin = adminService.getAdminById(1L);

        assertNotNull(foundAdmin);
        assertEquals("user1", foundAdmin.getUsername());
    }

    @Test
    public void testGetAdminById_InvalidId() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.getAdminById(null);
        });
        assertEquals("Invalid admin ID: null", thrown.getMessage());

        thrown = assertThrows(ValidationException.class, () -> {
            adminService.getAdminById(-1L);
        });
        assertEquals("Invalid admin ID: -1", thrown.getMessage());
    }

    @Test
    public void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.getAdminById(1L);
        });
        assertEquals("Admin not found with ID: 1", thrown.getMessage());
    }

    @Test
    public void testSaveAdmin_ValidAdmin() {
        Admin admin = createAdmin(1L, "user1", "Password1!");

        when(adminRepository.findByUsername("user1")).thenReturn(null);
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin savedAdmin = adminService.saveAdmin(admin);

        assertNotNull(savedAdmin);
        assertEquals("user1", savedAdmin.getUsername());
    }

    @Test
    public void testSaveAdmin_ValidationErrors() {
        Admin admin = createAdmin(1L, "", "Password1!");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.saveAdmin(admin);
        });
        assertTrue(thrown.getErrors().containsValue("Username is mandatory"));
    }

    @Test
    public void testSaveAdmin_UsernameExists() {
        Admin existingAdmin = createAdmin(1L, "user1", "Password1!");
        Admin newAdmin = createAdmin(2L, "user1", "Password2!");

        when(adminRepository.findByUsername("user1")).thenReturn(existingAdmin);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.saveAdmin(newAdmin);
        });
        assertEquals("Username already exists", thrown.getMessage());
    }

    @Test
    public void testDeleteAdmin_ValidId() {
        when(adminRepository.existsById(1L)).thenReturn(true);

        adminService.deleteAdmin(1L);

        verify(adminRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAdmin_InvalidId() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.deleteAdmin(null);
        });
        assertEquals("Invalid admin ID: null", thrown.getMessage());

        thrown = assertThrows(ValidationException.class, () -> {
            adminService.deleteAdmin(-1L);
        });
        assertEquals("Invalid admin ID: -1", thrown.getMessage());
    }

    @Test
    public void testDeleteAdmin_NotFound() {
        when(adminRepository.existsById(1L)).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.deleteAdmin(1L);
        });
        assertEquals("Admin not found with ID: 1", thrown.getMessage());
    }


    @Test
    public void testUpdateAdmin_ValidAdmin() {
        Admin existingAdmin = createAdmin(1L, "user1", "Password1!");
        Admin updatedAdmin = createAdmin(1L, "user2", "NewPassword1!");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.findByUsername("user2")).thenReturn(null);
        when(adminRepository.save(existingAdmin)).thenReturn(existingAdmin);

        Admin result = adminService.updateAdmin(1L, updatedAdmin);

        assertNotNull(result);
        assertEquals("user2", result.getUsername());
        assertEquals("NewPassword1!", result.getPassword());
    }

    @Test
    public void testUpdateAdmin_InvalidId() {
        Admin admin = createAdmin(1L, "user1", "Password1!");

        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.updateAdmin(1L, admin);
        });
        assertEquals("Insert valid adminId", thrown.getMessage());
    }

    @Test
    public void testUpdateAdmin_UsernameExists() {
        Admin existingAdmin = createAdmin(1L, "user1", "Password1!");
        Admin updateAdmin = createAdmin(1L, "user2", "NewPassword1!");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.findByUsername("user2")).thenReturn(createAdmin(2L, "user2", "OtherPassword!"));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.updateAdmin(1L, updateAdmin);
        });
        assertEquals("Username already exists", thrown.getMessage());
    }

    @Test
    public void testUpdateAdmin_InvalidPassword() {
        Admin existingAdmin = createAdmin(1L, "user1", "Password1!");
        Admin updateAdmin = createAdmin(1L, "user1", "short");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            adminService.updateAdmin(1L, updateAdmin);
        });
        assertEquals("Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character", thrown.getMessage());
    }







}
