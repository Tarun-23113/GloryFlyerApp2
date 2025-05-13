package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.AuthRepository
import com.example.gloryflyerapp.ui.components.PhoneNumberField
import com.example.gloryflyerapp.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
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
            text = "Welcome to Glory Flyer",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        PrimaryButton(
            text = if (isLoading) "Sending OTP..." else "Send OTP",
            onClick = {
                if (phoneNumber.length < 10) {
                    errorMessage = "Please enter a valid phone number"
                    return@PrimaryButton
                }
                
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    
                    try {
                        authRepository.signInWithPhoneNumber(
                            phoneNumber = "+91$phoneNumber",
                            onVerificationCodeSent = { verificationId ->
                                navController.navigate("otp_verification/$verificationId")
                            },
                            onVerificationFailed = { exception ->
                                errorMessage = exception.message ?: "Verification failed"
                            }
                        )
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "An error occurred"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading
        )

        TextButton(
            onClick = { navController.navigate("signup") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Don't have an account? Sign up")
        }
    }
} 