package com.bhagya.spring_security.myApp.controller;

import com.bhagya.spring_security.myApp.entity.Battle;
import com.bhagya.spring_security.myApp.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/battles")
@RequiredArgsConstructor
public class BattleController {

    private final BattleRepository battleRepository;

    @GetMapping
    public List<Battle> getBattles() {
        return battleRepository.findAll();
    }
}
