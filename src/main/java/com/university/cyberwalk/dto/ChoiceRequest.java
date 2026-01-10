package com.university.cyberwalk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceRequest {
    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Option ID is required")
    private Long optionId;

    private String targetVideoId;
    private int scoreChange;
}
