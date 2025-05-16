package com.example.gloryflyerapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.ui.theme.ThemeManager
import com.example.gloryflyerapp.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var notifications by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(true) }
    var pushNotifications by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Mode Toggle
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            if (ThemeState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Theme Icon"
                        )
                        Column {
                            Text(
                                "Dark Mode",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                if (ThemeState.isDarkMode) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Switch(
                        checked = ThemeState.isDarkMode,
                        onCheckedChange = { ThemeState.toggleTheme() }
                    )
                }
            }

            // Other Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Account Settings",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Profile Settings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                            Text("Edit Profile")
                        }
                        IconButton(onClick = { /* TODO: Navigate to profile edit */ }) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Edit Profile")
                        }
                    }

                    HorizontalDivider()

                    // Notification Settings
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                            Text("Notifications")
                        }
                        IconButton(onClick = { /* TODO: Navigate to notification settings */ }) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Notification Settings")
                        }
                    }
                }
            }

            // About Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "About",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = "About")
                            Text("App Version")
                        }
                        Text("1.0.0")
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(Icons.Default.PrivacyTip, contentDescription = "Privacy")
                            Text("Privacy Policy")
                        }
                        IconButton(onClick = { /* TODO: Navigate to privacy policy */ }) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Privacy Policy")
                        }
                    }
                }
            }
        }
    }
} 