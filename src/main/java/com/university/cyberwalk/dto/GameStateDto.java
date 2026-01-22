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

    private int attacksPerformed;
    private int maxAttacks;

    private String lastActionMessage;
    private String lastOutcome;

    // Score deltas from last action
    private Integer lastDefenderScoreDelta;
    private Integer lastAttackerScoreDelta;

    // Current level info
    private String currentLevelName;

    // Defender profile info (visible to both attacker and defender)
    private String defenderProfileName;
    private String defenderProfileDescription;
    private int defenderAge;
    private String defenderAgeGroup;
    private String defenderOccupation;
    private String defenderTechSavviness;
    private String defenderMentalState;
    private String defenderFinancialStatus;
    private String defenderAvatarIcon;

    // Current attack info (for defender)
    private String currentAttackMessage;
    private String impersonatedEntity;
}
