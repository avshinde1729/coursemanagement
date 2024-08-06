
package com.example.coursemanagement.repository;

import com.example.coursemanagement.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);
}
