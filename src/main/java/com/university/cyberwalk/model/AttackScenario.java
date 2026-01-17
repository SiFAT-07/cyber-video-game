package com.university.cyberwalk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attack_scenarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackScenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String attackType; // FAKE_CALL, FAKE_WIFI, PHISHING_EMAIL, FAKE_SMS, SOCIAL_ENGINEERING, etc.

    @Column(nullable = false)
    private String name; // e.g., "Impersonation Call", "Evil Twin WiFi"

    @Column(length = 2000)
    private String description;

    @Column(length = 1000)
    private String attackerNarrative; // What the attacker sees/thinks

    @ManyToOne
    @JoinColumn(name = "level_id")
    @JsonIgnore
    private Level level;

    @OneToMany(mappedBy = "attackScenario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AttackOption> attackOptions = new ArrayList<>();
}
