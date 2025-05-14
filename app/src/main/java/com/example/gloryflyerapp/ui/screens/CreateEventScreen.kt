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
    var eventDescription by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf<EventType?>(null) }
    var eventDate by remember { mutableStateOf(LocalDateTime.now()) }
    var isLoading by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Event") },
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
        ) {
            // Event Name
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event Description
            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event Type
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { }
            ) {
                OutlinedTextField(
                    value = eventType?.name ?: "Select Event Type",
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                DropdownMenu(
                    expanded = false,
                    onDismissRequest = { }
                ) {
                    EventType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = { eventType = type }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Event Date
            OutlinedTextField(
                value = eventDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                onValueChange = { },
                label = { Text("Date & Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Create Button
            Button(
                onClick = {
                    if (eventName.isBlank() || eventType == null) {
                        return@Button
                    }
                    
                    isLoading = true
                    val event = Event(
                        id = System.currentTimeMillis().toString(),
                        name = eventName,
                        title = eventName,
                        description = eventDescription,
                        date = eventDate,
                        type = eventType ?: EventType.OTHER
                    )
                    navController.navigate("preview_flyer/${event.id}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading && eventName.isNotBlank() && eventType != null
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