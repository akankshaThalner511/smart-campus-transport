package com.cts.service;

import com.cts.dto.QRValidationResponse;
import com.cts.entity.StudentDetails;
import com.cts.entity.enums.FeeStatus;
import com.cts.entity.enums.StudentStatus;
import com.cts.entity.enums.TransportValidation;
import com.cts.util.EncryptionUtil;
import com.cts.util.QrUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class QrService {

    // ✅ Generate QR for a given studentId
    public String generateQr(String studentId) throws WriterException, IOException {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID is missing");
        }

        try {
            // Encrypt studentId for QR
            String encrypted = EncryptionUtil.encrypt(studentId);
            log.info("Encrypted studentId: {}", encrypted);

            // Correct QR Data Format: STU/<encrypted>
            return QrUtil.generateQrCode("STU/" + encrypted, studentId);

        } catch (Exception e) {
            log.error("Failed to generate QR", e);
            throw new IOException("QR generation failed: " + e.getMessage());
        }
    }

    // ✅ Decrypt the encrypted part to retrieve Student ID
    public String decryptStudentId(String encryptedText) throws Exception {
        return EncryptionUtil.decrypt(encryptedText);
    }
    // Already returning QRValidationResponse
    public QRValidationResponse validateStudentQr(String qrText) {
        try {
            if (qrText == null || !qrText.startsWith("STU/")) {
                return new QRValidationResponse("FAILED", "Invalid QR prefix", null);
            }

            String encryptedPart = qrText.substring(4).trim();
            String decrypted = EncryptionUtil.decrypt(encryptedPart);

            StudentDetails student = StudentDetails.builder()
                    .studentId(UUID.randomUUID())
                    .rollNo("Aashu123")
                    .department("Computer Science")
                    .feeStatus(FeeStatus.PAID)
                    .transportValidation(TransportValidation.ALLOWED)
                    .status(StudentStatus.ACTIVE)
                    .qrCodeUrl(qrText)
                    .build();

            if (student.getFeeStatus() != FeeStatus.PAID ||
                    student.getTransportValidation() != TransportValidation.ALLOWED) {
                return new QRValidationResponse("FAILED",
                        "Transport validation denied or fee unpaid", null);
            }

            return new QRValidationResponse("SUCCESS",
                    "Boarding allowed", student.getRollNo());

        } catch (Exception e) {
            return new QRValidationResponse("FAILED",
                    "Decryption/Validation failed: " + e.getMessage(), null);
        }
    }

}
