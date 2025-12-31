package com.university.cyberwalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceRequest {
    private String sessionId;
    private Long optionId;
    private String targetVideoId;
    private int scoreChange;
}
