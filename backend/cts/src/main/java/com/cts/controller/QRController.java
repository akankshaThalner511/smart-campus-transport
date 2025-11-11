package com.cts.controller;

import com.cts.dto.*;
import com.cts.service.QrService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QRController {

    private final QrService qrService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQr(@RequestBody QRGenerationRequestDTO request) throws IOException, WriterException {
        if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
            return ResponseEntity.ok(null); // Return null if studentId is missing
        }

        String qrPath = qrService.generateQr(request.getStudentId());
        return ResponseEntity.ok(qrPath);
    }


    // ✅ Endpoint to decode QR (only processes QR starting with "STU/")
    @PostMapping("/decode")
    public ResponseEntity<String> decodeQr(@RequestBody String qrText) {
        // 1️⃣ Null or empty check
        if (qrText == null || qrText.isEmpty()) {
            return ResponseEntity.badRequest().body("QR code is empty");
        }

        // 2️⃣ Only process QR codes starting with "STU/"
        if (!qrText.startsWith("STU/")) {
            return ResponseEntity.badRequest().body("Invalid QR code prefix");
        }

        try {
            // 3️⃣ Remove prefix and decrypt
            String encryptedPart = qrText.substring(4); // Remove "STU/"
            String studentId = qrService.decryptStudentId(encryptedPart);

            return ResponseEntity.ok(studentId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to decode QR: " + e.getMessage());
        }
    }

    @PostMapping("/generate/v1")
    public ResponseEntity<QRGenerationResponseDTO> generateQrv1(
            @RequestBody QRGenerationRequestDTO request) {

        if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new QRGenerationResponseDTO(null, "Student ID missing", false));
        }

        try {
            String qrPath = qrService.generateQr(request.getStudentId());

            return ResponseEntity.ok(
                    new QRGenerationResponseDTO(qrPath,
                            "QR Generated Successfully",
                            true)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new QRGenerationResponseDTO(null,
                            "Failed: " + e.getMessage(),
                            false)
            );
        }
    }
    @PostMapping("/decode/v1")
    public ResponseEntity<QRDecodeResponseDTO> decodeQrv1(
            @RequestBody QRDecodeRequestDTO request) {

        String qrText = request.getQrText();

        if (qrText == null || qrText.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new QRDecodeResponseDTO(null,
                            "QR Code is empty",
                            false)
            );
        }

        if (!qrText.startsWith("STU/")) {
            return ResponseEntity.badRequest().body(
                    new QRDecodeResponseDTO(null,
                            "Invalid QR prefix",
                            false)
            );
        }

        try {
            String encrypted = qrText.substring(4).trim();
            String studentId = qrService.decryptStudentId(encrypted);

            return ResponseEntity.ok(
                    new QRDecodeResponseDTO(studentId,
                            "QR Valid",
                            true)
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new QRDecodeResponseDTO(null,
                            "Decrypt failed: " + e.getMessage(),
                            false)
            );
        }
    }
    @PostMapping("/validate")
    public ResponseEntity<QRDecodeResponseDTO> validateQr(@RequestBody QRDecodeRequestDTO request) {
        String qrText = request.getQrText();

        if (qrText == null || qrText.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new QRDecodeResponseDTO(null, "QR Code is empty", false)
            );
        }

        try {
            // Call the service method that returns QRValidationResponse
            QRValidationResponse validation = qrService.validateStudentQr(qrText);

            boolean success = "SUCCESS".equals(validation.getStatus());

            // Map rollNo from validation response to studentId in DTO (for testing purposes)
            String studentIdOrRollNo = validation.getRollNo(); // Using rollNo as dummy ID

            return ResponseEntity.ok(
                    new QRDecodeResponseDTO(
                            studentIdOrRollNo,
                            validation.getMessage(),
                            success
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new QRDecodeResponseDTO(null, "Validation failed: " + e.getMessage(), false)
            );
        }
    }
    @PostMapping("/validate-student")
    public ResponseEntity<QRStudentValidationResponseDTO> validateStudentQR(
            @RequestBody QRDecodeRequestDTO request) {

        if (request.getQrText() == null || request.getQrText().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    QRStudentValidationResponseDTO.builder()
                            .status("FAILED")
                            .message("QR Code is empty")
                            .build()
            );
        }

        QRStudentValidationResponseDTO response = qrService.validateStudentByQR(request);

        /*if ("FAILED".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }*/

        return ResponseEntity.ok(response);
    }



}
