package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.ChoiceRequest;
import com.university.cyberwalk.dto.SessionResponse;
import com.university.cyberwalk.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "*")
public class GameSessionController {
    
    @Autowired
    private GameSessionService gameSessionService;
    
    @PostMapping("/start")
    public ResponseEntity<SessionResponse> startNewSession() {
        SessionResponse response = gameSessionService.createNewSession();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable String sessionId) {
        SessionResponse response = gameSessionService.getSession(sessionId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/choice")
    public ResponseEntity<SessionResponse> makeChoice(@RequestBody ChoiceRequest request) {
        SessionResponse response = gameSessionService.makeChoice(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/complete/{sessionId}")
    public ResponseEntity<SessionResponse> completeSession(@PathVariable String sessionId) {
        SessionResponse response = gameSessionService.completeSession(sessionId);
        return ResponseEntity.ok(response);
    }
}
