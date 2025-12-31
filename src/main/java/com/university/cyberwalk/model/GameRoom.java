package com.university.cyberwalk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomId;

    private String attackerSessionId; // Session ID of the attacker
    private String defenderSessionId; // Session ID of the defender

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.WAITING;

    @Enumerated(EnumType.STRING)
    private AttackType selectedAttackType;

    private int attackerScore = 0;
    private int defenderScore = 0;

    private String currentVideoId; // Synced video state

    public enum RoomStatus {
        WAITING, // Waiting for 2nd player
        ATTACK_SELECTION, // Attacker choosing attack
        DEFENDER_TURN, // Defender making choices
        ROUND_OVER // Round completed
    }
}
