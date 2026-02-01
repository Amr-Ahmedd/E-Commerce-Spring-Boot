package com.project.ecommerce.repository;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findFirstByEmail(String email);
    User findByRole(UserRole userRole);
}
