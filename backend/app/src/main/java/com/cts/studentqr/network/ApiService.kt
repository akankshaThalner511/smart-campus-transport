package com.cts.studentqr.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// ✅ Request data class
data class QRDecodeRequestDTO(val qrText: String)

// ✅ Response data class
data class QRDecodeResponseDTO(
    val studentId: String?,
    val message: String?,
    val valid: Boolean?
)

// ✅ Retrofit API interface
interface ApiService {
    @POST("/api/v1/qr/validate")
    fun validateQr(@Body request: QRDecodeRequestDTO): Call<QRDecodeResponseDTO>
}
