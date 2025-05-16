package com.example.gloryflyerapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val authGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1A237E),  // Deep Indigo
        Color(0xFF3949AB),  // Indigo
        Color(0xFF5C6BC0)   // Light Indigo
    )
)

@Composable
fun AuthBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = authGradient)
    ) {
        content()
    }
} 