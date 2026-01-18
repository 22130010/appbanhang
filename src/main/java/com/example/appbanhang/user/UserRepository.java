package com.example.appbanhang.user;
import org.springframework.data.jpa.repository.JpaRepository; 


public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
}