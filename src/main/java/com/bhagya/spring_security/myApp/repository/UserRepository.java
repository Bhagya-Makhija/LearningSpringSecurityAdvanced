package com.bhagya.spring_security.myApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhagya.spring_security.myApp.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);

}
