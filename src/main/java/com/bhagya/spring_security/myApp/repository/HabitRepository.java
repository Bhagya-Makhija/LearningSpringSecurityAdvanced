package com.bhagya.spring_security.myApp.repository;

import com.bhagya.spring_security.myApp.entity.Habit;
import com.bhagya.spring_security.myApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findByUser(User user);
}
