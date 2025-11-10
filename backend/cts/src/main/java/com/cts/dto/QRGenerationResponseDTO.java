package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRGenerationResponseDTO {
    private String qrImageUrl;
    private String message;
    private boolean success;
}
