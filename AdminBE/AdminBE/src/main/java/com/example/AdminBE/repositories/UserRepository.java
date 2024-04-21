package com.example.AdminBE.repositories;

import com.example.AdminBE.entities.User;

import com.example.AdminBE.models.dtos.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
        boolean existsByPhoneNumber(String phoneNumber);
        Optional<User> findByPhoneNumber(String phoneNumber);
}

