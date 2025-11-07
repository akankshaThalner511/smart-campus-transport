package com.ams.utility;

import com.ams.config.FileStorageConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class QRCodeUtil {

    private final FileStorageConfig fileStorageConfig;

    /**
     * Generate QR code PNG for a student and return public URL.
     */
    public String generateQRCode(String studentId) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(studentId, BarcodeFormat.QR_CODE, 200, 200); // bigger size

        // Ensure upload directory exists
        Path uploadPath = Paths.get(fileStorageConfig.getUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("Created QR code directory: {}", uploadPath.toAbsolutePath());
        }

        // Build file path
        String fileName = studentId + ".png";
        Path filePath = uploadPath.resolve(fileName);

        // Write QR code image
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
        log.info("QR code generated: {}", filePath.toAbsolutePath());

        // Build public URL
        String baseUrl = fileStorageConfig.getBaseUrl(); // e.g., http://localhost:8080
        String relativePath = fileStorageConfig.getUploadDir().replace("\\", "/"); // Windows fix
        if (!relativePath.endsWith("/")) relativePath += "/";

        return baseUrl + "/" + relativePath + fileName;
    }
}
