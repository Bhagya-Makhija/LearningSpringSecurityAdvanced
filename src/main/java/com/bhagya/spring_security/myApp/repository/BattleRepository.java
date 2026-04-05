package com.bhagya.spring_security.myApp.repository;

import com.bhagya.spring_security.myApp.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle,Long> {
}
