package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.*;
import com.university.cyberwalk.model.GameRoom;
import com.university.cyberwalk.service.GamePlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GamePlayController {

    @Autowired
    private GamePlayService gamePlayService;

    // ========== GAME STATE ENDPOINTS ==========

    @GetMapping("/{roomId}/state")
    public ResponseEntity<GameStateDto> getGameState(@PathVariable String roomId) {
        return ResponseEntity.ok(gamePlayService.getGameState(roomId));
    }

    @PostMapping("/{roomId}/start")
    public ResponseEntity<GameRoom> startNewGame(@PathVariable String roomId) {
        return ResponseEntity.ok(gamePlayService.startNewGame(roomId));
    }

    // ========== ATTACKER ACTIONS ==========

    @PostMapping("/{roomId}/select-level")
    public ResponseEntity<GameRoom> selectLevel(
            @PathVariable String roomId,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(gamePlayService.selectLevel(roomId, body.get("levelId")));
    }

    @PostMapping("/{roomId}/select-profile")
    public ResponseEntity<GameRoom> selectDefenderProfile(
            @PathVariable String roomId,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(gamePlayService.selectDefenderProfile(roomId, body.get("profileId")));
    }

    @PostMapping("/{roomId}/select-attack-scenario")
    public ResponseEntity<GameRoom> selectAttackScenario(
            @PathVariable String roomId,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(gamePlayService.selectAttackScenario(roomId, body.get("scenarioId")));
    }

    @PostMapping("/{roomId}/select-attack-option")
    public ResponseEntity<GameRoom> selectAttackOption(
            @PathVariable String roomId,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(gamePlayService.selectAttackOption(roomId, body.get("optionId")));
    }

    // ========== DEFENDER ACTIONS ==========

    @PostMapping("/{roomId}/defender-choice")
    public ResponseEntity<GameRoom> makeDefenderChoice(
            @PathVariable String roomId,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(gamePlayService.makeDefenderChoice(roomId, body.get("choiceId")));
    }

    // ========== ROUND MANAGEMENT ==========

    @PostMapping("/{roomId}/next-round")
    public ResponseEntity<GameRoom> continueToNextRound(@PathVariable String roomId) {
        return ResponseEntity.ok(gamePlayService.continueToNextRound(roomId));
    }

    // ========== DATA QUERIES ==========

    @GetMapping("/levels")
    public ResponseEntity<List<LevelDto>> getAvailableLevels() {
        return ResponseEntity.ok(gamePlayService.getAvailableLevels());
    }

    @GetMapping("/levels/{levelId}/profiles")
    public ResponseEntity<List<DefenderProfileDto>> getDefenderProfiles(@PathVariable Long levelId) {
        return ResponseEntity.ok(gamePlayService.getDefenderProfiles(levelId));
    }

    @GetMapping("/levels/{levelId}/attack-scenarios")
    public ResponseEntity<List<AttackScenarioDto>> getAttackScenarios(@PathVariable Long levelId) {
        return ResponseEntity.ok(gamePlayService.getAttackScenarios(levelId));
    }

    @GetMapping("/attack-scenarios/{scenarioId}/options")
    public ResponseEntity<List<AttackOptionDto>> getAttackOptions(@PathVariable Long scenarioId) {
        return ResponseEntity.ok(gamePlayService.getAttackOptions(scenarioId));
    }

    @GetMapping("/attack-options/{optionId}/choices")
    public ResponseEntity<List<DefenderChoiceDto>> getDefenderChoices(@PathVariable Long optionId) {
        return ResponseEntity.ok(gamePlayService.getDefenderChoices(optionId));
    }
}
