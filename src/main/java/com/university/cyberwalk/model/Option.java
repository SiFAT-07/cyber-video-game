package com.university.cyberwalk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label; // Display text for the option

    @Column(nullable = false)
    private String targetVideoId; // The video ID this option leads to

    // Scoring Constants
    public static final int SCORE_TRIPLE_PLUS = 30;
    public static final int SCORE_DOUBLE_PLUS = 20;
    public static final int SCORE_PLUS = 10;
    public static final int SCORE_MINUS = -10;
    public static final int SCORE_DOUBLE_MINUS = -20;
    public static final int SCORE_TRIPLE_MINUS = -30;
    public static final int SCORE_FAIL = -999;

    @Column(nullable = false)
    private int defenderScoreDelta; // Points change for Defender

    @Column(nullable = false)
    private int attackerScoreDelta; // Points change for Attacker

    @Column(nullable = false)
    private String position; // "bottom-left" or "bottom-right" or custom coordinates

    @Column(nullable = false)
    private String interactionType; // "click", "hotspot", "drag", "keyboard"

    private Double appearTime; // Seconds from video start when this option appears

    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    @JsonIgnore
    private Scenario scenario;
}
