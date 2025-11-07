package com.qr_generation_utility.qr.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrUtil {

    public static String generateQrCode(String text, String studentId) throws WriterException, IOException {
        if (text == null || studentId == null || text.isEmpty() || studentId.isEmpty()) {
            throw new WriterException("Invalid input for QR generation");
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);

        String qrDir = "src/main/resources/static/qr/";
        File directory = new File(qrDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = qrDir + studentId + ".png";
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "/qr/" + studentId + ".png";
    }
}
