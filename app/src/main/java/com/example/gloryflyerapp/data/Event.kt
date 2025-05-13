package com.example.gloryflyerapp.data

import java.time.LocalDateTime

enum class EventType {
    BIRTHDAY,
    WEDDING,
    ANNIVERSARY,
    OTHER
}

data class Event(
    val id: String = "",
    val name: String,
    val type: EventType,
    val date: LocalDateTime,
    val description: String = "",
    val flyerMessage: String = ""
) {
    fun generateFlyerMessage(): String {
        return when (type) {
            EventType.BIRTHDAY -> "🎉 Join us in celebrating $name's special day! 🎂"
            EventType.WEDDING -> "💒 Join us in celebrating $name's wedding! 💑"
            EventType.ANNIVERSARY -> "💑 Celebrating $name's anniversary! 💖"
            EventType.OTHER -> "🌟 $name - $description"
        }
    }
} 