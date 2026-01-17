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

    private String currentVideoId; // Legacy - kept for compatibility

    // New level-based game state
    private Long currentLevelId;
    private Long currentDefenderProfileId;
    private Long currentAttackScenarioId;
    private Long currentAttackOptionId;

    // Game phase tracking
    @Enumerated(EnumType.STRING)
    private GamePhase gamePhase = GamePhase.LEVEL_SELECT;

    // Turn tracking
    private boolean isAttackerTurn = true;

    // Round tracking
    private int currentRound = 1;
    private int maxRounds = 3;

    // Last action info for display
    @Column(length = 1000)
    private String lastActionMessage;

    @Column(length = 2000)
    private String lastOutcome;

    public enum RoomStatus {
        WAITING, // Waiting for 2nd player
        ATTACK_SELECTION, // Attacker choosing attack (legacy)
        DEFENDER_TURN, // Defender making choices (legacy)
        PLAYING, // Game in progress
        ROUND_OVER // Round completed
    }

    public enum GamePhase {
        LEVEL_SELECT, // Attacker selects a level
        PROFILE_SELECT, // Shows defender profile info
        ATTACK_TYPE_SELECT, // Attacker selects attack type
        ATTACK_OPTION_SELECT, // Attacker selects specific attack option
        DEFENDER_RESPONSE, // Defender responds to attack
        OUTCOME_DISPLAY, // Show outcome of the round
        GAME_OVER // Game finished
    }
}
