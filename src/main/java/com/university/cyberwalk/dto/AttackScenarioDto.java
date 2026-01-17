package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackScenarioDto {
    private Long id;
    private String attackType;
    private String name;
    private String description;
    private String attackerNarrative;
    private List<AttackOptionDto> attackOptions;
}
