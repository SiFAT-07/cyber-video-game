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
    
    @Column(nullable = false)
    private int scoreChange; // Points gained/lost for choosing this option
    
    @Column(nullable = false)
    private String position; // "bottom-left" or "bottom-right" or custom coordinates
    
    @Column(nullable = false)
    private String interactionType; // "click", "hotspot", "drag", "keyboard"
    
    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    @JsonIgnore
    private Scenario scenario;
}
