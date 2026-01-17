package com.university.cyberwalk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "defender_choices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefenderChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label; // e.g., "Trust the caller", "Hang up and verify", "Ask for callback number"

    @Column(length = 2000)
    private String description; // Detailed description of what happens

    @Column(length = 2000)
    private String outcome; // What happens as a result of this choice

    // Scoring
    private int defenderScoreDelta; // Points change for defender
    private int attackerScoreDelta; // Points change for attacker

    // Choice categorization
    private String choiceType; // CORRECT, WRONG, RISKY, NEUTRAL

    private boolean isCriticallyWrong = false; // Catastrophic mistake
    private boolean isCriticallyRight = false; // Perfect response

    @Column(length = 1000)
    private String educationalNote; // Teaching moment - explains why this is good/bad

    // Follow-up scenario (optional)
    private Long followUpAttackOptionId; // If this leads to another stage

    private boolean endsScenario = false; // If true, this choice ends the current scenario

    @ManyToOne
    @JoinColumn(name = "attack_option_id")
    @JsonIgnore
    private AttackOption attackOption;
}
