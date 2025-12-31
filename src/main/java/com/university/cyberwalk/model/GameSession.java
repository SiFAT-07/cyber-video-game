package com.university.cyberwalk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String sessionId; // Unique identifier for the session
    
    @Column(nullable = false)
    private int currentScore = 0;
    
    @Column(nullable = false)
    private String currentVideoId = "1"; // Start with video "1"
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime lastUpdated;
    
    @Column
    private boolean isCompleted = false;
}
