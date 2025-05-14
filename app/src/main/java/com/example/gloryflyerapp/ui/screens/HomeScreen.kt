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
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    val tabs = listOf("My Events", "Create", "Profile", "Settings")

    // Sample recent events (replace with actual data from your repository)
    val recentEvents = remember {
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
            )
        )
    }

    // Filter events based on search query
    val filteredEvents = remember(searchQuery, recentEvents) {
        if (searchQuery.isEmpty()) {
            recentEvents
        } else {
            recentEvents.filter { event ->
                event.title.contains(searchQuery, ignoreCase = true) ||
                event.description.contains(searchQuery, ignoreCase = true) ||
                event.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            if (isSearchVisible) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { },
                    active = true,
                    onActiveChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search events...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        IconButton(onClick = { 
                            isSearchVisible = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Search")
                        }
                    }
                ) { }
            } else {
                TopAppBar(
                    title = { Text("Glory Flyer") },
                    actions = {
                        IconButton(onClick = { isSearchVisible = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search Events")
                        }
                    }
                )
            }
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
                                0 -> {
                                    try {
                                        navController.navigate("events_list") {
                                            launchSingleTop = true
                                        }
                                    } catch (e: Exception) {
                                        // Handle navigation error
                                        e.printStackTrace()
                                    }
                                }
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
            // Recent Events Header
            if (!isSearchVisible) {
                Text(
                    text = "Recent Events",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            if (filteredEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) 
                            "No events found" 
                        else 
                            "No matching events found for '$searchQuery'",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Events List
                LazyColumn {
                    items(filteredEvents) { event ->
                        EventCard(
                            event = event,
                            onClick = { navController.navigate("preview_flyer/${event.id}") }
                        )
                    }
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