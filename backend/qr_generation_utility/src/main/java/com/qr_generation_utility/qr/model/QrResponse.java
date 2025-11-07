package com.qr_generation_utility.qr.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QrResponse {

	private String qrLink;
    private String message;
}
