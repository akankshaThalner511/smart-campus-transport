package com.cts.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QRStudentValidationResponseDTO {
    private String status;   // SUCCESS / FAILED
    private String message;  // Reason
    private String rollNo;   // Optional
    private String fullName;
    private String department;
    private String profileImageUrl;
}
