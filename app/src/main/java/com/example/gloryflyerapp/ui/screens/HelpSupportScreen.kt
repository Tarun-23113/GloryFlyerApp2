package com.example.gloryflyerapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavHostController) {
    val context = LocalContext.current
    var showChatDialog by remember { mutableStateOf(false) }
    
    val faqItems = remember {
        listOf(
            FaqItem(
                question = "How do I create a new event?",
                answer = "Tap the '+' button in the bottom navigation bar to create a new event. Fill in the event details like name, date, time, and description."
            ),
            FaqItem(
                question = "How do I share my event flyer?",
                answer = "Open your event from 'My Events', tap the 'Share' button, and choose your preferred sharing method."
            ),
            FaqItem(
                question = "Can I edit an event after creating it?",
                answer = "Yes, you can edit your event by opening it from 'My Events' and tapping the edit icon."
            ),
            FaqItem(
                question = "How do I customize my flyer design?",
                answer = "The app automatically generates beautiful flyers based on your event type and details. We're working on adding custom design options in future updates."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(faqItems) { faq ->
                FaqCard(faq)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Contact Support",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showChatDialog = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                "Live Chat Support",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Chat Support Dialog
    if (showChatDialog) {
        AlertDialog(
            onDismissRequest = { showChatDialog = false },
            title = {
                Text(
                    "Start Live Chat",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "Connect with our support team instantly via WhatsApp for quick assistance.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Our support team is available 24/7 to help you.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/+919876543210") // Replace with your support WhatsApp number
                        }
                        context.startActivity(intent)
                        showChatDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Chat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChatDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FaqCard(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private data class FaqItem(
    val question: String,
    val answer: String
) 