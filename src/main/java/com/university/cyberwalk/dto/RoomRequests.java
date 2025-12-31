package com.university.cyberwalk.dto;

import com.university.cyberwalk.model.AttackType;
import lombok.Data;

public class RoomRequests {

    @Data
    public static class CreateRoomRequest {
        // Empty body usually, or config
    }

    @Data
    public static class JoinRoomRequest {
        private String roomId;
        private String role; // "ATTACKER" or "DEFENDER"
    }

    @Data
    public static class AttackSelectionRequest {
        private String roomId;
        private AttackType attackType;
    }

    @Data
    public static class DefenderActionRequest {
        private String roomId;
        private Long optionId;
    }
}
