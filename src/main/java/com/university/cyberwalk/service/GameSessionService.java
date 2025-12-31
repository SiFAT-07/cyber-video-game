package com.university.cyberwalk.service;

import com.university.cyberwalk.dto.ChoiceRequest;
import com.university.cyberwalk.dto.SessionResponse;
import com.university.cyberwalk.model.GameSession;
import com.university.cyberwalk.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GameSessionService {
    
    @Autowired
    private GameSessionRepository gameSessionRepository;
    
    public SessionResponse createNewSession() {
        GameSession session = new GameSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setCurrentScore(0);
        session.setCurrentVideoId("1");
        session.setStartTime(LocalDateTime.now());
        session.setLastUpdated(LocalDateTime.now());
        session.setCompleted(false);
        
        session = gameSessionRepository.save(session);
        return convertToResponse(session);
    }
    
    public SessionResponse getSession(String sessionId) {
        GameSession session = gameSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        return convertToResponse(session);
    }
    
    public SessionResponse makeChoice(ChoiceRequest request) {
        GameSession session = gameSessionRepository.findBySessionId(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found: " + request.getSessionId()));
        
        // Update score
        session.setCurrentScore(session.getCurrentScore() + request.getScoreChange());
        
        // Update current video
        session.setCurrentVideoId(request.getTargetVideoId());
        
        // Update timestamp
        session.setLastUpdated(LocalDateTime.now());
        
        session = gameSessionRepository.save(session);
        return convertToResponse(session);
    }
    
    public SessionResponse completeSession(String sessionId) {
        GameSession session = gameSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        
        session.setCompleted(true);
        session.setLastUpdated(LocalDateTime.now());
        
        session = gameSessionRepository.save(session);
        return convertToResponse(session);
    }
    
    private SessionResponse convertToResponse(GameSession session) {
        return new SessionResponse(
                session.getSessionId(),
                session.getCurrentScore(),
                session.getCurrentVideoId(),
                session.isCompleted()
        );
    }
}
