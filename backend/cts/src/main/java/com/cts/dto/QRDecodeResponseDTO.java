package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRDecodeResponseDTO {
    private String studentId;
    private String message;
    private boolean valid;
}
