package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStateDto {
    private String roomId;
    private String status;
    private String gamePhase;
    
    private int attackerScore;
    private int defenderScore;
    
    private boolean isAttackerTurn;
    private int currentRound;
    private int maxRounds;
    
    private String lastActionMessage;
    private String lastOutcome;
    
    // Current level info
    private String currentLevelName;
    
    // Defender profile info (visible to attacker for strategy)
    private String defenderProfileName;
    private String defenderProfileDescription;
    private int defenderAge;
    private String defenderOccupation;
    private List<String> defenderRelationships;
    private List<String> defenderVulnerabilities;
    
    // Current attack info (for defender)
    private String currentAttackMessage;
    private String impersonatedEntity;
}
