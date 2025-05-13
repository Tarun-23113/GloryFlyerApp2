package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType
import com.example.gloryflyerapp.ui.components.DatePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavHostController) {
    var eventName by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf<EventType?>(null) }
    var eventDate by remember { mutableStateOf(LocalDateTime.now()) }
    var description by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Event Name
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Event, contentDescription = null)
                }
            )

            // Event Type
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTypeDialog = true }
            ) {
                OutlinedTextField(
                    value = eventType?.name ?: "Select Event Type",
                    onValueChange = { },
                    label = { Text("Event Type") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Category, contentDescription = null)
                    },
                    readOnly = true,
                    enabled = false
                )
            }

            // Event Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = eventDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")),
                    onValueChange = { },
                    label = { Text("Event Date") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    },
                    readOnly = true,
                    enabled = false
                )
            }

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Create Button
            Button(
                onClick = {
                    if (eventName.isBlank() || eventType == null) {
                        // Show error
                        return@Button
                    }
                    
                    isLoading = true
                    // TODO: Save event to repository
                    val event = Event(
                        id = System.currentTimeMillis().toString(),
                        name = eventName,
                        type = eventType ?: EventType.OTHER,
                        date = eventDate,
                        description = description
                    )
                    navController.navigate("preview_flyer/${event.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading && eventName.isNotBlank() && eventType != null,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Event")
                }
            }
        }
    }

    if (showTypeDialog) {
        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            title = { Text("Select Event Type") },
            text = {
                Column {
                    EventType.values().forEach { type ->
                        ListItem(
                            headlineContent = { Text(type.name) },
                            leadingContent = {
                                Icon(
                                    when (type) {
                                        EventType.BIRTHDAY -> Icons.Default.Cake
                                        EventType.WEDDING -> Icons.Default.Favorite
                                        EventType.ANNIVERSARY -> Icons.Default.Favorite
                                        EventType.OTHER -> Icons.Default.Event
                                    },
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                eventType = type
                                showTypeDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTypeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { 
                eventDate = it
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
} 