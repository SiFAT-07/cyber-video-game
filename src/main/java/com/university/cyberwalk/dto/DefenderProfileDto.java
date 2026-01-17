package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefenderProfileDto {
    private Long id;
    private String name;
    private String description;
    private int age;
    private String ageGroup;
    private String occupation;
    private String techSavviness;
    private String mentalState;
    private String financialStatus;
    private List<String> relationships;
    private List<String> vulnerabilities;
    private String avatarIcon;
}
