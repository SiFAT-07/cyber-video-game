package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private Long id;
    private String label;
    private String targetVideoId;
    private int defenderScoreDelta;
    private int attackerScoreDelta;
    private String position;
    private String interactionType;
    private Double appearTime;
}
