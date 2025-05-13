package com.example.gloryflyerapp.ui.screens

import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Events", "Create", "Profile", "Settings")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Glory Flyer") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
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
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome to Glory Flyer",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Quick Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.Event,
                    text = "My Events",
                    onClick = { navController.navigate("events_list") }
                )
                QuickActionButton(
                    icon = Icons.Default.Add,
                    text = "Create Event",
                    onClick = { navController.navigate("create_event") }
                )
                QuickActionButton(
                    icon = Icons.Default.Person,
                    text = "Profile",
                    onClick = { navController.navigate("profile") }
                )
            }

            // Recent Events
            Text(
                text = "Recent Events",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // TODO: Replace with actual data from repository
            val recentEvents = remember {
                listOf(
                    Event(
                        id = "1",
                        name = "Birthday Party",
                        type = EventType.BIRTHDAY,
                        date = java.time.LocalDateTime.now().plusDays(7)
                    ),
                    Event(
                        id = "2",
                        name = "Wedding Ceremony",
                        type = EventType.WEDDING,
                        date = java.time.LocalDateTime.now().plusMonths(1)
                    )
                )
            }

            recentEvents.forEach { event ->
                ListItem(
                    headlineContent = { Text(event.name) },
                    supportingContent = { Text(event.date.toString()) },
                    leadingContent = {
                        Icon(
                            when (event.type) {
                                EventType.BIRTHDAY -> Icons.Default.Cake
                                EventType.WEDDING -> Icons.Default.Favorite
                                EventType.ANNIVERSARY -> Icons.Default.Favorite
                                EventType.OTHER -> Icons.Default.Event
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("preview_flyer/${event.id}")
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 