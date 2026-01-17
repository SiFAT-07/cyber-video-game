package com.university.cyberwalk.service;

import com.university.cyberwalk.dto.*;
import com.university.cyberwalk.model.*;
import com.university.cyberwalk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GamePlayService {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private DefenderProfileRepository defenderProfileRepository;

    @Autowired
    private AttackScenarioRepository attackScenarioRepository;

    @Autowired
    private AttackOptionRepository attackOptionRepository;

    @Autowired
    private DefenderChoiceRepository defenderChoiceRepository;

    // ========== GAME STATE MANAGEMENT ==========

    @Transactional
    public GameRoom selectLevel(String roomId, Long levelId) {
        GameRoom room = getRoom(roomId);
        validateAttackerTurn(room);

        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));

        room.setCurrentLevelId(levelId);
        room.setGamePhase(GameRoom.GamePhase.PROFILE_SELECT);
        room.setLastActionMessage("Level selected: " + level.getName());

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom selectDefenderProfile(String roomId, Long profileId) {
        GameRoom room = getRoom(roomId);

        DefenderProfile profile = defenderProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Defender Profile not found"));

        room.setCurrentDefenderProfileId(profileId);
        room.setGamePhase(GameRoom.GamePhase.ATTACK_TYPE_SELECT);
        room.setLastActionMessage("Target profile: " + profile.getName());

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom selectAttackScenario(String roomId, Long scenarioId) {
        GameRoom room = getRoom(roomId);
        validateAttackerTurn(room);

        AttackScenario scenario = attackScenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new RuntimeException("Attack Scenario not found"));

        room.setCurrentAttackScenarioId(scenarioId);
        room.setGamePhase(GameRoom.GamePhase.ATTACK_OPTION_SELECT);
        room.setLastActionMessage("Attack type selected: " + scenario.getName());

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom selectAttackOption(String roomId, Long optionId) {
        GameRoom room = getRoom(roomId);
        validateAttackerTurn(room);

        AttackOption option = attackOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Attack Option not found"));

        room.setCurrentAttackOptionId(optionId);
        room.setGamePhase(GameRoom.GamePhase.DEFENDER_RESPONSE);
        room.setAttackerTurn(false);
        room.setLastActionMessage("Attack launched: " + option.getLabel());

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom makeDefenderChoice(String roomId, Long choiceId) {
        GameRoom room = getRoom(roomId);
        validateDefenderTurn(room);

        DefenderChoice choice = defenderChoiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Defender Choice not found"));

        // Apply score changes
        room.setDefenderScore(room.getDefenderScore() + choice.getDefenderScoreDelta());
        room.setAttackerScore(room.getAttackerScore() + choice.getAttackerScoreDelta());

        // Store last score deltas for display
        room.setLastDefenderScoreDelta(choice.getDefenderScoreDelta());
        room.setLastAttackerScoreDelta(choice.getAttackerScoreDelta());

        // Set outcome message
        room.setLastOutcome(choice.getOutcome());
        room.setLastActionMessage("Defender chose: " + choice.getLabel());

        // Check if there's a follow-up or if this ends the scenario
        if (choice.isEndsScenario() || choice.getFollowUpAttackOptionId() == null) {
            // Move to next round or end game
            room.setCurrentRound(room.getCurrentRound() + 1);

            if (room.getCurrentRound() > room.getMaxRounds()) {
                room.setGamePhase(GameRoom.GamePhase.GAME_OVER);
                room.setStatus(GameRoom.RoomStatus.ROUND_OVER);
            } else {
                // Reset for next round
                room.setGamePhase(GameRoom.GamePhase.OUTCOME_DISPLAY);
                room.setCurrentAttackScenarioId(null);
                room.setCurrentAttackOptionId(null);
            }
        } else {
            // Continue to follow-up attack option
            room.setCurrentAttackOptionId(choice.getFollowUpAttackOptionId());
            room.setGamePhase(GameRoom.GamePhase.DEFENDER_RESPONSE);
        }

        room.setAttackerTurn(true);

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom continueToNextRound(String roomId) {
        GameRoom room = getRoom(roomId);

        if (room.getGamePhase() == GameRoom.GamePhase.OUTCOME_DISPLAY) {
            room.setGamePhase(GameRoom.GamePhase.ATTACK_TYPE_SELECT);
            room.setLastOutcome(null);
            room.setLastActionMessage("Round " + room.getCurrentRound() + " begins!");
        }

        return gameRoomRepository.save(room);
    }

    @Transactional
    public GameRoom startNewGame(String roomId) {
        GameRoom room = getRoom(roomId);

        room.setCurrentLevelId(null);
        room.setCurrentDefenderProfileId(null);
        room.setCurrentAttackScenarioId(null);
        room.setCurrentAttackOptionId(null);
        room.setGamePhase(GameRoom.GamePhase.LEVEL_SELECT);
        room.setStatus(GameRoom.RoomStatus.PLAYING);
        room.setAttackerTurn(true);
        room.setCurrentRound(1);
        room.setAttackerScore(0);
        room.setDefenderScore(0);
        room.setLastActionMessage("New game started!");
        room.setLastOutcome(null);

        return gameRoomRepository.save(room);
    }

    // ========== GAME STATE QUERIES ==========

    public GameStateDto getGameState(String roomId) {
        GameRoom room = getRoom(roomId);
        GameStateDto state = new GameStateDto();

        state.setRoomId(room.getRoomId());
        state.setAttackerScore(room.getAttackerScore());
        state.setDefenderScore(room.getDefenderScore());
        state.setGamePhase(room.getGamePhase().name());
        state.setAttackerTurn(room.isAttackerTurn());
        state.setCurrentRound(room.getCurrentRound());
        state.setMaxRounds(room.getMaxRounds());
        state.setLastActionMessage(room.getLastActionMessage());
        state.setLastOutcome(room.getLastOutcome());
        state.setStatus(room.getStatus().name());

        // Load current level info
        if (room.getCurrentLevelId() != null) {
            levelRepository.findById(room.getCurrentLevelId())
                    .ifPresent(level -> state.setCurrentLevelName(level.getName()));
        }

        // Load defender profile info
        if (room.getCurrentDefenderProfileId() != null) {
            defenderProfileRepository.findById(room.getCurrentDefenderProfileId())
                    .ifPresent(profile -> {
                        state.setDefenderProfileName(profile.getName());
                        state.setDefenderProfileDescription(profile.getDescription());
                        state.setDefenderAge(profile.getAge());
                        state.setDefenderOccupation(profile.getOccupation());
                        state.setDefenderRelationships(profile.getRelationships());
                        state.setDefenderVulnerabilities(profile.getVulnerabilities());
                    });
        }

        // Load current attack info
        if (room.getCurrentAttackOptionId() != null) {
            attackOptionRepository.findById(room.getCurrentAttackOptionId())
                    .ifPresent(option -> {
                        state.setCurrentAttackMessage(option.getAttackerMessage());
                        state.setImpersonatedEntity(option.getImpersonatedEntity());
                    });
        }

        return state;
    }

    public List<LevelDto> getAvailableLevels() {
        return levelRepository.findByEnabledTrueOrderByOrderIndexAsc()
                .stream()
                .map(this::convertToLevelDto)
                .collect(Collectors.toList());
    }

    public List<DefenderProfileDto> getDefenderProfiles(Long levelId) {
        return defenderProfileRepository.findByLevelId(levelId)
                .stream()
                .map(this::convertToDefenderProfileDto)
                .collect(Collectors.toList());
    }

    public List<AttackScenarioDto> getAttackScenarios(Long levelId) {
        return attackScenarioRepository.findByLevelId(levelId)
                .stream()
                .map(this::convertToAttackScenarioDto)
                .collect(Collectors.toList());
    }

    public List<AttackOptionDto> getAttackOptions(Long scenarioId) {
        return attackOptionRepository.findByAttackScenarioId(scenarioId)
                .stream()
                .map(this::convertToAttackOptionDto)
                .collect(Collectors.toList());
    }

    public List<DefenderChoiceDto> getDefenderChoices(Long attackOptionId) {
        return defenderChoiceRepository.findByAttackOptionId(attackOptionId)
                .stream()
                .map(this::convertToDefenderChoiceDto)
                .collect(Collectors.toList());
    }

    // ========== HELPER METHODS ==========

    private GameRoom getRoom(String roomId) {
        return gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    private void validateAttackerTurn(GameRoom room) {
        if (!room.isAttackerTurn()) {
            throw new RuntimeException("Not attacker's turn");
        }
    }

    private void validateDefenderTurn(GameRoom room) {
        if (room.isAttackerTurn()) {
            throw new RuntimeException("Not defender's turn");
        }
    }

    // Conversion methods
    private LevelDto convertToLevelDto(Level level) {
        LevelDto dto = new LevelDto();
        dto.setId(level.getId());
        dto.setName(level.getName());
        dto.setDescription(level.getDescription());
        dto.setDifficulty(level.getDifficulty());
        dto.setEnabled(level.isEnabled());
        dto.setOrderIndex(level.getOrderIndex());
        return dto;
    }

    private DefenderProfileDto convertToDefenderProfileDto(DefenderProfile profile) {
        DefenderProfileDto dto = new DefenderProfileDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setDescription(profile.getDescription());
        dto.setAge(profile.getAge());
        dto.setAgeGroup(profile.getAgeGroup());
        dto.setOccupation(profile.getOccupation());
        dto.setTechSavviness(profile.getTechSavviness());
        dto.setMentalState(profile.getMentalState());
        dto.setFinancialStatus(profile.getFinancialStatus());
        dto.setRelationships(profile.getRelationships());
        dto.setVulnerabilities(profile.getVulnerabilities());
        dto.setAvatarIcon(profile.getAvatarIcon());
        return dto;
    }

    private AttackScenarioDto convertToAttackScenarioDto(AttackScenario scenario) {
        AttackScenarioDto dto = new AttackScenarioDto();
        dto.setId(scenario.getId());
        dto.setAttackType(scenario.getAttackType());
        dto.setName(scenario.getName());
        dto.setDescription(scenario.getDescription());
        dto.setAttackerNarrative(scenario.getAttackerNarrative());
        return dto;
    }

    private AttackOptionDto convertToAttackOptionDto(AttackOption option) {
        AttackOptionDto dto = new AttackOptionDto();
        dto.setId(option.getId());
        dto.setLabel(option.getLabel());
        dto.setDescription(option.getDescription());
        dto.setAttackerMessage(option.getAttackerMessage());
        dto.setImpersonatedEntity(option.getImpersonatedEntity());
        dto.setBaseAttackerPoints(option.getBaseAttackerPoints());
        dto.setRiskLevel(option.getRiskLevel());
        dto.setCriticalRisk(option.isCriticalRisk());
        return dto;
    }

    private DefenderChoiceDto convertToDefenderChoiceDto(DefenderChoice choice) {
        DefenderChoiceDto dto = new DefenderChoiceDto();
        dto.setId(choice.getId());
        dto.setLabel(choice.getLabel());
        dto.setDescription(choice.getDescription());
        dto.setOutcome(choice.getOutcome());
        dto.setDefenderScoreDelta(choice.getDefenderScoreDelta());
        dto.setAttackerScoreDelta(choice.getAttackerScoreDelta());
        dto.setChoiceType(choice.getChoiceType());
        dto.setCriticallyWrong(choice.isCriticallyWrong());
        dto.setCriticallyRight(choice.isCriticallyRight());
        dto.setEducationalNote(choice.getEducationalNote());
        dto.setFollowUpAttackOptionId(choice.getFollowUpAttackOptionId());
        dto.setEndsScenario(choice.isEndsScenario());
        return dto;
    }
}
