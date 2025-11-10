package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRValidationResponse {
    private String status;    // SUCCESS / FAILED
    private String message;   // Description
    private String rollNo;    // Optional, student rollNo if success
}
