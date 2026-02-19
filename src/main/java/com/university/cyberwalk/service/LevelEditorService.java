package com.university.cyberwalk.service;

import com.university.cyberwalk.dto.*;
import com.university.cyberwalk.model.*;
import com.university.cyberwalk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelEditorService {

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

    // ========== LEVEL OPERATIONS ==========

    public List<LevelDto> getAllLevels() {
        return levelRepository.findAllByOrderByOrderIndexAsc()
                .stream()
                .map(this::convertToLevelDto)
                .collect(Collectors.toList());
    }

    public List<LevelDto> getEnabledLevels() {
        return levelRepository.findByEnabledTrueOrderByOrderIndexAsc()
                .stream()
                .map(this::convertToLevelDto)
                .collect(Collectors.toList());
    }

    public LevelDto getLevelById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        return convertToLevelDto(level);
    }

    @Transactional
    public LevelDto createLevel(LevelDto dto) {
        Level level = new Level();
        level.setName(dto.getName());
        level.setDescription(dto.getDescription());
        level.setDifficulty(dto.getDifficulty());
        level.setEnabled(dto.isEnabled());
        level.setOrderIndex(dto.getOrderIndex());
        level.setMaxAttacks(dto.getMaxAttacks() > 0 ? dto.getMaxAttacks() : 5);

        Level savedLevel = levelRepository.save(level);
        return convertToLevelDto(savedLevel);
    }

    @Transactional
    public LevelDto updateLevel(Long id, LevelDto dto) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found"));

        level.setName(dto.getName());
        level.setDescription(dto.getDescription());
        level.setDifficulty(dto.getDifficulty());
        level.setEnabled(dto.isEnabled());
        level.setOrderIndex(dto.getOrderIndex());
        level.setMaxAttacks(dto.getMaxAttacks() > 0 ? dto.getMaxAttacks() : 5);

        Level savedLevel = levelRepository.save(level);
        return convertToLevelDto(savedLevel);
    }

    @Transactional
    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }

    // ========== DEFENDER PROFILE OPERATIONS ==========

    @Transactional
    public DefenderProfileDto createDefenderProfile(Long levelId, DefenderProfileDto dto) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));

        DefenderProfile profile = new DefenderProfile();
        updateDefenderProfileFromDto(profile, dto);
        profile.setLevel(level);

        DefenderProfile saved = defenderProfileRepository.save(profile);
        return convertToDefenderProfileDto(saved);
    }

    @Transactional
    public DefenderProfileDto updateDefenderProfile(Long id, DefenderProfileDto dto) {
        DefenderProfile profile = defenderProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Defender Profile not found"));

        updateDefenderProfileFromDto(profile, dto);
        DefenderProfile saved = defenderProfileRepository.save(profile);
        return convertToDefenderProfileDto(saved);
    }

    @Transactional
    public void deleteDefenderProfile(Long id) {
        defenderProfileRepository.deleteById(id);
    }

    // ========== ATTACK SCENARIO OPERATIONS ==========

    @Transactional
    public AttackScenarioDto createAttackScenario(Long levelId, AttackScenarioDto dto) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));

        AttackScenario scenario = new AttackScenario();
        scenario.setAttackType(dto.getAttackType());
        scenario.setName(dto.getName());
        scenario.setDescription(dto.getDescription());
        scenario.setAttackerNarrative(dto.getAttackerNarrative());
        scenario.setLevel(level);

        AttackScenario saved = attackScenarioRepository.save(scenario);
        return convertToAttackScenarioDto(saved);
    }

    @Transactional
    public AttackScenarioDto updateAttackScenario(Long id, AttackScenarioDto dto) {
        AttackScenario scenario = attackScenarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attack Scenario not found"));

        scenario.setAttackType(dto.getAttackType());
        scenario.setName(dto.getName());
        scenario.setDescription(dto.getDescription());
        scenario.setAttackerNarrative(dto.getAttackerNarrative());

        AttackScenario saved = attackScenarioRepository.save(scenario);
        return convertToAttackScenarioDto(saved);
    }

    @Transactional
    public void deleteAttackScenario(Long id) {
        attackScenarioRepository.deleteById(id);
    }

    // ========== ATTACK OPTION OPERATIONS ==========

    @Transactional
    public AttackOptionDto createAttackOption(Long scenarioId, AttackOptionDto dto) {
        AttackScenario scenario = attackScenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new RuntimeException("Attack Scenario not found"));

        AttackOption option = new AttackOption();
        updateAttackOptionFromDto(option, dto);
        option.setAttackScenario(scenario);

        AttackOption saved = attackOptionRepository.save(option);
        return convertToAttackOptionDto(saved);
    }

    @Transactional
    public AttackOptionDto updateAttackOption(Long id, AttackOptionDto dto) {
        AttackOption option = attackOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attack Option not found"));

        updateAttackOptionFromDto(option, dto);
        AttackOption saved = attackOptionRepository.save(option);
        return convertToAttackOptionDto(saved);
    }

    @Transactional
    public void deleteAttackOption(Long id) {
        attackOptionRepository.deleteById(id);
    }

    // ========== DEFENDER CHOICE OPERATIONS ==========

    @Transactional
    public DefenderChoiceDto createDefenderChoice(Long attackOptionId, DefenderChoiceDto dto) {
        AttackOption option = attackOptionRepository.findById(attackOptionId)
                .orElseThrow(() -> new RuntimeException("Attack Option not found"));

        DefenderChoice choice = new DefenderChoice();
        updateDefenderChoiceFromDto(choice, dto);
        choice.setAttackOption(option);

        DefenderChoice saved = defenderChoiceRepository.save(choice);
        return convertToDefenderChoiceDto(saved);
    }

    @Transactional
    public DefenderChoiceDto updateDefenderChoice(Long id, DefenderChoiceDto dto) {
        DefenderChoice choice = defenderChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Defender Choice not found"));

        updateDefenderChoiceFromDto(choice, dto);
        DefenderChoice saved = defenderChoiceRepository.save(choice);
        return convertToDefenderChoiceDto(saved);
    }

    @Transactional
    public void deleteDefenderChoice(Long id) {
        defenderChoiceRepository.deleteById(id);
    }

    // ========== CONVERSION HELPERS ==========

    private LevelDto convertToLevelDto(Level level) {
        LevelDto dto = new LevelDto();
        dto.setId(level.getId());
        dto.setName(level.getName());
        dto.setDescription(level.getDescription());
        dto.setDifficulty(level.getDifficulty());
        dto.setEnabled(level.isEnabled());
        dto.setOrderIndex(level.getOrderIndex());
        dto.setMaxAttacks(level.getMaxAttacks());

        if (level.getDefenderProfiles() != null) {
            dto.setDefenderProfiles(level.getDefenderProfiles().stream()
                    .map(this::convertToDefenderProfileDto)
                    .collect(Collectors.toList()));
        }

        if (level.getAttackScenarios() != null) {
            dto.setAttackScenarios(level.getAttackScenarios().stream()
                    .map(this::convertToAttackScenarioDto)
                    .collect(Collectors.toList()));
        }

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

        if (scenario.getAttackOptions() != null) {
            dto.setAttackOptions(scenario.getAttackOptions().stream()
                    .map(this::convertToAttackOptionDto)
                    .collect(Collectors.toList()));
        }

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

        if (option.getDefenderChoices() != null) {
            dto.setDefenderChoices(option.getDefenderChoices().stream()
                    .map(this::convertToDefenderChoiceDto)
                    .collect(Collectors.toList()));
        }

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
        dto.setEducationalNote(choice.getEducationalNote());
        return dto;
    }

    // ========== UPDATE HELPERS ==========

    private void updateDefenderProfileFromDto(DefenderProfile profile, DefenderProfileDto dto) {
        profile.setName(dto.getName());
        profile.setDescription(dto.getDescription());
        profile.setAge(dto.getAge());
        profile.setAgeGroup(dto.getAgeGroup());
        profile.setOccupation(dto.getOccupation());
        profile.setTechSavviness(dto.getTechSavviness());
        profile.setMentalState(dto.getMentalState());
        profile.setFinancialStatus(dto.getFinancialStatus());
        profile.setAvatarIcon(dto.getAvatarIcon());
    }

    private void updateAttackOptionFromDto(AttackOption option, AttackOptionDto dto) {
        option.setLabel(dto.getLabel());
        option.setDescription(dto.getDescription());
        option.setAttackerMessage(dto.getAttackerMessage());
        option.setImpersonatedEntity(dto.getImpersonatedEntity());
        option.setBaseAttackerPoints(dto.getBaseAttackerPoints());
        option.setRiskLevel(dto.getRiskLevel());
        option.setCriticalRisk(dto.isCriticalRisk());
    }

    private void updateDefenderChoiceFromDto(DefenderChoice choice, DefenderChoiceDto dto) {
        choice.setLabel(dto.getLabel());
        choice.setDescription(dto.getDescription());
        choice.setOutcome(dto.getOutcome());
        choice.setDefenderScoreDelta(dto.getDefenderScoreDelta());
        choice.setAttackerScoreDelta(dto.getAttackerScoreDelta());
        choice.setChoiceType(dto.getChoiceType());
        choice.setEducationalNote(dto.getEducationalNote());
    }
}
