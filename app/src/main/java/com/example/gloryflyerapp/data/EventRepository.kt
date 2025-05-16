package com.example.gloryflyerapp.data

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object EventRepository {
    private val _events = mutableStateListOf<Event>()
    private val _eventsFlow = MutableStateFlow<List<Event>>(_events.toList())
    val events: StateFlow<List<Event>> = _eventsFlow.asStateFlow()

    fun addEvent(event: Event) {
        _events.add(event)
        _eventsFlow.value = _events.toList()
    }

    fun getEvent(id: String): Event? {
        return _events.find { it.id == id }
    }

    fun getAllEvents(): List<Event> {
        return _events.toList()
    }

    fun deleteEvent(id: String) {
        _events.removeIf { it.id == id }
        _eventsFlow.value = _events.toList()
    }

    fun updateEvent(event: Event) {
        val index = _events.indexOfFirst { it.id == event.id }
        if (index != -1) {
            _events[index] = event
            _eventsFlow.value = _events.toList()
        }
    }
} 