package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelDto {
    private Long id;
    private String name;
    private String description;
    private String difficulty;
    private boolean enabled;
    private int orderIndex;
    private List<DefenderProfileDto> defenderProfiles;
    private List<AttackScenarioDto> attackScenarios;
}
