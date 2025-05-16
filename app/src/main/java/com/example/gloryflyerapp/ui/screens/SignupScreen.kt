package com.example.gloryflyerapp.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.AuthRepository
import com.example.gloryflyerapp.ui.components.NameField
import com.example.gloryflyerapp.ui.components.PhoneNumberField
import com.example.gloryflyerapp.ui.components.PrimaryButton
import com.example.gloryflyerapp.ui.theme.AuthBackground
import kotlinx.coroutines.launch

private const val TAG = "SignupScreen"

@Composable
fun SignupScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }
    val scrollState = rememberScrollState()

    AuthBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Join us and start your journey",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF3949AB)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                NameField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth()
                )

                PhoneNumberField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFE53935),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                PrimaryButton(
                    text = if (isLoading) "Sending OTP..." else "Send OTP",
                    onClick = {
                        // Validate inputs
                        if (name.isBlank()) {
                            errorMessage = "Please enter your name"
                            return@PrimaryButton
                        }
                        if (phoneNumber.length < 10) {
                            errorMessage = "Please enter a valid phone number"
                            return@PrimaryButton
                        }

                        scope.launch {
                            try {
                                isLoading = true
                                errorMessage = null
                                Log.d(TAG, "Starting signup process for phone: $phoneNumber")
                                
                                authRepository.signInWithPhoneNumber(
                                    phoneNumber = "+91$phoneNumber",
                                    onVerificationCodeSent = { verificationId ->
                                        Log.d(TAG, "OTP sent successfully, navigating to verification")
                                        navController.navigate("otp_verification/$verificationId/$name")
                                    },
                                    onVerificationFailed = { exception ->
                                        Log.e(TAG, "OTP verification failed", exception)
                                        errorMessage = exception.message ?: "Failed to send OTP"
                                    }
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Error during signup", e)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { navController.navigate("login") },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF1A237E)
                        )
                    ) {
                        Text(
                            "Already have an account? Login",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                TextButton(
                    onClick = { 
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF3949AB)
                    )
                ) {
                    Text(
                        "Skip for now",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
} 