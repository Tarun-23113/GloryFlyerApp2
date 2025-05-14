package com.example.gloryflyerapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType
import com.example.gloryflyerapp.utils.FileUtils
import com.example.gloryflyerapp.utils.FlyerGenerator
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlyerPreviewScreen(
    navController: NavHostController,
    eventId: String
) {
    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var flyerBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var flyerUri by remember { mutableStateOf<android.net.Uri?>(null) }
    
    // Add event state
    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(eventId) {
        try {
            // Create sample event (replace with actual event fetching logic)
            event = Event(
                id = eventId,
                name = "Sample Event",
                title = "Sample Event Title",
                description = "This is a sample event description",
                date = LocalDateTime.now().plusDays(7),
                type = EventType.OTHER
            )
            flyerBitmap = event?.let { FlyerGenerator.generateFlyerBitmap(context, it) }
            flyerUri = event?.let { FlyerGenerator.generateFlyer(context, it) }
            isLoading = false
        } catch (e: Exception) {
            hasError = true
            isLoading = false
            snackbarMessage = "Failed to generate flyer: ${e.message}"
            showSnackbar = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flyer Preview") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Flyer Preview Content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (hasError) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Failed to load flyer")
                    }
                } else {
                    event?.let { currentEvent ->
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = currentEvent.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = currentEvent.description,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = currentEvent.date.format(java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        flyerBitmap?.let { bitmap ->
                            val fileName = FileUtils.generateFileName("Event_$eventId")
                            val uri = FileUtils.getSharedFileUri(context, bitmap, fileName)
                            uri?.let {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "image/png"
                                    putExtra(Intent.EXTRA_STREAM, it)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Flyer"))
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            flyerBitmap?.let { bitmap ->
                                val fileName = FileUtils.generateFileName("Event_$eventId")
                                val uri = FileUtils.saveBitmapToGallery(context, bitmap, fileName)
                                if (uri != null) {
                                    snackbarMessage = "Flyer saved to gallery"
                                    showSnackbar = true
                                } else {
                                    snackbarMessage = "Failed to save flyer"
                                    showSnackbar = true
                                }
                            }
                        } else {
                            snackbarMessage = "Storage permission required"
                            showSnackbar = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download")
                }
            }
        }
    }

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

    if (showShareDialog) {
        ShareDialog(
            onDismiss = { showShareDialog = false },
            onShare = { shareType ->
                flyerUri?.let { uri ->
                    shareFlyer(context, uri, shareType)
                }
                showShareDialog = false
            }
        )
    }
}

@Composable
private fun ShareDialog(
    onDismiss: () -> Unit,
    onShare: (ShareType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share Flyer") },
        text = {
            Column {
                ShareOption(
                    icon = Icons.Default.Image,
                    title = "Save to Gallery",
                    onClick = { onShare(ShareType.GALLERY) }
                )
                ShareOption(
                    icon = Icons.Default.Share,
                    title = "Share via...",
                    onClick = { onShare(ShareType.SHARE) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShareOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(
                icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

fun Modifier.Companion.clickable(onClick: () -> Unit): Modifier {
    TODO("Not yet implemented")
}

private fun shareFlyer(context: Context, uri: android.net.Uri, shareType: ShareType) {
    when (shareType) {
        ShareType.GALLERY -> {
            // TODO: Save to gallery
        }
        ShareType.SHARE -> {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Flyer"))
        }
    }
}

private enum class ShareType {
    GALLERY,
    SHARE
}
