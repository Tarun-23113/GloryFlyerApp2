package com.example.gloryflyerapp.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class EventNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val EVENT_TITLE = "event_title"
        const val EVENT_DESCRIPTION = "event_description"
        const val EVENT_TIME = "event_time"
    }

    override suspend fun doWork(): Result {
        val eventTitle = inputData.getString(EVENT_TITLE) ?: return Result.failure()
        val eventDescription = inputData.getString(EVENT_DESCRIPTION) ?: ""
        val eventTime = inputData.getLong(EVENT_TIME, 0)

        // Calculate time until 6 PM on the day before the event
        val eventDateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(eventTime),
            ZoneId.systemDefault()
        )
        
        val notificationTime = eventDateTime
            .minusDays(1)
            .withHour(18)
            .withMinute(0)
            .withSecond(0)

        val delay = java.time.Duration.between(
            LocalDateTime.now(),
            notificationTime
        ).toMillis()

        if (delay > 0) {
            // Schedule the notification
            val notificationData = workDataOf(
                EVENT_TITLE to eventTitle,
                EVENT_DESCRIPTION to eventDescription
            )

            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(notificationData)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    "event_notification_${eventTime}",
                    ExistingWorkPolicy.REPLACE,
                    notificationWork
                )
        }

        return Result.success()
    }
} 