package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.ui.components.NameField
import com.example.gloryflyerapp.ui.components.PhoneNumberField
import com.example.gloryflyerapp.ui.components.PrimaryButton

@Composable
fun SignupScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        NameField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PhoneNumberField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PrimaryButton(
            text = "Send OTP",
            onClick = { navController.navigate("otp_verification") }
        )

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Already have an account? Login")
        }
    }
} 