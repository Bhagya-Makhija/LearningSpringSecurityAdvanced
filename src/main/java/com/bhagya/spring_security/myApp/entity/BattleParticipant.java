package com.bhagya.spring_security.myApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "battle_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BattleParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "battle_id")
    private Battle battle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int score;   // total score in battle
}
