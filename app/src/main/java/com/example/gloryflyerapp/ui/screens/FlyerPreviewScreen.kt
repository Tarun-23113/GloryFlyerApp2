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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

    LaunchedEffect(eventId) {
        try {
            // TODO: Get event from repository
            val event = Event(
                id = eventId,
                name = "Sample Event",
                type = EventType.BIRTHDAY,
                date = LocalDateTime.now()
            )
            flyerBitmap = FlyerGenerator.generateFlyerBitmap(context, event)
            flyerUri = FlyerGenerator.generateFlyer(context, event)
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showShareDialog = true }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                hasError -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Failed to load flyer",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Go Back")
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            flyerBitmap?.let { bitmap ->
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Flyer Preview",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
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
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Share")
                            }

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
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download")
                            }
                        }
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
