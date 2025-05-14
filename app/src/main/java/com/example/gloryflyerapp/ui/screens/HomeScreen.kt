package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("My Events", "Create", "Profile", "Settings")

    // Sample recent events (replace with actual data from your repository)
    val recentEvents = remember {
        listOf(
            Event("1", "Birthday Party", "Birthday celebration", LocalDateTime.now().plusDays(2)),
            Event("2", "Wedding Anniversary", "Silver jubilee celebration", LocalDateTime.now().plusDays(5)),
            Event("3", "Corporate Event", "Annual meeting", LocalDateTime.now().plusDays(7)),
            Event("4", "Family Gathering", "Sunday lunch", LocalDateTime.now().plusDays(3))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Glory Flyer") },
                actions = {
                    // Search button
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Events")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                when (index) {
                                    0 -> Icons.Default.Event
                                    1 -> Icons.Default.Add
                                    2 -> Icons.Default.Person
                                    else -> Icons.Default.Settings
                                },
                                contentDescription = title
                            )
                        },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            when (index) {
                                0 -> navController.navigate("events_list")
                                1 -> navController.navigate("create_event")
                                2 -> navController.navigate("profile")
                                3 -> navController.navigate("settings")
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = { /* TODO: Implement search */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search Events") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            // Recent Events Header
            Text(
                text = "Recent Events",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Recent Events List
            LazyColumn {
                items(recentEvents) { event ->
                    EventCard(
                        event = event,
                        onClick = { navController.navigate("preview_flyer/${event.id}") }
                    )
                }
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

// Data class for Event (you can move this to a separate file)
private data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDateTime
) 