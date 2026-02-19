package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.*;
import com.university.cyberwalk.service.LevelEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
@CrossOrigin(origins = "*")
public class LevelEditorController {

    @Autowired
    private LevelEditorService levelEditorService;

    // ========== LEVEL ENDPOINTS ==========

    @GetMapping
    public ResponseEntity<List<LevelDto>> getAllLevels() {
        return ResponseEntity.ok(levelEditorService.getAllLevels());
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<LevelDto>> getEnabledLevels() {
        return ResponseEntity.ok(levelEditorService.getEnabledLevels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelDto> getLevelById(@PathVariable Long id) {
        return ResponseEntity.ok(levelEditorService.getLevelById(id));
    }

    @PostMapping
    public ResponseEntity<LevelDto> createLevel(@RequestBody LevelDto dto) {
        return ResponseEntity.ok(levelEditorService.createLevel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LevelDto> updateLevel(@PathVariable Long id, @RequestBody LevelDto dto) {
        return ResponseEntity.ok(levelEditorService.updateLevel(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelEditorService.deleteLevel(id);
        return ResponseEntity.ok().build();
    }

    // ========== DEFENDER PROFILE ENDPOINTS ==========

    @PostMapping("/{levelId}/defender-profiles")
    public ResponseEntity<DefenderProfileDto> createDefenderProfile(
            @PathVariable Long levelId,
            @RequestBody DefenderProfileDto dto) {
        return ResponseEntity.ok(levelEditorService.createDefenderProfile(levelId, dto));
    }

    @PutMapping("/defender-profiles/{id}")
    public ResponseEntity<DefenderProfileDto> updateDefenderProfile(
            @PathVariable Long id,
            @RequestBody DefenderProfileDto dto) {
        return ResponseEntity.ok(levelEditorService.updateDefenderProfile(id, dto));
    }

    @DeleteMapping("/defender-profiles/{id}")
    public ResponseEntity<Void> deleteDefenderProfile(@PathVariable Long id) {
        levelEditorService.deleteDefenderProfile(id);
        return ResponseEntity.ok().build();
    }

    // ========== ATTACK SCENARIO ENDPOINTS ==========

    @PostMapping("/{levelId}/attack-scenarios")
    public ResponseEntity<AttackScenarioDto> createAttackScenario(
            @PathVariable Long levelId,
            @RequestBody AttackScenarioDto dto) {
        return ResponseEntity.ok(levelEditorService.createAttackScenario(levelId, dto));
    }

    @PutMapping("/attack-scenarios/{id}")
    public ResponseEntity<AttackScenarioDto> updateAttackScenario(
            @PathVariable Long id,
            @RequestBody AttackScenarioDto dto) {
        return ResponseEntity.ok(levelEditorService.updateAttackScenario(id, dto));
    }

    @DeleteMapping("/attack-scenarios/{id}")
    public ResponseEntity<Void> deleteAttackScenario(@PathVariable Long id) {
        levelEditorService.deleteAttackScenario(id);
        return ResponseEntity.ok().build();
    }

    // ========== ATTACK OPTION ENDPOINTS ==========

    @PostMapping("/attack-scenarios/{scenarioId}/options")
    public ResponseEntity<AttackOptionDto> createAttackOption(
            @PathVariable Long scenarioId,
            @RequestBody AttackOptionDto dto) {
        return ResponseEntity.ok(levelEditorService.createAttackOption(scenarioId, dto));
    }

    @PutMapping("/attack-options/{id}")
    public ResponseEntity<AttackOptionDto> updateAttackOption(
            @PathVariable Long id,
            @RequestBody AttackOptionDto dto) {
        return ResponseEntity.ok(levelEditorService.updateAttackOption(id, dto));
    }

    @DeleteMapping("/attack-options/{id}")
    public ResponseEntity<Void> deleteAttackOption(@PathVariable Long id) {
        levelEditorService.deleteAttackOption(id);
        return ResponseEntity.ok().build();
    }

    // ========== DEFENDER CHOICE ENDPOINTS ==========

    @PostMapping("/attack-options/{attackOptionId}/choices")
    public ResponseEntity<DefenderChoiceDto> createDefenderChoice(
            @PathVariable Long attackOptionId,
            @RequestBody DefenderChoiceDto dto) {
        return ResponseEntity.ok(levelEditorService.createDefenderChoice(attackOptionId, dto));
    }

    @PutMapping("/defender-choices/{id}")
    public ResponseEntity<DefenderChoiceDto> updateDefenderChoice(
            @PathVariable Long id,
            @RequestBody DefenderChoiceDto dto) {
        return ResponseEntity.ok(levelEditorService.updateDefenderChoice(id, dto));
    }

    @DeleteMapping("/defender-choices/{id}")
    public ResponseEntity<Void> deleteDefenderChoice(@PathVariable Long id) {
        levelEditorService.deleteDefenderChoice(id);
        return ResponseEntity.ok().build();
    }
}
