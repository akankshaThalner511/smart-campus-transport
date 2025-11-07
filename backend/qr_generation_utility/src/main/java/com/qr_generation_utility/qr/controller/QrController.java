package com.qr_generation_utility.qr.controller;




import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qr_generation_utility.qr.exception.QrException;
import com.qr_generation_utility.qr.model.QrResponse;
import com.qr_generation_utility.qr.service.QrService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qr")
public class QrController {

    private final QrService qrService;

    @PostMapping("/generate")
    public QrResponse generateQr(@RequestBody(required = false) String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new QrException(1001, "Student ID is missing");
        }
        String qrLink = qrService.generateQr(studentId);
        return new QrResponse(qrLink, "QR successfully generated");
    }

    @PostMapping("/decrypt")
    public String decryptQr(@RequestBody String encryptedText) throws Exception {
        return qrService.decryptQr(encryptedText);
    }
}
