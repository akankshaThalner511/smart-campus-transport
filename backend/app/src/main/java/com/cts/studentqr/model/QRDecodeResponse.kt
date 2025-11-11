package com.cts.studentqr.model

// This will map the JSON returned from backend
data class QRDecodeResponse(
    val studentId: String?,  // ID returned by backend
    val message: String?,    // Success or error message
    val valid: Boolean?      // true if student is valid
)
