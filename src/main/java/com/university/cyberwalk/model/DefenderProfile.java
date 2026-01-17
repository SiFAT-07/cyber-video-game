package com.university.cyberwalk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defender_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefenderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., "University Student", "Office Worker", "Elderly Person"

    @Column(length = 1000)
    private String description;

    // Environmental Factors
    private int age;
    private String ageGroup; // YOUNG, MIDDLE_AGED, ELDERLY

    @Column(length = 500)
    private String occupation; // Student, Professional, Retired, etc.

    private String techSavviness; // LOW, MEDIUM, HIGH

    private String mentalState; // CALM, STRESSED, ANXIOUS, DISTRACTED

    private String financialStatus; // STABLE, STRUGGLING, WEALTHY

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "defender_relationships", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "relationship")
    private List<String> relationships = new ArrayList<>(); // mother, father, partner, boss, bank, etc.

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "defender_vulnerabilities", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "vulnerability")
    private List<String> vulnerabilities = new ArrayList<>(); // trusting, impulsive, lonely, greedy, etc.

    @ManyToOne
    @JoinColumn(name = "level_id")
    @JsonIgnore
    private Level level;

    // Display icon/avatar for the profile
    private String avatarIcon;
}
