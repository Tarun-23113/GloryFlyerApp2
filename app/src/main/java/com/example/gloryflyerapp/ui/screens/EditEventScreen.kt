package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType
import com.example.gloryflyerapp.data.EventRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    navController: NavHostController,
    eventId: String
) {
    // Get event from repository
    val event = remember(eventId) { EventRepository.getEvent(eventId) }
    
    var eventName by remember { mutableStateOf(event?.title ?: "") }
    var eventType by remember { mutableStateOf(event?.type) }
    var eventDate by remember { mutableStateOf(event?.date ?: LocalDateTime.now()) }
    var eventDescription by remember { mutableStateOf(event?.description ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = eventDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    if (event == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = "Event not found",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Event") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Event Name
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event Type
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = eventType?.name?.replace("_", " ") ?: "Select Event Type",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    EventType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name.replace("_", " ")) },
                            onClick = {
                                eventType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date and Time Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Date Selection
                OutlinedTextField(
                    value = eventDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                        }
                    }
                )

                // Time Selection
                OutlinedTextField(
                    value = eventDate.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { },
                    label = { Text("Time") },
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Select Time")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Event Description
            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    if (eventName.isBlank() || eventType == null) {
                        return@Button
                    }
                    
                    isLoading = true
                    val updatedEvent = Event(
                        id = event.id,
                        name = eventName,
                        title = eventName,
                        description = eventDescription,
                        date = eventDate,
                        type = eventType ?: EventType.OTHER
                    )
                    // Update the event
                    EventRepository.updateEvent(updatedEvent)
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
                    Text("Save Changes")
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            // Update eventDate while preserving the time
                            val currentTime = LocalTime.of(eventDate.hour, eventDate.minute)
                            eventDate = LocalDateTime.of(selectedDate, currentTime)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        CustomTimePickerDialog(
            onTimeSelected = { hour, minute ->
                val currentDate = eventDate.toLocalDate()
                eventDate = LocalDateTime.of(currentDate, LocalTime.of(hour, minute))
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false },
            initialHour = eventDate.hour,
            initialMinute = eventDate.minute
        )
    }

    // Snackbar
    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text(snackbarMessage)
        }
    }
}

@Composable
private fun CustomTimePickerDialog(
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
    initialHour: Int = LocalTime.now().hour,
    initialMinute: Int = LocalTime.now().minute
) {
    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            Column {
                // Hour selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hour:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { 
                            if (selectedHour > 0) selectedHour-- 
                        }) {
                            Icon(Icons.Default.Remove, "Decrease hour")
                        }
                        Text(selectedHour.toString().padStart(2, '0'))
                        IconButton(onClick = { 
                            if (selectedHour < 23) selectedHour++ 
                        }) {
                            Icon(Icons.Default.Add, "Increase hour")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Minute selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Minute:")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { 
                            if (selectedMinute > 0) selectedMinute-- 
                        }) {
                            Icon(Icons.Default.Remove, "Decrease minute")
                        }
                        Text(selectedMinute.toString().padStart(2, '0'))
                        IconButton(onClick = { 
                            if (selectedMinute < 59) selectedMinute++ 
                        }) {
                            Icon(Icons.Default.Add, "Increase minute")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                onTimeSelected(selectedHour, selectedMinute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 