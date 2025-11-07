package com.cts.controller;

import com.cts.dto.QRGenerationRequestDTO;
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
}
