package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackOptionDto {
    private Long id;
    private String label;
    private String description;
    private String attackerMessage;
    private String impersonatedEntity;
    private int baseAttackerPoints;
    private int riskLevel;
    private boolean isCriticalRisk;
    private List<DefenderChoiceDto> defenderChoices;
}
