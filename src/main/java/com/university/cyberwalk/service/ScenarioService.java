package com.university.cyberwalk.service;

import com.university.cyberwalk.dto.OptionDto;
import com.university.cyberwalk.dto.ScenarioResponse;
import com.university.cyberwalk.model.Option;
import com.university.cyberwalk.model.Scenario;
import com.university.cyberwalk.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    public ScenarioResponse getScenarioByVideoId(String videoId) {
        Scenario scenario = scenarioRepository.findByVideoId(videoId)
                .orElseThrow(() -> new RuntimeException("Scenario not found for videoId: " + videoId));

        return convertToResponse(scenario);
    }

    public List<ScenarioResponse> getAllScenarios() {
        return scenarioRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ScenarioResponse convertToResponse(Scenario scenario) {
        List<OptionDto> optionDtos = scenario.getOptions().stream()
                .map(this::convertToOptionDto)
                .collect(Collectors.toList());

        return new ScenarioResponse(
                scenario.getVideoId(),
                scenario.getVideoPath(),
                scenario.getDescription(),
                scenario.getAttackerDescription(),
                scenario.getAttackType() != null ? scenario.getAttackType().name() : null,
                scenario.isLeafNode(),
                scenario.getNextScenarioId(),
                optionDtos);
    }

    private OptionDto convertToOptionDto(Option option) {
        return new OptionDto(
                option.getId(),
                option.getLabel(),
                option.getTargetVideoId(),
                option.getDefenderScoreDelta(),
                option.getAttackerScoreDelta(),
                option.getPosition(),
                option.getInteractionType(),
                option.getAppearTime());
    }
}
