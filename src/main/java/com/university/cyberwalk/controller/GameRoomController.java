package com.university.cyberwalk.controller;

import com.university.cyberwalk.dto.RoomRequests;
import com.university.cyberwalk.model.GameRoom;
import com.university.cyberwalk.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@CrossOrigin(origins = "*") // Allow localhost frontend
public class GameRoomController {

    @Autowired
    private GameRoomService gameRoomService;

    @PostMapping("/create")
    public ResponseEntity<GameRoom> createRoom() {
        return ResponseEntity.ok(gameRoomService.createRoom());
    }

    @PostMapping("/join")
    public ResponseEntity<GameRoom> joinRoom(@RequestBody RoomRequests.JoinRoomRequest request) {
        return ResponseEntity.ok(gameRoomService.joinRoom(request.getRoomId(), request.getRole()));
    }

    @GetMapping("/{roomId}/status")
    public ResponseEntity<GameRoom> getRoomStatus(@PathVariable String roomId) {
        return ResponseEntity.ok(gameRoomService.getRoomStatus(roomId));
    }

    @PostMapping("/{roomId}/attack")
    public ResponseEntity<GameRoom> selectAttack(@PathVariable String roomId,
            @RequestBody RoomRequests.AttackSelectionRequest request) {
        return ResponseEntity.ok(gameRoomService.setAttack(roomId, request.getAttackType()));
    }

    @PostMapping("/{roomId}/action")
    public ResponseEntity<GameRoom> defenderAction(@PathVariable String roomId,
            @RequestBody RoomRequests.DefenderActionRequest request) {
        return ResponseEntity.ok(gameRoomService.processAction(roomId, request.getOptionId()));
    }
}
