package com.university.cyberwalk.service;

import com.university.cyberwalk.model.AttackType;
import com.university.cyberwalk.model.GameRoom;
import com.university.cyberwalk.repository.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameRoomService {

    @Autowired
    private com.university.cyberwalk.repository.OptionRepository optionRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    public GameRoom processAction(String roomId, Long optionId) {
        GameRoom room = gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != GameRoom.RoomStatus.DEFENDER_TURN) {
            throw new RuntimeException("Not in defender turn");
        }

        com.university.cyberwalk.model.Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        room.setDefenderScore(room.getDefenderScore() + option.getDefenderScoreDelta());
        room.setAttackerScore(room.getAttackerScore() + option.getAttackerScoreDelta());
        room.setCurrentVideoId(option.getTargetVideoId());

        // Update Video State Logic (if needed) or just let Client handle navigation
        // based on Option's targetVideoId.
        // For server-side validation, we could track currentVideoId in Room.

        // Check if game over?
        if (com.university.cyberwalk.model.Option.SCORE_FAIL == option.getDefenderScoreDelta()) {
            room.setStatus(GameRoom.RoomStatus.ROUND_OVER);
        }

        return gameRoomRepository.save(room);
    }

    public GameRoom createRoom() {
        GameRoom room = new GameRoom();
        room.setRoomId(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        room.setStatus(GameRoom.RoomStatus.WAITING);
        return gameRoomRepository.save(room);
    }

    public GameRoom joinRoom(String roomId, String role) {
        GameRoom room = gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if ("ATTACKER".equalsIgnoreCase(role)) {
            if (room.getAttackerSessionId() != null) {
                throw new RuntimeException("Attacker role already taken");
            }
            room.setAttackerSessionId(UUID.randomUUID().toString());
        } else if ("DEFENDER".equalsIgnoreCase(role)) {
            if (room.getDefenderSessionId() != null) {
                throw new RuntimeException("Defender role already taken");
            }
            room.setDefenderSessionId(UUID.randomUUID().toString());
        } else {
            throw new RuntimeException("Invalid role");
        }

        if (room.getAttackerSessionId() != null && room.getStatus() == GameRoom.RoomStatus.WAITING) {
            room.setStatus(GameRoom.RoomStatus.ATTACK_SELECTION);
        }

        return gameRoomRepository.save(room);
    }

    public GameRoom getRoomStatus(String roomId) {
        return gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public GameRoom setAttack(String roomId, AttackType attackType) {
        GameRoom room = gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != GameRoom.RoomStatus.ATTACK_SELECTION) {
            throw new RuntimeException("Not in attack selection phase");
        }

        if (room.getDefenderSessionId() == null) {
            throw new RuntimeException("Cannot start attack: Defender has not joined yet");
        }

        room.setSelectedAttackType(attackType);
        room.setStatus(GameRoom.RoomStatus.DEFENDER_TURN);

        // Simple mapping for prototype (In production, use a lookup table)
        switch (attackType) {
            case OFFICE_REAL:
            case OFFICE_FAKE:
                room.setCurrentVideoId("1"); // Office Scenario Start
                break;
            case MFA_REAL:
            case MFA_FAKE:
                room.setCurrentVideoId("2"); // Home Scenario Start (Mock)
                break;
            case WIFI_ATTACK:
                room.setCurrentVideoId("3"); // Restaurant Scenario Start (Mock)
                break;
            default:
                room.setCurrentVideoId("1");
        }

        return gameRoomRepository.save(room);
    }

    public GameRoom updateScore(String roomId, int defenderDelta, int attackerDelta) {
        GameRoom room = gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setDefenderScore(room.getDefenderScore() + defenderDelta);
        room.setAttackerScore(room.getAttackerScore() + attackerDelta);

        // Logic to check if round is over can be added here

        return gameRoomRepository.save(room);
    }
}
