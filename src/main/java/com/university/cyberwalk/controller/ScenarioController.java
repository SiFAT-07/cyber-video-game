package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.ScenarioResponse;
import com.university.cyberwalk.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
@CrossOrigin(origins = "*")
public class ScenarioController {
    
    @Autowired
    private ScenarioService scenarioService;
    
    @GetMapping("/{videoId}")
    public ResponseEntity<ScenarioResponse> getScenario(@PathVariable String videoId) {
        ScenarioResponse response = scenarioService.getScenarioByVideoId(videoId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ScenarioResponse>> getAllScenarios() {
        List<ScenarioResponse> scenarios = scenarioService.getAllScenarios();
        return ResponseEntity.ok(scenarios);
    }
}
