package com.qr_generation_utility.qr.service;

import com.qr_generation_utility.qr.util.EncryptionUtil;
import com.qr_generation_utility.qr.util.QrUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QrService {

    public String generateQr(String studentId) {
        try {
            // Encrypt studentId for QR
            String encrypted = EncryptionUtil.encrypt(studentId);
          
     //     log.info("Encrypted studentId: {}", encrypted);

            // Generate QR PNG
            return QrUtil.generateQrCode(encrypted, studentId);
        } catch (Exception e) {
      //      log.error("Failed to generate QR", e);
            throw new RuntimeException("QR generation failed");
        }
    }

    public String decryptQr(String encryptedText) throws Exception {
        return EncryptionUtil.decrypt(encryptedText);
    }
}
