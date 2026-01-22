package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefenderChoiceDto {
    private Long id;
    private String label;
    private String description;
    private String outcome;
    private int defenderScoreDelta;
    private int attackerScoreDelta;
    private String choiceType;
    private String educationalNote;
}
