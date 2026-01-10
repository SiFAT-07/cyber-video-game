package com.university.cyberwalk.dto;

import com.university.cyberwalk.model.AttackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class RoomRequests {

    @Data
    public static class CreateRoomRequest {
        // Empty body usually, or config
    }

    @Data
    public static class JoinRoomRequest {
        @NotBlank(message = "Room ID is required")
        private String roomId;

        @NotBlank(message = "Role is required")
        private String role; // "ATTACKER" or "DEFENDER"
    }

    @Data
    public static class AttackSelectionRequest {
        private String roomId;

        @NotNull(message = "Attack Type is required")
        private AttackType attackType;
    }

    @Data
    public static class DefenderActionRequest {
        private String roomId;

        @NotNull(message = "Option ID is required")
        private Long optionId;
    }
}
