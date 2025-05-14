package com.example.gloryflyerapp.data

import java.time.LocalDateTime

enum class EventType {
    BIRTHDAY,
    WEDDING,
    ANNIVERSARY,
    OTHER
}

data class Event(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val date: LocalDateTime,
    val type: EventType = EventType.OTHER
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