package com.university.cyberwalk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attack_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label; // e.g., "Call as Mother", "Call as Bank Representative"

    @Column(length = 1000)
    private String description; // What this attack option does

    @Column(length = 2000)
    private String attackerMessage; // What the attacker says/sends to the defender

    @Column(length = 500)
    private String impersonatedEntity; // Who/what the attacker is pretending to be

    // Risk/Reward for attacker choosing this option
    private int baseAttackerPoints = 0; // Points attacker gets if defender falls for it
    private int riskLevel = 1; // 1-5, higher = riskier but potentially more rewarding

    private boolean isCriticalRisk = false; // If true, can lead to big win or big loss

    @ManyToOne
    @JoinColumn(name = "attack_scenario_id")
    @JsonIgnore
    private AttackScenario attackScenario;

    @OneToMany(mappedBy = "attackOption", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DefenderChoice> defenderChoices = new ArrayList<>();
}
