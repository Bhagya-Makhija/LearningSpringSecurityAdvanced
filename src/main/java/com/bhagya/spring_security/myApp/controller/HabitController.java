package com.bhagya.spring_security.myApp.controller;

import com.bhagya.spring_security.myApp.entity.Habit;
import com.bhagya.spring_security.myApp.entity.User;
import com.bhagya.spring_security.myApp.repository.HabitRepository;
import com.bhagya.spring_security.myApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        habit.setUser(user);
        habit.setCreatedAt(LocalDateTime.now());
        habit.setActive(true);

        return habitRepository.save(habit);
    }

    @GetMapping
    public List<Habit> getMyHabits(Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        return habitRepository.findByUser(user);
    }

    @GetMapping("/allHabits")
    public List<Habit> getAllHabits(Principal principal) {
        return habitRepository.findAll();
    }
}
