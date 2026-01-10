package com.university.cyberwalk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "scenarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String videoId; // e.g., "1", "1_1", "1_2"

    @Column(nullable = false)
    private String videoPath; // e.g., "/video/1.mp4"

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "attack_type")
    private AttackType attackType;

    @Column(length = 1000)
    private String attackerDescription; // Description shown to Attacker (e.g., "Real Call from IT")

    @Column(nullable = false)
    private boolean isLeafNode = false; // true if this scenario has no further options

    private String nextScenarioId; // The ID of the next scene after this branch/scene ends

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Option> options;
}
