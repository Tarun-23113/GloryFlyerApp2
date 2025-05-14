package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsListScreen(navController: NavHostController) {
    // Sample events (replace with actual data from your repository)
    val events = remember {
        listOf(
            Event(
                id = "1",
                name = "John's Birthday",
                title = "Birthday Party",
                description = "Birthday celebration",
                date = LocalDateTime.now().plusDays(2),
                type = EventType.BIRTHDAY
            ),
            Event(
                id = "2",
                name = "Mike & Sarah",
                title = "Wedding Anniversary",
                description = "Silver jubilee celebration",
                date = LocalDateTime.now().plusDays(5),
                type = EventType.ANNIVERSARY
            ),
            Event(
                id = "3",
                name = "Annual Meeting",
                title = "Corporate Event",
                description = "Annual meeting",
                date = LocalDateTime.now().plusDays(7),
                type = EventType.OTHER
            ),
            Event(
                id = "4",
                name = "Family Reunion",
                title = "Family Gathering",
                description = "Sunday lunch",
                date = LocalDateTime.now().plusDays(3),
                type = EventType.OTHER
            ),
            Event(
                id = "5",
                name = "Team Building",
                title = "Team Building Event",
                description = "Office event",
                date = LocalDateTime.now().plusDays(10),
                type = EventType.OTHER
            ),
            Event(
                id = "6",
                name = "Product Launch",
                title = "New Product Launch",
                description = "New product release",
                date = LocalDateTime.now().plusDays(15),
                type = EventType.OTHER
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { navController.navigate("preview_flyer/${event.id}") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
} 