package com.example.gloryflyerapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.AuthRepository
import com.example.gloryflyerapp.ui.components.OtpField
import com.example.gloryflyerapp.ui.components.PrimaryButton
import kotlinx.coroutines.launch

private const val TAG = "OtpVerificationScreen"

@Composable
fun OtpVerificationScreen(
    navController: NavHostController,
    verificationId: String,
    name: String? = null
) {
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify OTP",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Enter the 6-digit code sent to your phone",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OtpField(
            value = otp,
            onValueChange = { otp = it },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        PrimaryButton(
            text = if (isLoading) "Verifying..." else "Verify OTP",
            onClick = {
                if (otp.length != 6) {
                    errorMessage = "Please enter a valid 6-digit OTP"
                    return@PrimaryButton
                }

                scope.launch {
                    try {
                        isLoading = true
                        errorMessage = null
                        Log.d(TAG, "Starting OTP verification for ID: $verificationId")
                        
                        authRepository.verifyOtp(verificationId, otp, name).fold(
                            onSuccess = {
                                Log.d(TAG, "OTP verification successful")
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onFailure = { exception ->
                                Log.e(TAG, "OTP verification failed", exception)
                                errorMessage = exception.message ?: "Verification failed"
                            }
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error during OTP verification", e)
                        errorMessage = e.message ?: "An error occurred"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        TextButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Didn't receive OTP? Resend")
        }
    }
} 