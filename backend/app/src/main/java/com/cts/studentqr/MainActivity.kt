package com.cts.studentqr

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cts.studentqr.network.QRDecodeRequestDTO
import com.cts.studentqr.network.QRDecodeResponseDTO
import com.cts.studentqr.network.RetrofitClient
import com.cts.studentqr.ui.theme.StudentQRAppTheme
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private var studentIdState = mutableStateOf("")
    private var isValidState = mutableStateOf<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentQRAppTheme {
                MainScreen(
                    studentIdState = studentIdState,
                    isValidState = isValidState
                )
            }
        }
    }

    @Composable
    fun MainScreen(
        studentIdState: MutableState<String>,
        isValidState: MutableState<Boolean?>
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFf0f0f0))
                .padding(16.dp)
        ) {
            // ✅ Response Card at top
            isValidState.value?.let { valid ->
                val cardColor = if (valid) Color(0xFF4CAF50) else Color(0xFFF44336)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .align(Alignment.TopCenter),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (valid) "Valid Student ✅ ${studentIdState.value}"
                        else "Invalid Student ❌",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // ✅ Scan QR Button at center
            Button(
                onClick = { startQRScanner() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(60.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text(
                    text = "Scan QR",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    private fun startQRScanner() {
        IntentIntegrator(this).apply {
            setOrientationLocked(false)
            setPrompt("Place QR code inside the viewfinder")
            initiateScan()
        }
    }

    private fun callBackend(qr: String) {
        val request = QRDecodeRequestDTO(qrText = qr)
        RetrofitClient.apiService.validateQr(request)
            .enqueue(object : Callback<QRDecodeResponseDTO> {
                override fun onResponse(
                    call: Call<QRDecodeResponseDTO>,
                    response: Response<QRDecodeResponseDTO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            studentIdState.value = it.studentId ?: ""
                            isValidState.value = it.valid
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Backend error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<QRDecodeResponseDTO>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val scannedQr = result.contents
            if (!scannedQr.isNullOrEmpty()) {
                callBackend(scannedQr)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
