package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.AuthRepository
import com.example.gloryflyerapp.data.UserProfile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    // Load current user profile
    LaunchedEffect(Unit) {
        authRepository.getCurrentUser()?.let { user ->
            name = user.displayName ?: ""
            phone = user.phoneNumber ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Section
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Phone Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                enabled = false,
                singleLine = true
            )

            // Save Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name"
                        showErrorDialog = true
                        return@Button
                    }
                    
                    scope.launch {
                        isLoading = true
                        try {
                            val result = authRepository.updateUserProfile(
                                UserProfile(
                                    name = name,
                                    email = authRepository.getCurrentUser()?.email ?: "",
                                    phone = phone
                                )
                            )
                            if (result.isSuccess) {
                                showSuccessDialog = true
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Failed to update profile"
                                showErrorDialog = true
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "An error occurred"
                            showErrorDialog = true
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Changes")
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.navigateUp()
            },
            title = { Text("Success") },
            text = { Text("Your profile has been updated successfully.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
} 