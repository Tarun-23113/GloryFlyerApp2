package com.example.gloryflyerapp.ui.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

object ThemeManager {
    private const val PREF_NAME = "theme_preferences"
    private const val KEY_DARK_MODE = "dark_mode"

    var isDarkMode by mutableStateOf(false)
        private set

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun toggleTheme(context: Context) {
        isDarkMode = !isDarkMode
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_DARK_MODE, isDarkMode)
        }
    }
} 