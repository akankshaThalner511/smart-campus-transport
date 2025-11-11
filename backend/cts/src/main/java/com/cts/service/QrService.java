package com.cts.service;

import com.cts.dto.QRDecodeRequestDTO;
import com.cts.dto.QRStudentValidationResponseDTO;
import com.cts.dto.QRValidationResponse;
import com.cts.entity.StudentDetails;
import com.cts.entity.StudentTransportInfo;
import com.cts.entity.enums.FeeStatus;
import com.cts.entity.enums.StudentStatus;
import com.cts.entity.enums.TransportValidation;
import com.cts.util.EncryptionUtil;
import com.cts.util.QrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.io.IOException;

@Slf4j
@Service
public class QrService {

    // ✅ Generate QR for Student ID (encrypted text)
    public String generateQr(String studentId) throws IOException {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID is missing");
        }
        try {
            String encrypted = EncryptionUtil.encrypt(studentId);
            log.info("Encrypted Text: {}", encrypted);

            return QrUtil.generateQrCode("STU/" + encrypted, studentId);

        } catch (Exception ex) {
            log.error("QR Generation Failed", ex);
            throw new IOException("QR Generation Error: " + ex.getMessage());
        }
    }

    // ✅ Existing Simple Response API (Not Modified)
    public QRValidationResponse validateStudentQr(String qrText) {
        try {
            if (qrText == null || !qrText.startsWith("STU/")) {
                return new QRValidationResponse("FAILED", "Invalid QR prefix", null);
            }

            String encryptedPart = qrText.substring(4).trim();
            String decrypted = EncryptionUtil.decrypt(encryptedPart);

            // Dummy data
            StudentDetails student = StudentDetails.builder()
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

        } catch (Exception ex) {
            return new QRValidationResponse("FAILED",
                    "Decryption/Validation failed: " + ex.getMessage(), null);
        }
    }
    // ✅ Re-add this method because controller is using it
    public String decryptStudentId(String encryptedText) throws Exception {
        return EncryptionUtil.decrypt(encryptedText);
    }
    public QRStudentValidationResponseDTO validateStudentByQR(QRDecodeRequestDTO request) {

        String qrText = request.getQrText();

        // ✅ Validate QR prefix
        if (qrText == null || !qrText.startsWith("STU/")) {
            return QRStudentValidationResponseDTO.builder()
                    .status("FAILED")
                    .message("Invalid QR Code Format")
                    .build();
        }

        // ✅ Extract Base64 encrypted part
        String encryptedValue = qrText.substring(4);

        String rollNo;
        try {
            rollNo = EncryptionUtil.decrypt(encryptedValue);
            log.info("Decrypted RollNo: {}", rollNo);
        } catch (Exception e) {
            log.error("QR Decryption Failed", e);
            return QRStudentValidationResponseDTO.builder()
                    .status("FAILED")
                    .message("QR Decryption Failed")
                    .build();
        }

        // ✅ Find Student in Dummy Data
        StudentTransportInfo student = StudentTransportInfo.DUMMY_DATA.get(rollNo);

        if (student == null) {
            return QRStudentValidationResponseDTO.builder()
                    .status("FAILED")
                    .message("Student Not Found")
                    .build();
        }

        // ✅ Check Transport Eligibility
        if (student.getFeeStatus() != FeeStatus.PAID ||
                student.getTransportValidation() == TransportValidation.DENIED) {

            return QRStudentValidationResponseDTO.builder()
                    .status("FAILED")
                    .message("Transport Access Denied")
                    .rollNo(student.getRollNo())
                    .fullName(student.getFullName())
                    .department(student.getDepartment())
                    .profileImageUrl(student.getProfileImageUrl())
                    .build();
        }

        // ✅ SUCCESS RESPONSE
        return QRStudentValidationResponseDTO.builder()
                .status("SUCCESS")
                .message("Boarding Allowed ✅")
                .rollNo(student.getRollNo())
                .fullName(student.getFullName())
                .department(student.getDepartment())
                .profileImageUrl(student.getProfileImageUrl())
                .build();
    }

}
