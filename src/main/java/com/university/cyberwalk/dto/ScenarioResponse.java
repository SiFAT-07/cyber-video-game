package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioResponse {
    private String videoId;
    private String videoPath;
    private String description;
    private String attackerDescription;
    private String attackType;
    private boolean isLeafNode;
    private String nextScenarioId;
    private List<OptionDto> options;
}
