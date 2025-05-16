package com.example.gloryflyerapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object ThemeState {
    var isDarkMode by mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }

    // Dark theme colors
    val darkBackground = Color(0xFF121212)
    val darkSurface = Color(0xFF1E1E1E)
    val darkPrimary = Color(0xFFBB86FC)
    val darkSecondary = Color(0xFF03DAC6)
    val darkError = Color(0xFFCF6679)
    val darkOnPrimary = Color(0xFF000000)
    val darkOnSecondary = Color(0xFF000000)
    val darkOnBackground = Color(0xFFFFFFFF)
    val darkOnSurface = Color(0xFFFFFFFF)
    val darkOnError = Color(0xFF000000)
} 