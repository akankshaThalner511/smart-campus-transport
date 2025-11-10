package com.cts.service;

import com.cts.util.EncryptionUtil;
import com.cts.util.QrUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class QrService {

    public String generateQr(String studentId) throws WriterException, IOException {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID is missing");
        }

        try {
            // Encrypt studentId for QR
            String encrypted = EncryptionUtil.encrypt(studentId);
            log.info("Encrypted studentId: {}", encrypted);

            // ✅ Correct QR Data Format: STU/<encrypted>
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
}
